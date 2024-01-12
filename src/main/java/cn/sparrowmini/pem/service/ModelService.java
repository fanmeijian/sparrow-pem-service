package cn.sparrowmini.pem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.pem.model.Model;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "datamodel", description = "数据模型服务")
@RequestMapping("/models")
public interface ModelService {

	@Operation(summary = "浏览模型", operationId = "models")
	@GetMapping("")
	@ResponseBody
	public Page<Model> models(@Nullable Pageable pageable, @Nullable Model model);

	@Operation(summary = "设置模型权限",operationId = "addModelPermissions")
	@PostMapping("/{modelId}/permissions")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public void addPermission(@PathVariable String modelId, @RequestBody PermissionRequestBody body);

	@Operation(summary = "模型权限列表", operationId = "modelPermissions")
	@GetMapping(value = "/{modelId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ModelPermissionResponseBody permissions(@PathVariable String modelId);

	@Operation(summary = "删除模型权限", operationId = "removeModelPermissions")
	@PostMapping("/{modelId}/permissions/remove")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removePermission(@PathVariable String modelId, @RequestBody PermissionRequestBody body);

}
