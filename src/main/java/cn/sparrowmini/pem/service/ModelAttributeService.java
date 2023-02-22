package cn.sparrowmini.pem.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "datamodel", description = "数据模型服务")
@RequestMapping("/models")
public interface ModelAttributeService {

	@Operation(summary = "属性权限列表")
	@GetMapping(value = "/{modelId}/attributes/{attributeId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ModelAttributePermissionResponseBody attributePermissions(@PathVariable String modelId,
			@PathVariable String attributeId);

	@Operation(summary = "设置属性权限")
	@PostMapping(value = "/{modelId}/attributes/{attributeId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public void addPermission(@PathVariable String modelId, @PathVariable String attributeId,
			@RequestBody PermissionRequestBody body);

	@Operation(summary = "移除属性权限")
	@DeleteMapping("/{modelId}/attributes/{attributeId}/permissions")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removePermission(@PathVariable String modelId, @PathVariable String attributeId,
			@RequestBody PermissionRequestBody body);

}
