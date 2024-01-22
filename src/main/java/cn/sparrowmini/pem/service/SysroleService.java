package cn.sparrowmini.pem.service;

import java.util.List;
import java.util.Map;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.pem.model.Sysrole;
import cn.sparrowmini.pem.model.relation.UserSysrole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "sysrole", description = "角色服务")
@RequestMapping("/sysroles")
public interface SysroleService {

	@Operation(summary = "角色列表", operationId = "sysroles")
	@GetMapping("")
	@ResponseBody
	public Page<Sysrole> all(@Nullable @ParameterObject Pageable pageable, @Nullable Sysrole sysrole);

	@Operation(summary = "新增角色", operationId = "newSysrole")
	@PostMapping("")
	@ResponseBody
	public Sysrole create(@RequestBody Sysrole sysrole);

	@Operation(summary = "角色详情", operationId = "sysrole")
	@GetMapping("/{sysroleId}")
	@ResponseBody
	public Sysrole get(@PathVariable("sysroleId") String sysroleId);

	@Operation(summary = "更新角色", operationId = "updateSysrole")
	@PatchMapping("/{sysroleId}")
	@ResponseBody
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Sysrole.class)))
	public Sysrole update(@PathVariable("sysroleId") String sysroleId, @RequestBody Map<String, Object> map);

	@Operation(summary = "删除角色", operationId = "deleteSysroles")
	@PostMapping("/delete")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@RequestBody List<String> ids);

	@Operation(summary = "授权用户", operationId = "addSysroleUsers")
	@PostMapping("/{sysroleId}/users")
	@ResponseBody
	public void addPermissions(@PathVariable("sysroleId") String sysroleId, @RequestBody List<String> usernames);

	@Operation(summary = "取消用户授权", operationId = "removeSysroleUsers")
	@PostMapping("/{sysroleId}/users/remove")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removePermissions(@PathVariable("sysroleId") String sysroleId, @RequestBody List<String> usernames);

	@Operation(summary = "角色用户列表", operationId = "sysroleUsers")
	@GetMapping("/{sysroleId}/users")
	@ResponseBody
	public Page<UserSysrole> getUsers(@PathVariable String sysroleId, @Nullable @ParameterObject Pageable pageable);

}
