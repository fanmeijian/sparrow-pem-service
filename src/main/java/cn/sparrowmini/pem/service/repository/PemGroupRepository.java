package cn.sparrowmini.pem.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.Group;

public interface PemGroupRepository extends JpaRepository<Group, String> {

}
