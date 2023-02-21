package cn.sparrowmini.pem.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.pem.model.ModelAttribute;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "datamodel", description = "数据模型服务")
@RequestMapping("/models")
public interface ModelAttributeRestService {

	@Operation(summary = "属性列表")
	@GetMapping("/{modelId}/attributes")
	@ResponseBody
	public Page<ModelAttribute> allAttributes(@PathVariable("modelId") String modelId, @Nullable Pageable pageable,
			@Nullable ModelAttribute modelAttribute);

	@Operation(summary = "新增属性")
	@PostMapping("/attributes")
	@ResponseBody
	public ModelAttribute createAttribute(@RequestBody ModelAttribute modelAttribute);

	@Operation(summary = "属性详情")
	@GetMapping("/{modelId}/attributes/{attributeId}")
	@ResponseBody
	public ModelAttribute getAttribute(@PathVariable("modelId") String modelId,
			@PathVariable("attributeId") String attributeId);

	@Operation(summary = "更新属性")
	@PatchMapping("/{modelId}/attributes/{attributeId}")
	@ResponseBody
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = ModelAttribute.class)))
	public ModelAttribute updateAttribute(@PathVariable("modelId") String modelId,
			@PathVariable("attributeId") String attributeId, @RequestBody Map<String, Object> map);

	@Operation(summary = "属性权限列表")
	@GetMapping(value = "/{modelId}/attributes/{attributeId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ModelAttributePermissionResponseBody attributePermissions(@PathVariable String modelId, @PathVariable String attributeId);
	
	@Operation(summary = "设置属性权限")
	@PostMapping(value = "/{modelId}/attributes/{attributeId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public void addAttributePermission(@PathVariable String modelId, @PathVariable String attributeId,
			@RequestBody PermissionRequestBody body);

	@Operation(summary = "移除属性权限")
	@DeleteMapping("/{modelId}/attributes/{attributeId}/permissions")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeAttributePermission(@PathVariable String modelId, @PathVariable String attributeId,
			@RequestBody PermissionRequestBody body);

	@Operation(summary = "删除属性")
	@PutMapping("/{modelId}/attributes/delete")
	@ResponseBody
	public void deleteAttribute(@PathVariable("modelId") String modelId, @RequestBody List<String> attributeIds);
}
