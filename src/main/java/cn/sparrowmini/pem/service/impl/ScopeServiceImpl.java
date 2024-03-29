package cn.sparrowmini.pem.service.impl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.pem.model.Scope;
import cn.sparrowmini.pem.model.constant.SysPermissionTarget;
import cn.sparrowmini.pem.model.relation.SysroleScope;
import cn.sparrowmini.pem.model.relation.SysroleScope.SysroleScopePK;
import cn.sparrowmini.pem.model.relation.UserScope;
import cn.sparrowmini.pem.model.relation.UserScope.UserScopePK;
import cn.sparrowmini.pem.service.ScopePermission;
import cn.sparrowmini.pem.service.ScopeService;
import cn.sparrowmini.pem.service.repository.ScopeRepository;
import cn.sparrowmini.pem.service.repository.SysroleScopeRepository;
import cn.sparrowmini.pem.service.repository.UserScopeRepository;
import cn.sparrowmini.pem.service.scope.PreserveScope;
import cn.sparrowmini.pem.service.scope.ScopeScope;
import cn.sparrowmini.pem.service.scope.ScopeScope.ScopePemScope;

@Service
public class ScopeServiceImpl extends AbstractPreserveScope implements ScopeService {

	@Autowired
	ScopeRepository scopeRepository;
	@Autowired
	SysroleScopeRepository sysroleScopeRepository;
	@Autowired
	UserScopeRepository userScopeRepository;

	@Autowired
	PreserveScope[] preserveScopes;

	@Override
	@ResponseStatus(value = HttpStatus.CREATED)
	@ScopePermission(scope = "admin:scope:create", name = "创建scope")
//	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_CREATE + "') or hasRole('ROLE_" + ROLE_SYSADMIN + "')")
	public Scope create(Scope scope) {
		return scopeRepository.save(scope);
	}

	@Override
	@ScopePermission(name = "", scope = ScopeScope.UPDATE)
	public Scope update(String scopeId, Map<String, Object> map) {
		Scope scope = scopeRepository.findById(scopeId).get();
		PatchUpdateHelper.merge(scope, map);
		return scopeRepository.save(scope);
	}

	@Override
	@ScopePermission(name = "", scope = ScopeScope.READ)
	public Scope get(String id) {
		return scopeRepository.findById(id).get();
	}

	@Override
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ScopePermission(name = "", scope = ScopeScope.DELETE)
	public void delete(List<String> ids) {
		scopeRepository.deleteAllByIdInBatch(ids);
	}

	@Override
	@ScopePermission(name = "", scope = ScopeScope.LIST)
	public Page<Scope> all(Pageable pageable, Scope scope) {
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);
		return scopeRepository.findAll(Example.of(scope, matcher), pageable);
	}

	@Override
	@ScopePermission(name = "", scope = ScopePemScope.LIST)
	public Page<?> getPermissions(String scopeId, SysPermissionTarget type, Pageable pageable) {
		Page<?> page = null;
		switch (type) {
		case SYSROLE:
			page = sysroleScopeRepository.findByIdScopeId(scopeId, pageable);
			break;
		case USER:
			page = userScopeRepository.findByIdScopeId(scopeId, pageable);
			break;
		default:
			break;
		}
		return page;
	}

	@Override
	@Transactional
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@ScopePermission(name = "", scope = ScopePemScope.ADD)
	public void addPermissions(String scopeId, SysPermissionTarget type, List<String> ids) {
		switch (type) {
		case SYSROLE:
			ids.forEach(f -> {
				sysroleScopeRepository.save(new SysroleScope(f.toString(), scopeId));
			});
			break;
		case USER:
			ids.forEach(f -> {
				userScopeRepository.save(new UserScope(f.toString(), scopeId));
			});
			break;
		default:
			break;
		}

	}

	@Override
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Transactional
	@ScopePermission(name = "", scope = ScopePemScope.REMOVE)
	public void removePermissions(String scopeId, SysPermissionTarget type, List<String> ids) {
		switch (type) {
		case SYSROLE:
			ids.forEach(f -> {
				sysroleScopeRepository.deleteById(new SysroleScopePK(f.toString(), scopeId));
			});
			break;
		case USER:
			ids.forEach(f -> {
				userScopeRepository.deleteById(new UserScopePK(f.toString(), scopeId));
			});
			break;
		default:
			break;
		}

	}

	@Override
	@PreAuthorize("hasRole('ROLE_" + ROLE_SYSADMIN + "')")
	public List<String> preserveScopes() {
		List<String> scopes = this.getScopes();
		for (PreserveScope preserveScope : preserveScopes) {
			scopes.addAll(preserveScope.getScopes());
		}
		return scopes;
	}

}
