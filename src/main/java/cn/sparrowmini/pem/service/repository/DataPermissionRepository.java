package cn.sparrowmini.pem.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.DataPermission;

public interface DataPermissionRepository extends JpaRepository<DataPermission, String> {

}
