package cn.sparrowmini.pem.service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.relation.GroupUser;
import cn.sparrowmini.pem.model.relation.GroupUser.GroupUserId;

public interface GroupUserRepository extends JpaRepository<GroupUser, GroupUserId> {

	List<GroupUser> findByIdUsername(String username);

	Page<GroupUser> findByIdGroupId(String groupId, Pageable pageable);

}
