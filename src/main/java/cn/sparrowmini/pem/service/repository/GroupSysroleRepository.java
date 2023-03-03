package cn.sparrowmini.pem.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.relation.GroupSysrole;
import cn.sparrowmini.pem.model.relation.GroupSysrole.GroupSysrolePK;

public interface GroupSysroleRepository extends JpaRepository<GroupSysrole, GroupSysrolePK> {

	Page<GroupSysrole> findByIdGroupId(String groupId, Pageable pageable);

}
