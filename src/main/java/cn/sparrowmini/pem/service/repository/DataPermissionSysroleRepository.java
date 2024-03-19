package cn.sparrowmini.pem.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.DataPermissionSysrole;
import cn.sparrowmini.pem.model.DataPermissionSysrole.DataPermissionSysroleId;

public interface DataPermissionSysroleRepository extends JpaRepository<DataPermissionSysrole, DataPermissionSysroleId> {

}
