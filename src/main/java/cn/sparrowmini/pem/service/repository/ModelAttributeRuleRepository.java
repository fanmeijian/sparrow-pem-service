package cn.sparrowmini.pem.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.sparrowmini.pem.model.ModelAttribute.ModelAttributePK;
import cn.sparrowmini.pem.model.ModelAttributeRule;
import cn.sparrowmini.pem.model.ModelAttributeRule.ModelAttributeRuleId;
import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.model.constant.PermissionTypeEnum;

public interface ModelAttributeRuleRepository extends JpaRepository<ModelAttributeRule, ModelAttributeRuleId> {
	List<ModelAttributeRule> findByIdModelAttributeId(ModelAttributePK modelAttributeId);

	@Query("select m from ModelAttributeRule m where m.id.modelAttributeId.modelId=?1 and m.id.modelAttributeId.attributeId=?2 and m.id.permissionType=?3 and m.id.permission=?4")
	List<ModelAttributeRule> findByPermission(String modelId, String attributeId, PermissionTypeEnum permissionType,
			PermissionEnum permission);
}
