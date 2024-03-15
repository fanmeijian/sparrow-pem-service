package cn.sparrowmini.pem.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.sparrowmini.pem.model.ModelRule;
import cn.sparrowmini.pem.model.ModelRule.ModelRuleId;

public interface ModelRuleRepository extends JpaRepository<ModelRule, ModelRuleId> {
	List<ModelRule> findByIdModelId(String modelId);
}
