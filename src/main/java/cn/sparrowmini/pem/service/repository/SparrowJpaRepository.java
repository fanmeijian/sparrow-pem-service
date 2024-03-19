package cn.sparrowmini.pem.service.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.security.core.context.SecurityContextHolder;

import cn.sparrowmini.pem.model.DataPermissionSysrole;
import cn.sparrowmini.pem.model.DataPermissionSysrole_;
import cn.sparrowmini.pem.model.DataPermissionUsername;
import cn.sparrowmini.pem.model.DataPermissionUsername_;
import cn.sparrowmini.pem.model.Sysrole;
import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.model.constant.PermissionTypeEnum;
import cn.sparrowmini.pem.model.relation.UserSysrole;
import cn.sparrowmini.pem.model.relation.UserSysrole_;

/**
 * 用于实现控制数据权限过滤
 * 
 * @param <T>
 * @param <ID>
 */

@NoRepositoryBean
public interface SparrowJpaRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

	@Override
	default List<T> findAll(Sort sort) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(username);
		return null;
	};

	@Override
	default List<T> findAll() {
		Specification<T> specification = new Specification<T>() {
			private static final long serialVersionUID = 1L;
			String username = SecurityContextHolder.getContext().getAuthentication().getName();

			List<Predicate> predicates = new ArrayList<Predicate>();

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				// 针对角色的授权
				Subquery<UserSysrole> userSysroleSubquery = query.subquery(UserSysrole.class);
				Root<UserSysrole> userSysroleRoot = userSysroleSubquery.from(UserSysrole.class);
				userSysroleSubquery.select(userSysroleRoot.get("id").get("sysroleId"));
				userSysroleSubquery
						.where(criteriaBuilder.equal(userSysroleRoot.get(UserSysrole_.ID).get("username"), username));

				Subquery<Sysrole> sysroleSubquery = query.subquery(Sysrole.class);
				Root<Sysrole> sysroleRoot = sysroleSubquery.from(Sysrole.class);
				sysroleSubquery.select(sysroleRoot.get("id"));
				sysroleSubquery.where(criteriaBuilder.in(sysroleRoot.get("id")).value(userSysroleSubquery));

				Subquery<DataPermissionSysrole> dataPermissionSysroleSubquery = query
						.subquery(DataPermissionSysrole.class);
				Root<DataPermissionSysrole> dataPermissionSysroleRoot = dataPermissionSysroleSubquery
						.from(DataPermissionSysrole.class);
				dataPermissionSysroleSubquery
						.select(dataPermissionSysroleRoot.get(DataPermissionSysrole_.ID).get("dataPermissionId"));
				dataPermissionSysroleSubquery.where(criteriaBuilder
						.in(dataPermissionSysroleRoot.get("id").get("sysroleId")).value(sysroleSubquery));

				// 针对个人的授权
				Subquery<DataPermissionUsername> dataPermissionUsernameSubquery = query
						.subquery(DataPermissionUsername.class);
				Root<DataPermissionUsername> dataPermissionUsernameRoot = dataPermissionUsernameSubquery
						.from(DataPermissionUsername.class);
				dataPermissionUsernameSubquery
						.select(dataPermissionUsernameRoot.get(DataPermissionUsername_.ID).get("dataPermissionId"));
				dataPermissionUsernameSubquery.where(criteriaBuilder.and(
						criteriaBuilder.equal(
								dataPermissionUsernameRoot.get(DataPermissionUsername_.ID).get("username"), username),
						criteriaBuilder.equal(
								dataPermissionUsernameRoot.get(DataPermissionUsername_.ID).get("permissionType"),
								PermissionTypeEnum.ALLOW),
						criteriaBuilder.equal(
								dataPermissionUsernameRoot.get(DataPermissionUsername_.ID).get("permission"),
								PermissionEnum.READER)));

				Subquery<DataPermissionUsername> dataPermissionUsernameDenySubquery = query
						.subquery(DataPermissionUsername.class);
				Root<DataPermissionUsername> dataPermissionUsernameDenyRoot = dataPermissionUsernameDenySubquery
						.from(DataPermissionUsername.class);
				dataPermissionUsernameDenySubquery
						.select(dataPermissionUsernameDenyRoot.get(DataPermissionUsername_.ID).get("dataPermissionId"));
				dataPermissionUsernameDenySubquery.where(criteriaBuilder.and(criteriaBuilder.equal(
						dataPermissionUsernameDenyRoot.get(DataPermissionUsername_.ID).get("username"), username),
						criteriaBuilder.equal(
								dataPermissionUsernameDenyRoot.get(DataPermissionUsername_.ID).get("permissionType"),
								PermissionTypeEnum.DENY),
						criteriaBuilder.equal(
								dataPermissionUsernameDenyRoot.get(DataPermissionUsername_.ID).get("permission"),
								PermissionEnum.READER)));

				predicates.add(root.get("dataPermissionId").isNull());
				predicates.add(criteriaBuilder.and(root.get("dataPermissionId").in(dataPermissionUsernameSubquery),
						root.get("dataPermissionId").in(dataPermissionUsernameDenySubquery).not()));

				predicates.add(criteriaBuilder.in(root.get("dataPermissionId")).value(dataPermissionSysroleSubquery));

				query.distinct(true);
				return criteriaBuilder.or(predicates.toArray(new Predicate[0]));

			}

		};
		return findAll(specification);
	};

	@Override
	Page<T> findAll(Pageable pageable);

	@Override
	<S extends T> List<S> findAll(Example<S> example);

	@Override
	<S extends T> List<S> findAll(Example<S> example, Sort sort);
}
