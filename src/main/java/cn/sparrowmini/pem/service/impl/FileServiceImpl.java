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

import cn.sparrowmini.pem.model.SparrowFile;
import cn.sparrowmini.pem.model.SysroleFile;
import cn.sparrowmini.pem.model.SysroleFile.SysroleFilePK;
import cn.sparrowmini.pem.model.UserFile;
import cn.sparrowmini.pem.model.UserFile.UserFilePK;
import cn.sparrowmini.pem.model.constant.SysPermissionTarget;
import cn.sparrowmini.pem.service.FileService;
import cn.sparrowmini.pem.service.repository.FileRepository;
import cn.sparrowmini.pem.service.repository.SysroleFileRepository;
import cn.sparrowmini.pem.service.repository.UserFileReposiroty;
import cn.sparrowmini.pem.service.scope.FileScope;

@Service
public class FileServiceImpl extends AbstractPreserveScope implements FileService, FileScope {

	@Autowired
	FileRepository fileRepository;
	@Autowired
	SysroleFileRepository sysroleScopeRepository;
	@Autowired
	UserFileReposiroty userScopeRepository;

//	@Autowired
//	PreserveScope[] preserveScopes;

	@Override
	@ResponseStatus(value = HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_CREATE + "') or hasRole('ROLE_" + ROLE_SYSADMIN + "')")
	public SparrowFile create(SparrowFile scope) {
		return fileRepository.save(scope);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_UPDATE + "') or hasRole('ROLE_" + ROLE_SYSADMIN + "')")
	public SparrowFile update(String fileId, Map<String, Object> map) {
		SparrowFile file = fileRepository.findById(fileId).get();
		PatchUpdateHelper.merge(file, map);
		return fileRepository.save(file);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_READ + "') or hasRole('ROLE_" + ROLE_SYSADMIN + "')")
	public SparrowFile get(String id) {
		return fileRepository.findById(id).get();
	}

	@Override
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_DELETE + "') or hasRole('ROLE_" + ROLE_SUPER_SYSADMIN + "')")
	public void delete(List<String> ids) {
		fileRepository.deleteAllByIdInBatch(ids);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_LIST + "') or hasRole('ROLE_" + ROLE_SYSADMIN + "')")
	public Page<SparrowFile> all(Pageable pageable, SparrowFile scope) {
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);
		return fileRepository.findAll(Example.of(scope, matcher), pageable);
	}

	@Override
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_PEM_LIST + "') or hasRole('ROLE_" + ROLE_SYSADMIN + "')")
	public Page<?> getPermissions(String fileId, SysPermissionTarget type, Pageable pageable) {
		Page<?> page = null;
		switch (type) {
		case SYSROLE:
			page = sysroleScopeRepository.findByIdFileId(fileId, pageable);
			break;
		case USER:
			page = userScopeRepository.findByIdFileId(fileId, pageable);
			break;
		default:
			break;
		}
		return page;
	}

	@Override
	@Transactional
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_PEM_ADD + "') or hasRole('ROLE_" + ROLE_SYSADMIN + "')")
	public void addPermissions(String fileId, SysPermissionTarget type, List<?> ids) {
		switch (type) {
		case SYSROLE:
			ids.forEach(f -> {
				sysroleScopeRepository.save(new SysroleFile(f.toString(), fileId));
			});
			break;
		case USER:
			ids.forEach(f -> {
				userScopeRepository.save(new UserFile(f.toString(), fileId));
			});
			break;
		default:
			break;
		}

	}

	@Override
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Transactional
	@PreAuthorize("hasAuthority('SCOPE_" + SCOPE_ADMIN_PEM_REMOVE + "') or hasRole('ROLE_" + ROLE_SUPER_SYSADMIN + "')")
	public void removePermissions(String fileId, SysPermissionTarget type, List<?> ids) {
		switch (type) {
		case SYSROLE:
			ids.forEach(f -> {
				sysroleScopeRepository.deleteById(new SysroleFilePK(f.toString(), fileId));
			});
			break;
		case USER:
			ids.forEach(f -> {
				userScopeRepository.deleteById(new UserFilePK(f.toString(), fileId));
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
//		for (PreserveScope preserveScope : preserveScopes) {
//			scopes.addAll(preserveScope.getScopes());
//		}
		return scopes;
	}

}
