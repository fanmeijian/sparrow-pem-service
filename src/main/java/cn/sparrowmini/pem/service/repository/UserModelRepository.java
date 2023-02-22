package cn.sparrowmini.pem.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.relation.UserModel;
import cn.sparrowmini.pem.model.relation.UserModel.UserModelId;

public interface UserModelRepository extends JpaRepository<UserModel, UserModelId> {
	Page<UserModel> findByIdModelId(String modelId, Pageable pageable);

	Page<UserModel> findByIdModelIdAndIdUsername(String modelId, String username, Pageable pageable);
}
