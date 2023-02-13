package cn.sparrowmini.pem.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.SysroleScope;
import cn.sparrowmini.pem.model.SysroleScope.SysroleScopePK;

public interface SysroleScopeRepository extends JpaRepository<SysroleScope, SysroleScopePK> {

	Page<SysroleScope> findByIdScopeId(String scopeId, Pageable pageable);

}
