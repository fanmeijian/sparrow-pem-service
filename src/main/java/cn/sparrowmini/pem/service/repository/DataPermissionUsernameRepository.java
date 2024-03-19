package cn.sparrowmini.pem.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.DataPermissionUsername;
import cn.sparrowmini.pem.model.DataPermissionUsername.DataPermissionUsernameId;

public interface DataPermissionUsernameRepository extends JpaRepository<DataPermissionUsername, DataPermissionUsernameId> {

}
