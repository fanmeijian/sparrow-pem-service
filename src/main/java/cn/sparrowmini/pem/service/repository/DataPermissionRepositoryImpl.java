//package cn.sparrowmini.pem.service.repository;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//import javax.persistence.criteria.Subquery;
//
//import org.springframework.data.jpa.repository.support.JpaEntityInformation;
//import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
//
//import cn.sparrowmini.pem.model.DataReadPermission;
//
//public class DataPermissionRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
//		implements ExtendedJpaRepository<T, ID> {
//
//	private static final long serialVersionUID = 1L;
//
//	private EntityManager entityManager;
//
//	public DataPermissionRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
//		super(entityInformation, entityManager);
//		this.entityManager = entityManager;
//	}
//
//	public List<T> findAll() {
//		List<String> userSysrolesList = entityManager.createQuery("select id from Sysrole s where s.code='ADMIN1'")
//				.getResultList();
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<T> cQuery = builder.createQuery(getDomainClass());
//		Root<T> root = cQuery.from(getDomainClass());
//		List<Predicate> predicates = new ArrayList<>();
//		predicates.add(root.get("dataReadPermissionId").isNull());
//
////		for (String title : userSysrolesList) {
////			predicates.add(builder.isMember(title, root.get("dataReadPermission").get("sysroles")));
////		}
////
////		Predicate finalPredicate = builder.or(predicates.toArray(new Predicate[] {}));
//
//		Subquery<DataReadPermission> subquery = cQuery.subquery(DataReadPermission.class);
//		Root<DataReadPermission> dept = subquery.from(DataReadPermission.class);
//
//		for (String title : userSysrolesList) {
//			predicates.add(builder.isMember(title, dept.get("sysroles")));
//		}
//
////		Predicate finalPredicate = builder.or(predicates.toArray(new Predicate[] {}));
//
//		subquery.select(dept).distinct(true).where(builder.or(predicates.toArray(new Predicate[] {})));
//
////		Join<T, DataReadPermission> join = root.join(root.get("dataReadPermission"),JoinType.LEFT);
////		cQuery.select(root).where(builder.or(root.get("dataReadPermissionId").isNull()),
////				builder.in(root.get("dataReadPermissionId")).value(subquery));
//		Predicate finalPredicate = builder.in(root.get("dataReadPermissionId")).value(subquery);
//		cQuery.select(root).where(builder.or(root.get("dataReadPermissionId").isNull(),finalPredicate));
//		TypedQuery<T> query = entityManager.createQuery(cQuery);
//		return query.getResultList();
//
//	}
//
//}
