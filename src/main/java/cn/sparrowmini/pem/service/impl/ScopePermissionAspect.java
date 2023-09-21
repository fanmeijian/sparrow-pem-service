package cn.sparrowmini.pem.service.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.sparrowmini.common.CurrentUser;
import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.model.relation.SysroleScope.SysroleScopePK;
import cn.sparrowmini.pem.model.relation.UserScope.UserScopePK;
import cn.sparrowmini.pem.model.relation.UserSysrole;
import cn.sparrowmini.pem.service.ScopePermission;
import cn.sparrowmini.pem.service.exception.NoPermissionException;
import cn.sparrowmini.pem.service.repository.ScopeRepository;
import cn.sparrowmini.pem.service.repository.SysroleScopeRepository;
import cn.sparrowmini.pem.service.repository.UserScopeRepository;
import cn.sparrowmini.pem.service.repository.UserSysroleRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ScopePermissionAspect {
	@Autowired
	private UserScopeRepository userScopeRepository;

	@Autowired
	private SysroleScopeRepository sysroleScopeRepository;

	@Autowired
	private UserSysroleRepository userSysroleRepository;

	@Autowired
	private ScopeRepository scopeRepository;

	@Around("@annotation(scopePermission)")
	public Object hasPermission(ProceedingJoinPoint joinPoint, ScopePermission scopePermission) throws Throwable {
		log.debug("username {}, scope code: {}", CurrentUser.get(), scopePermission.scope());
		String username = CurrentUser.get();
		String scopeId = this.scopeRepository.findByCode(scopePermission.scope()).getId();

		// check user scope permission
		if (this.userScopeRepository.findById(new UserScopePK(username, scopeId)).orElse(null) != null) {
			return joinPoint.proceed();
		}

		// check sysrole scope permission
		for (UserSysrole userSysrole : this.userSysroleRepository.findByIdUsername(username)) {
			String sysroleId = userSysrole.getId().getSysroleId();
			if (this.sysroleScopeRepository.findById(new SysroleScopePK(sysroleId, scopeId)).orElse(null) != null) {
				return joinPoint.proceed();
			}
		}
		throw new NoPermissionException("没有权限: " + scopePermission.name() + PermissionEnum.EXECUTE);
	}
}
