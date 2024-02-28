package cn.sparrowmini.pem.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.pem.model.Sysrole;
import cn.sparrowmini.pem.model.constant.PreserveSysroleEnum;
import cn.sparrowmini.pem.model.relation.SysroleMenu;
import cn.sparrowmini.pem.model.relation.SysroleMenu.SysroleMenuPK;
import cn.sparrowmini.pem.model.relation.UserSysrole;
import cn.sparrowmini.pem.model.relation.UserSysrole.UserSysrolePK;
import cn.sparrowmini.pem.service.ScopePermission;
import cn.sparrowmini.pem.service.SysroleService;
import cn.sparrowmini.pem.service.repository.SysroleMenuRepository;
import cn.sparrowmini.pem.service.repository.SysroleRepository;
import cn.sparrowmini.pem.service.repository.UserSysroleRepository;
import cn.sparrowmini.pem.service.scope.SysroleScope;
import cn.sparrowmini.pem.service.scope.SysroleScope.SysroleUserScope;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统的角色管理，仅允许管理员和超级管理员处理
 * 
 * @author fansword
 *
 */

@Slf4j
@Service
public class SysroleServiceImpl extends AbstractPreserveScope implements SysroleService {
	@Autowired
	SysroleRepository sysroleRepository;

	@Autowired
	UserSysroleRepository userSysroleRepository;
	@Autowired
	SysroleMenuRepository sysroleMenuRepository;

	public void delMenus(String sysroleId, List<String> menuIds) {
		sysroleMenuRepository.deleteByIdSysroleIdAndIdMenuIdIn(sysroleId, menuIds);
	}

	@Override
	@ScopePermission(name = "", scope = SysroleUserScope.LIST)
	public Page<UserSysrole> getUsers(String sysroleId, Pageable pageable) {
		return userSysroleRepository.findByIdSysroleId(sysroleId, pageable);
	}

	@ScopePermission(scope = "admin:sysrole:menu:add", name = "添加角色菜单")
	public void addMenus(String sysroleId, List<String> menuIds) {
		Set<SysroleMenu> sysroleMenus = new HashSet<SysroleMenu>();
		menuIds.forEach(f -> {
			sysroleMenus.add(new SysroleMenu(new SysroleMenuPK(sysroleId, f)));
		});
		sysroleMenuRepository.saveAll(sysroleMenus);
	}

	public void init() {
		sysroleRepository.save(new Sysrole("超级管理员", PreserveSysroleEnum.SYSADMIN.name()));
		log.info("Create sysrole {}", PreserveSysroleEnum.SYSADMIN.name());

		sysroleRepository.save(new Sysrole("系统管理员", PreserveSysroleEnum.ADMIN.name()));
		log.info("Create sysrole {}", PreserveSysroleEnum.ADMIN.name());

		userSysroleRepository.save(new UserSysrole(new UserSysrolePK("ROOT",
				sysroleRepository.findByCode(PreserveSysroleEnum.SYSADMIN.name()).get(0).getId())));
		log.info("Grant user {} sysrole SYSADMIN", PreserveSysroleEnum.ADMIN.name());

	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(name = "", scope = SysroleUserScope.ADD)
	public void addPermissions(String sysroleId, List<String> usernames) {
		usernames.forEach(f -> {
			userSysroleRepository.save(new UserSysrole(sysroleId, f));
		});
	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(name = "", scope = SysroleUserScope.REMOVE)
	public void removePermissions(String sysroleId, List<String> usernames) {
		usernames.forEach(f -> {
			userSysroleRepository.deleteById(new UserSysrolePK(f, sysroleId));
		});

	}

	@Override
	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ScopePermission(name = "", scope = SysroleScope.DELETE)
	public void delete(List<String> ids) {
		sysroleRepository.deleteAllByIdInBatch(ids);
	}

	@Override
	@ScopePermission(name = "", scope = SysroleScope.LIST)
	public Page<Sysrole> all(Pageable pageable, Sysrole sysrole) {
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);
		return sysroleRepository.findAll(Example.of(sysrole, matcher), pageable);
	}

	@Override
	@ResponseStatus(code = HttpStatus.CREATED)
	@ScopePermission(name = "", scope = SysroleScope.CREATE)
	public Sysrole create(Sysrole sysrole) {
		return sysroleRepository.save(sysrole);
	}

	@Override
	@ScopePermission(name = "", scope = SysroleScope.UPDATE)
	public Sysrole update(String sysroleId, Map<String, Object> map) {
		Sysrole source = sysroleRepository.getReferenceById(sysroleId);
		PatchUpdateHelper.merge(source, map);
		return sysroleRepository.save(source);
	}

	@Override
	@ScopePermission(name = "", scope = SysroleScope.READ)
	public Sysrole get(String sysroleId) {
		return sysroleRepository.findById(sysroleId).get();
	}
}
