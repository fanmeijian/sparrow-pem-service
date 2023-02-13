package cn.sparrowmini.pem.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.Scope;

public interface ScopeRepository extends JpaRepository<Scope, String> {

}
