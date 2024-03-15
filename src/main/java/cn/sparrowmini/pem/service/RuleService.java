package cn.sparrowmini.pem.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sparrowmini.pem.model.Rule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "rule", description = "规则服务")
@RequestMapping("/rules")
public interface RuleService {
	@PostMapping("")
	@Operation(summary = "新增规则", operationId = "newRule")
	@ResponseBody
	public Rule create(@RequestBody Rule rule);
}
