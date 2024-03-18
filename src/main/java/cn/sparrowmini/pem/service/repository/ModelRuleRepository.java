package cn.sparrowmini.pem.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.sparrowmini.pem.model.ModelRule;
import cn.sparrowmini.pem.model.ModelRule.ModelRuleId;
import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.model.constant.PermissionTypeEnum;

public interface ModelRuleRepository extends JpaRepository<ModelRule, ModelRuleId> {
	List<ModelRule> findByIdModelId(String modelId);

	@Query("select m from ModelRule m where m.id.modelId=?1 and m.id.permissionType=?2 and m.id.permission=?3")
	List<ModelRule> findByPermission(String modelId, PermissionTypeEnum permissionType, PermissionEnum permission);
}
