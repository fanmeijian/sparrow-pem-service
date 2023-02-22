package cn.sparrowmini.pem.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.relation.GroupRelation;
import cn.sparrowmini.pem.model.relation.GroupRelation.GroupRelationId;

public interface PemGroupRelationRepository extends JpaRepository<GroupRelation, GroupRelationId> {

}
