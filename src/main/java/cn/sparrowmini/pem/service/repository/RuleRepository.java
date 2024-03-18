package cn.sparrowmini.pem.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.Rule;

public interface RuleRepository extends JpaRepository<Rule, String>{

}
