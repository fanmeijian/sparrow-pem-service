package cn.sparrowmini.pem.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.DataPermissionSysrole;
import cn.sparrowmini.pem.model.DataPermissionSysrole.DataPermissionSysroleId;

public interface DataPermissionSysroleRepository extends JpaRepository<DataPermissionSysrole, DataPermissionSysroleId> {
	List<DataPermissionSysrole> findByIdDataPermissionId(String dataPermissionId);
}
