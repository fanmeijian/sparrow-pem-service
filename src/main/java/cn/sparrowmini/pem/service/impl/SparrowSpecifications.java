package cn.sparrowmini.pem.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;
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

public class SparrowSpecifications {

	public static Specification<Object> permissionSpecification() {
		return new Specification<Object>() {
			private static final long serialVersionUID = 1L;
			String username = SecurityContextHolder.getContext().getAuthentication().getName();

			List<Predicate> predicates = new ArrayList<Predicate>();

			public Predicate toPredicate(Root<Object> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

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
				dataPermissionSysroleSubquery.where(criteriaBuilder.and(
						dataPermissionSysroleRoot.get(DataPermissionSysrole_.ID).get("sysroleId").in(sysroleSubquery),
						criteriaBuilder.equal(
								dataPermissionSysroleRoot.get(DataPermissionUsername_.ID).get("permissionType"),
								PermissionTypeEnum.ALLOW),
						criteriaBuilder.equal(
								dataPermissionSysroleRoot.get(DataPermissionUsername_.ID).get("permission"),
								PermissionEnum.READER)));

				Subquery<DataPermissionSysrole> dataPermissionSysroleDenySubquery = query
						.subquery(DataPermissionSysrole.class);
				Root<DataPermissionSysrole> dataPermissionSysroleDenyRoot = dataPermissionSysroleDenySubquery
						.from(DataPermissionSysrole.class);
				dataPermissionSysroleDenySubquery
						.select(dataPermissionSysroleDenyRoot.get(DataPermissionSysrole_.ID).get("dataPermissionId"));
				dataPermissionSysroleDenySubquery.where(criteriaBuilder.and(
						dataPermissionSysroleDenyRoot.get(DataPermissionSysrole_.ID).get("sysroleId")
								.in(sysroleSubquery),
						criteriaBuilder.equal(
								dataPermissionSysroleDenyRoot.get(DataPermissionUsername_.ID).get("permissionType"),
								PermissionTypeEnum.DENY),
						criteriaBuilder.equal(
								dataPermissionSysroleDenyRoot.get(DataPermissionUsername_.ID).get("permission"),
								PermissionEnum.READER)));

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

				predicates.add(criteriaBuilder.and(root.get("dataPermissionId").in(dataPermissionSysroleSubquery),
						root.get("dataPermissionId").in(dataPermissionSysroleDenySubquery).not()));

				query.distinct(true);
				return criteriaBuilder.or(predicates.toArray(new Predicate[0]));

			}
		};

	}

}
