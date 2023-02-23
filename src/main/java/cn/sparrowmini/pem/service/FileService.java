package cn.sparrowmini.pem.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

import cn.sparrowmini.pem.model.SparrowFile;
import cn.sparrowmini.pem.model.constant.SysPermissionTarget;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "file", description = "文件服务")
@RequestMapping("/files")
public interface FileService {
	@PostMapping("")
	@Operation(summary = "新增功能")
	@ResponseBody
	public SparrowFile create(@RequestBody SparrowFile sparrowApi);

	@PatchMapping("/{SparrowFileId}")
	@Operation(summary = "更新功能")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = SparrowFile.class)))
	@ResponseBody
	public SparrowFile update(@PathVariable("SparrowFileId") String SparrowFileId, @RequestBody Map<String, Object> map);

	@GetMapping("/{SparrowFileId}")
	@Operation(summary = "获取功能详情")
	@ResponseBody
	public SparrowFile get(@PathVariable("SparrowFileId") String id);

	@DeleteMapping("")
	@Operation(summary = "删除功能")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@RequestBody List<String> ids);

	@GetMapping("")
	@Operation(summary = "浏览功能")
	@ResponseBody
	public Page<SparrowFile> all(@Nullable Pageable pageable, @Nullable SparrowFile SparrowFile);

	@GetMapping("/preserve")
	@Operation(summary = "浏览预置功能")
	@ResponseBody
	public List<String> preserveScopes();

	@GetMapping("/{SparrowFileId}/permissions")
	@Operation(summary = "可访问权限列表")
	@ResponseBody
	public Page<?> getPermissions(@PathVariable("SparrowFileId") String SparrowFileId, SysPermissionTarget type,
			@Nullable Pageable pageable);

	@PostMapping("/{SparrowFileId}/permissions")
	@Operation(summary = "增加授权")
	@ResponseBody
	public void addPermissions(@PathVariable("SparrowFileId") String SparrowFileId, SysPermissionTarget type,
			@RequestBody List<?> ids);

	@DeleteMapping("/{SparrowFileId}/permissions")
	@Operation(summary = "移除授权")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removePermissions(
			@PathVariable("SparrowFileId") String SparrowFileId, SysPermissionTarget type,
			@RequestBody List<?> ids);
}
