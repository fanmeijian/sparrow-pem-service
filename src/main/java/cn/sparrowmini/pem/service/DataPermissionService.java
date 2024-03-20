package cn.sparrowmini.pem.service;

import javax.annotation.Nullable;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sparrowmini.pem.model.DataPermission;
import cn.sparrowmini.pem.model.common.DataPermissionBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "data-permission", description = "数据权限服务")
@RequestMapping("/data-permissions")
public interface DataPermissionService {

	@GetMapping("")
	@Operation(summary = "浏览数据权限", operationId = "dataPermissions")
	@ResponseBody
	public Page<DataPermission> list(@ParameterObject @Nullable Pageable pageable);

	@GetMapping("/{id}")
	@Operation(summary = "数据权限详情", operationId = "dataPermission")
	@ResponseBody
	public DataPermissionBean get(@PathVariable String id);

	@PostMapping("")
	@Operation(summary = "新增数据权限", operationId = "newDataPermission")
	@ResponseBody
	public String create(@RequestBody DataPermissionBean dataPermissionBean);

	@PatchMapping("/{id}")
	@Operation(summary = "更新数据权限", operationId = "updateDataPermission")
	@ResponseBody
	public void update(@PathVariable String id, @RequestBody DataPermissionBean dataPermissionBean);

	@PostMapping("/{id}/remove")
	@Operation(summary = "更新数据权限", operationId = "removeDataPermission")
	@ResponseBody
	public void remove(@PathVariable String id, @RequestBody DataPermissionBean dataPermissionBean);
}
