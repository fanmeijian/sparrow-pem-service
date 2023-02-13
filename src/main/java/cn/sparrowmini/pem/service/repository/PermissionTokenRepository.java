package cn.sparrowmini.pem.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.token.SparrowPermissionToken;

public interface PermissionTokenRepository extends JpaRepository<SparrowPermissionToken, String> {

}
