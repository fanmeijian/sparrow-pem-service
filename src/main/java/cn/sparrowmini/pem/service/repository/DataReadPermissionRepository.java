package cn.sparrowmini.pem.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.DataReadPermission;

public interface DataReadPermissionRepository extends JpaRepository<DataReadPermission, String> {

}
