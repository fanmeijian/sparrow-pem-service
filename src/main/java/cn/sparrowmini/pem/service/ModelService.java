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

import cn.sparrowmini.pem.model.Model;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "datamodel", description = "数据模型服务")
@RequestMapping("/models")
public interface ModelService {

	@Operation(summary = "浏览模型")
	@GetMapping("")
	@ResponseBody
	public Page<Model> models(@Nullable Pageable pageable, @Nullable Model model);

	@Operation(summary = "新增模型")
	@PostMapping("")
	@ResponseBody
	public Model create(@RequestBody Model model);

	@Operation(summary = "模型详情")
	@GetMapping("/{modelId}")
	@ResponseBody
	public Model getModel(@PathVariable("modelId") String modelId);

	@Operation(summary = "更新模型")
	@PatchMapping("/{modelId}")
	@ResponseBody
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Model.class)))
	public Model update(@PathVariable("modelId") String modelId, Map<String, Object> map);

	@Operation(summary = "删除模型")
	@PutMapping("/delete")
	@ResponseBody
	public void delete(@RequestBody List<String> ids);

	@Operation(summary = "设置模型权限")
	@PostMapping("/{modelId}/permissions")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CREATED)
	public void addPermission(@PathVariable String modelId, @RequestBody PermissionRequestBody body);
	
	@Operation(summary = "模型权限列表")
	@GetMapping(value = "/{modelId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ModelPermissionResponseBody permissions(@PathVariable String modelId);

	@Operation(summary = "删除模型权限")
	@DeleteMapping("/{modelId}/permissions")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removePermission(@PathVariable String modelId, @RequestBody PermissionRequestBody body);

	@Operation(summary = "浏览模型")
	@GetMapping("/entities")
	@ResponseBody
	public List<Model> getAllEntities();

	@Operation(summary = "初始化模型")
	@GetMapping("/init")
	@ResponseBody
	public void init();
}
