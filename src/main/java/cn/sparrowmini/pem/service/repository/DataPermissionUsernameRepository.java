package cn.sparrowmini.pem.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.DataPermissionUsername;
import cn.sparrowmini.pem.model.DataPermissionUsername.DataPermissionUsernameId;

public interface DataPermissionUsernameRepository extends JpaRepository<DataPermissionUsername, DataPermissionUsernameId> {
	List<DataPermissionUsername> findByIdDataPermissionId(String dataPermissionId);
}
