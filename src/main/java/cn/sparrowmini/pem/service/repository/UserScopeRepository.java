package cn.sparrowmini.pem.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.relation.UserScope;
import cn.sparrowmini.pem.model.relation.UserScope.UserScopePK;

public interface UserScopeRepository extends JpaRepository<UserScope, UserScopePK> {

	Page<UserScope> findByIdScopeId(String scopeId, Pageable pageable);

}
