package cn.sparrowmini.pem.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.DataPermissionGroup;
import cn.sparrowmini.pem.model.DataPermissionGroup.DataPermissionGroupId;

public interface DataPermissionGroupRepository extends JpaRepository<DataPermissionGroup, DataPermissionGroupId> {
	List<DataPermissionGroup> findByIdDataPermissionId(String dataPermissionId);
}
