package cn.sparrowmini.pem.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.relation.GroupSysrole;
import cn.sparrowmini.pem.model.relation.GroupSysrole.GroupSysrolePK;

public interface GroupSysroleRepository extends JpaRepository<GroupSysrole, GroupSysrolePK> {

}
