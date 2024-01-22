package cn.sparrowmini.pem.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.common.SparrowTree;
import cn.sparrowmini.pem.model.Menu;
import cn.sparrowmini.pem.model.Sysrole;
import cn.sparrowmini.pem.model.common.MenuPermission;
import cn.sparrowmini.pem.model.constant.MenuTreeTypeEnum;
import cn.sparrowmini.pem.model.constant.SysPermissionTarget;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "menu", description = "菜单服务")
@RequestMapping("/menus")
public interface MenuService {

	@PostMapping("")
	@Operation(summary = "新增菜单", operationId = "newMenu")
	@ResponseBody
	public Menu save(@RequestBody Menu menu);

	@GetMapping("/{menuId}")
	@Operation(summary = "菜单详情", operationId = "menu")
	@ResponseBody
	public Menu get(@PathVariable("menuId") String menuId);

	@Operation(summary = "菜单树", operationId = "menuTree")
	@ResponseBody
	@GetMapping("/tree")
	public SparrowTree<Menu, String> getTree(MenuTreeTypeEnum type, @Nullable String sysroleId,
			@Nullable String username, @Nullable String parentId, Principal principal);

	@GetMapping("/{menuId}/permissions")
	@Operation(summary = "浏览授权", operationId = "menuPermissions")
	@ResponseBody
	public List<?> getPermissions(@PathVariable("menuId") String menuId, SysPermissionTarget type);

	@PostMapping("/{menuId}/permissions")
	@Operation(summary = "设置菜单权限",operationId = "addMenuPermissions")
	@ResponseBody
	public void addPermission(@PathVariable("menuId") String menuId, SysPermissionTarget type,
			@NotNull @RequestBody List<String> permissions);

	@PostMapping(value = "/{menuId}/permissions/remove", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "取消菜单权限", operationId = "removeMenuPermissions")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delPermission(@PathVariable String menuId, SysPermissionTarget type,
			@NotNull @RequestBody List<String> permissions);

	@PostMapping("/delete")
	@Operation(summary = "删除菜单", operationId = "deleteMenus")
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@NotNull @RequestBody final String[] ids);

	@PatchMapping(path = "/{menuId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "更新菜单", operationId = "updateMenu")
	@ResponseBody
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Menu.class)))
	public Menu update(@PathVariable("menuId") String menuId, @RequestBody Map<String, Object> map);

	@GetMapping("")
	@Operation(summary = "浏览菜单", operationId = "menus")
	@ResponseBody
	public Page<Menu> all(@Nullable @ParameterObject Pageable pageable, @Nullable Menu menu);

	@PatchMapping("/{menuId}/sort")
	@Operation(summary = "菜单排序", operationId = "sortMenu")
	@ResponseBody
	public void setPosition(@PathVariable("menuId") String menuId, @RequestParam("prevId") String prevId,
			@RequestParam("nextId") String nextId);

	public SparrowTree<Menu, String> getTreeByParentId(@Nullable @RequestParam("parentId") String parentId);

	public SparrowTree<Menu, String> getTreeByUsername(@NotNull @RequestParam("username") String username);

	public SparrowTree<Menu, String> getTreeBySysroleId(@NotNull @RequestParam("sysroleId") String sysroleId);

	@GetMapping("/my")
	@Operation(summary = "我的菜单", operationId = "myMenu")
	@ResponseBody
	public SparrowTree<Menu, String> getMyTree(Principal principal);

	public List<Sysrole> getSysroles(@PathVariable("menuId") String menuId);

	public List<String> getUsers(@PathVariable("menuId") String menuId);

	public void addPermission(@NotNull @RequestBody final MenuPermission menuPermission);

	public void delPermission(@NotNull @RequestBody final MenuPermission menuPermission);
}
