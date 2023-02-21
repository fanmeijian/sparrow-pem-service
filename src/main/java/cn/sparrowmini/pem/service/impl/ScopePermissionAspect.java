package cn.sparrowmini.pem.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import cn.sparrowmini.common.CurrentUser;
import cn.sparrowmini.pem.model.Scope;
import cn.sparrowmini.pem.model.UserScope;
import cn.sparrowmini.pem.model.UserScope.UserScopePK;
import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.service.PermissionService;
import cn.sparrowmini.pem.service.ScopePermission;
import cn.sparrowmini.pem.service.exception.NoPermissionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ScopePermissionAspect {

	@PersistenceContext
	EntityManager entityManager;

	@Around("@annotation(scopePermission)")
	public Object hasPermission(ProceedingJoinPoint joinPoint, ScopePermission scopePermission) throws Throwable {
		log.debug("cur user: {}", CurrentUser.get());
		log.debug("permission aspect work: {}", scopePermission.username());
		PermissionService permissionService = new PermissionServiceImpl(entityManager);
		log.debug("entityManager : {}", entityManager);
		if (!scopePermission.username().equals("$curUser")) {
			log.debug("cur user: {}", CurrentUser.get());
		}
		Query query = entityManager.createNamedQuery("Scope.findByCode", Scope.class);
		query.setParameter("code", scopePermission.scope());
		try {
			Scope scope = (Scope) query.getSingleResult();
			UserScope userScope = this.entityManager.find(UserScope.class, new UserScopePK(CurrentUser.get(), scope.getId()));
			if(userScope==null) {
				throw new NoPermissionException("没有权限: " + scopePermission.scope() + PermissionEnum.EXECUTE);
			}
//			PermissionToken permissionToken = entityManager
//					.find(SparrowPermissionToken.class, scope.getPermissionTokenId()).getPermissionToken();
//			if (permissionToken != null) {
//				if (!permissionService.hasPermission(CurrentUser.get(), permissionToken, PermissionEnum.EXECUTE)) {
//					throw new NoPermissionException("没有权限: " + scopePermission.scope() + PermissionEnum.EXECUTE);
//				}
//			}
		} catch (NoResultException e) {
			// TODO: handle exception
		}

		return joinPoint.proceed();
	}
}
