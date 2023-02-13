package cn.sparrowmini.pem.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.pem.model.Menu;
import cn.sparrowmini.pem.model.SparrowTree;
import cn.sparrowmini.pem.model.Sysrole;
import cn.sparrowmini.pem.model.SysroleMenu;
import cn.sparrowmini.pem.model.SysroleMenu.SysroleMenuPK;
import cn.sparrowmini.pem.model.UserMenu;
import cn.sparrowmini.pem.model.UserMenu.UserMenuPK;
import cn.sparrowmini.pem.model.common.MenuPermission;
import cn.sparrowmini.pem.model.constant.MenuTreeTypeEnum;
import cn.sparrowmini.pem.model.constant.SysPermissionTarget;
import cn.sparrowmini.pem.service.MenuScope;
import cn.sparrowmini.pem.service.MenuService;
import cn.sparrowmini.pem.service.SortService;
import cn.sparrowmini.pem.service.SysroleService;
import cn.sparrowmini.pem.service.TreeService;
import cn.sparrowmini.pem.service.repository.MenuRepository;
import cn.sparrowmini.pem.service.repository.SysroleMenuRepository;
import cn.sparrowmini.pem.service.repository.UserMenuRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MenuServiceImpl extends AbstractPreserveScope implements MenuService,MenuScope {

	@Autowired
	MenuRepository menuRepository;

	@Autowired
	UserMenuRepository userMenuRepository;

	@Autowired
	SysroleMenuRepository sysroleMenuRepository;

	@Autowired
	SortService<Menu, String> sparrowSortedService;

	@Autowired
	TreeService<Menu, String> sparrowTreeService;

	@Autowired
	SysroleService sysroleService;

	@Autowired
	UserService userService;

	public SparrowTree<Menu, String> getTreeByParentId(String parentId) {
		Menu menu = menuRepository.findById(parentId).orElse(new Menu(null, null));
		SparrowTree<Menu, String> menuTree = new SparrowTree<Menu, String>(menu, menu.getId(), menu.getNextNodeId(),
				menu.getNextNodeId());
		buildTree(menuTree);
		sparrowTreeService.sort(menuTree);
		return menuTree;
	}

	public void buildTree(SparrowTree<Menu, String> menuTree) {
		List<Menu> menus = menuRepository.findByParentId(menuTree.getMe().getId());
		// sort the menus
//        sparrowSortedService.sort(menus);

		for (Menu menu : menus) {
			SparrowTree<Menu, String> leaf = new SparrowTree<Menu, String>(menu, menu.getId(), menu.getNextNodeId(),
					menu.getNextNodeId());
			menuTree.getChildren().add(leaf);
			buildTree(leaf);
		}
	}

	public void buildTree(SparrowTree<Menu, String> menuTree, List<Menu> userMenus) {
		List<Menu> menus = menuRepository.findByParentId(menuTree.getMe().getId());
		// sort the menus
//        sparrowSortedService.sort(menus);

		for (Menu menu : menus) {
			SparrowTree<Menu, String> leaf = new SparrowTree<Menu, String>(menu, menu.getId(), menu.getNextNodeId(),
					menu.getNextNodeId());
			if (userMenus.stream().anyMatch(p -> p.getId().equals(menu.getId())))
				menuTree.getChildren().add(leaf);
			buildTree(leaf, userMenus);
		}
	}

	public SparrowTree<Menu, String> getTreeByUsername(String username) {
		SparrowTree<Menu, String> menuTree = new SparrowTree<Menu, String>(new Menu());
		buildUserTree(username, menuTree);
		sparrowTreeService.sort(menuTree);
		return menuTree;
	}

	public SparrowTree<Menu, String> getTreeBySysroleId(String sysroleId) {
		SparrowTree<Menu, String> menuTree = new SparrowTree<Menu, String>(new Menu());
		buildSysroleTree(sysroleId, menuTree);
		return menuTree;
	}

	// 构建角色菜单的大树，含直接父级的菜单
	public void buildSysroleTree(String sysroleId, SparrowTree<Menu, String> menuTree) {
		List<Menu> menusSet = new ArrayList<Menu>();
		getSysroleMenusWithParentAndChildren(sysroleId, menusSet);

		// 构建上级菜单树
		List<Menu> menus = menuRepository.findByParentId(menuTree.getMe().getId());
		for (Menu menu : menus) {
			SparrowTree<Menu, String> leaf = new SparrowTree<Menu, String>(menu, menu.getId(), menu.getNextNodeId(),
					menu.getNextNodeId());
			if (menusSet.stream().anyMatch(p -> p.getId().equals(menu.getId())))
				menuTree.getChildren().add(leaf);
			buildTree(leaf, menusSet);
		}
	}

	// 构建用户菜单的大树，含直接父级的菜单
	public void buildUserTree(String username, SparrowTree<Menu, String> menuTree) {
		List<Menu> menusSet = new ArrayList<Menu>();
		getUserMenusWithParentAndChildren(username, menusSet);
		// 整合用户拥有角色的菜单
		userService.getUserSysroles(username).forEach(sysrole -> {
			getSysroleMenusWithParentAndChildren(sysrole.getId().getSysroleId(), menusSet);
		});

		// 构建用户的菜单树
		List<Menu> menus = menuRepository.findByParentId(menuTree.getMe().getId());
		for (Menu menu : menus) {
			SparrowTree<Menu, String> leaf = new SparrowTree<Menu, String>(menu, menu.getId(), menu.getNextNodeId(),
					menu.getNextNodeId());
			if (menusSet.stream().anyMatch(p -> p.getId().equals(menu.getId())))
				menuTree.getChildren().add(leaf);
			buildTree(leaf, menusSet);
		}
	}

	// 获取用户菜单的亲戚集合（不含兄弟姐妹节点）
	public void getUserMenusWithParentAndChildren(String username, List<Menu> menus) {
		// menus.addAll(userRepository.findById(username).get().getMenus()) ;
		userMenuRepository.findByIdUsername(username).forEach(f -> {
			menus.add(menuRepository.findById(f.getId().getMenuId()).get());
			buildParents(menuRepository.findById(f.getId().getMenuId()).get().getParentId(), menus);
			if (f.getIncludeSubMenu()) {
				buildChildren(f.getId().getMenuId(), menus);
			}
		});
	}

	// 获取角色菜单的亲戚集合（不含兄弟姐妹节点）
	public void getSysroleMenusWithParentAndChildren(String sysroleId, List<Menu> menus) {
//      menus.addAll(userRepository.findById(username).get().getMenus());
		sysroleMenuRepository.findByIdSysroleId(sysroleId).forEach(f -> {
			menus.add(menuRepository.findById(f.getId().getMenuId()).get());
			buildParents(menuRepository.findById(f.getId().getMenuId()).get().getParentId(), menus);
			if (f.getIncludeSubMenu()) {
				// 当勾选了包含子菜单后，则取所有的子菜单，新增的子菜单也自动授权了。如果没勾选，则后面新加的权限不会出现，需要手工授权
				buildChildren(f.getId().getMenuId(), menus);
			}
		});
	}

	// 获取到所有的祖先集合
	public void buildParents(String parentId, List<Menu> menus) {

		if (parentId != null) {
			Menu parent = menuRepository.findById(parentId).orElse(null);
			menus.add(parent);
			buildParents(parent.getParentId(), menus);
		}
	}

	// 获取到所有的子孙集合
	public void buildChildren(String menuId, List<Menu> menus) {
		menuRepository.findByParentId(menuId).forEach(f -> {
			menus.add(f);
			buildChildren(f.getId(), menus);
		});
	}

	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@Override
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_PEM_ADD + "') or hasRole('ROLE_" + ROLE_ADMIN + "')")
	public void addPermission(MenuPermission menuPermission) {
		if (menuPermission.getUserMenuPKs() != null) {
			menuPermission.getUserMenuPKs().forEach(f -> {
				userMenuRepository.save(new UserMenu(f));
			});
		}

		if (menuPermission.getSysroleMenuPKs() != null) {
			menuPermission.getSysroleMenuPKs().forEach(f -> {
				sysroleMenuRepository.save(new SysroleMenu(f));
			});
		}
	}

	@Override
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_PEM_REMOVE + "') or hasRole('ROLE_" + ROLE_SUPER_ADMIN + "')")
	public void delPermission(MenuPermission menuPermission) {
		if (menuPermission.getUserMenuPKs() != null) {
			userMenuRepository.deleteByIdIn(menuPermission.getUserMenuPKs());
		}

		if (menuPermission.getSysroleMenuPKs() != null) {
			sysroleMenuRepository.deleteByIdIn(menuPermission.getSysroleMenuPKs());
		}
	}

	public void setPosition(Menu menu) {
		sparrowSortedService.saveSort(menuRepository, menu);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_PEM_ADD + "') or hasRole('ROLE_" + ROLE_ADMIN + "')")
	public List<Sysrole> getSysroles(String menuId) {
		List<Sysrole> sysroles = new ArrayList<>();
		sysroleMenuRepository.findByIdMenuId(menuId).forEach(f -> {
			sysroles.add(sysroleService.get(f.getId().getSysroleId()));
		});
		return sysroles;
	}

	@Override
	public SparrowTree<Menu, String> getMyTree(Principal principal) {
		log.debug(principal.getName());
		return getTreeByUsername(principal.getName());
	}

	@Override
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_DELETE + "') or hasRole('ROLE_" + ROLE_SUPER_SYSADMIN + "')")
	public void delete(@NotNull String[] ids) {
		menuRepository.deleteByIdIn(ids);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_LIST + "') or hasRole('ROLE_" + ROLE_ADMIN + "')")
	public Page<Menu> all(Pageable pageable, Menu menu) {
		log.debug("menu : {}", menu);
		return menuRepository.search(menu, pageable);
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_SORT + "') or hasRole('ROLE_" + ROLE_ADMIN + "')")
	public void setPosition(String menuId, String prevId, String nextId) {
		Menu menu = menuRepository.getById(menuId);
		menu.setPreviousNodeId(prevId);
		menu.setNextNodeId(nextId);
		setPosition(menu);
	}

	@Override
	@ResponseStatus(code = HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_CREATE + "') or hasRole('ROLE_" + ROLE_SYSADMIN + "')")
	public Menu save(Menu menu) {
		return menuRepository.save(menu);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_UPDATE + "') or hasRole('ROLE_" + ROLE_SYSADMIN + "')")
	public Menu update(String menuId, Map<String, Object> map) {
		Menu source = menuRepository.getById(menuId);
		PatchUpdateHelper.merge(source, map);
		return menuRepository.save(source);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_PEM_LIST + "') or hasRole('ROLE_" + ROLE_ADMIN + "')")
	public List<String> getUsers(String menuId) {
		List<String> users = new ArrayList<>();
		userMenuRepository.findByIdMenuId(menuId).forEach(f -> {
			users.add(f.getId().getUsername());
		});
		return users;
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_TREE + "') or hasRole('ROLE_" + ROLE_ADMIN + "')")
	public SparrowTree<Menu, String> getTree(MenuTreeTypeEnum type, String sysroleId, String username, String parentId,
			Principal principal) {
		switch (type) {
		case MY:
			return this.getMyTree(principal);
		case MENU:
			return this.getTreeByParentId(parentId);
		case USER:
			return this.getTreeByUsername(username);
		case SYSROLE:
			return this.getTreeBySysroleId(sysroleId);
		default:
			break;
		}
		return null;
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_PEM_LIST + "') or hasRole('ROLE_" + ROLE_ADMIN + "')")
	public List<?> getPermissions(String menuId, SysPermissionTarget type) {
		switch (type) {
		case SYSROLE:
			return this.getSysroles(menuId);
		case USER:
			return this.getUsers(menuId);
		default:
			break;
		}
		return null;
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_PEM_ADD + "') or hasRole('ROLE_" + ROLE_ADMIN + "')")
	public void addPermission(String menuId, SysPermissionTarget type, @NotNull List<String> permissions) {
		permissions.forEach(f->{
			switch (type) {
			case USER:
				userMenuRepository.save(new UserMenu(menuId, (String) f));	
				break;
			case SYSROLE:
				sysroleMenuRepository.save(new SysroleMenu(menuId, (String)f));
			default:
				break;
			}
			
		});
		
		
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_PEM_REMOVE + "') or hasRole('ROLE_" + ROLE_SUPER_ADMIN + "')")
	public void delPermission(String menuId, SysPermissionTarget type, @NotNull List<String> permissions) {
		permissions.forEach(f->{
			switch (type) {
			case USER:
				userMenuRepository.deleteById(new UserMenuPK((String)f,menuId));	
				break;
			case SYSROLE:
				sysroleMenuRepository.deleteById(new SysroleMenuPK((String)f,menuId));
			default:
				break;
			}
			
		});
	}

	@Override
	public Menu get(String menuId) {
		return menuRepository.findById(menuId).orElse(null);
	}

}
