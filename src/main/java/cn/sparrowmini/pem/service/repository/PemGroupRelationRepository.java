package cn.sparrowmini.pem.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.relation.GroupRelation;
import cn.sparrowmini.pem.model.relation.GroupRelation.GroupRelationId;

public interface PemGroupRelationRepository extends JpaRepository<GroupRelation, GroupRelationId> {

	Page<GroupRelation> findByIdParentId(String parentId, Pageable unpaged);

	Page<GroupRelation> findByIdGroupId(String groupId, Pageable pageable);

}
