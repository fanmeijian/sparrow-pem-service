package cn.sparrowmini.pem.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.pem.model.ModelAttribute;
import cn.sparrowmini.pem.model.ModelAttribute.ModelAttributePK;
import cn.sparrowmini.pem.model.SysroleModelAttribute;
import cn.sparrowmini.pem.model.SysroleModelAttribute.SysroleModelAttributeId;
import cn.sparrowmini.pem.model.UserModelAttribute;
import cn.sparrowmini.pem.model.UserModelAttribute.UserModelAttributeId;
import cn.sparrowmini.pem.model.token.PermissionToken;
import cn.sparrowmini.pem.model.token.SparrowPermissionToken;
import cn.sparrowmini.pem.service.ModelAttributePermissionResponseBody;
import cn.sparrowmini.pem.service.ModelAttributeService;
import cn.sparrowmini.pem.service.PermissionRequestBody;
import cn.sparrowmini.pem.service.repository.ModelAttributeRepository;
import cn.sparrowmini.pem.service.repository.SysroleModelAttributeRepository;
import cn.sparrowmini.pem.service.repository.UserModelAttributeRepository;

@Service
public class ModelAttributeServiceImpl implements ModelAttributeService {

	@Autowired
	ModelAttributeRepository modelAttributeRepository;
	// @Autowired ModelAttributePermissionService modelAttributePermissionService;
	@Autowired
	private UserModelAttributeRepository userModelAttributeRepository;
	@Autowired
	private SysroleModelAttributeRepository sysroleModelAttributeRepository;

	@Override
	public Page<ModelAttribute> all(Pageable pageable, ModelAttribute modelAttribute) {
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);
		return modelAttributeRepository.findAll(Example.of(modelAttribute, matcher), pageable);
	}

	@Override
	public ModelAttribute create(@RequestBody ModelAttribute modelAttributes) {
		return modelAttributeRepository.save(modelAttributes);
	}

	@Override
	public ModelAttribute update(ModelAttributePK attributeId, Map<String, Object> map) {
		ModelAttribute source = modelAttributeRepository.getById(attributeId);
		map.remove("id");
		PatchUpdateHelper.merge(source, map);
		return modelAttributeRepository.save(source);
	}

	@Override
	public void delete(@NotNull @RequestBody final List<ModelAttributePK> ids) {
		modelAttributeRepository.deleteByIdIn(ids);
	}

	@Override
	@Transactional
	public void addPermission(ModelAttributePK attributeId, PermissionToken permissionToken) {
		ModelAttribute modelAttribute = modelAttributeRepository.findById(attributeId).get();
		modelAttribute.setSparrowPermissionToken(new SparrowPermissionToken(permissionToken));
		modelAttributeRepository.save(modelAttribute);
	}

	@Override
	@Transactional
	public void removePermission(ModelAttributePK attributeId) {
		ModelAttribute modelAttribute = modelAttributeRepository.getById(attributeId);
		modelAttribute.setSparrowPermissionToken(null);
		modelAttributeRepository.save(modelAttribute);
	}

	@Override
	public ModelAttribute get(ModelAttributePK attributePK) {
		return modelAttributeRepository.findById(attributePK).get();
	}

	@Override
	public Page<ModelAttribute> allAttributes(String modelId, Pageable pageable, ModelAttribute modelAttribute) {
		modelAttribute.getId().setModelId(modelId);
		return this.all(pageable, modelAttribute);
	}

	@Override
	@ResponseStatus(code = HttpStatus.CREATED)
	public ModelAttribute createAttribute(ModelAttribute modelAttribute) {
		return this.create(modelAttribute);
	}

	@Override
	public ModelAttribute getAttribute(String modelId, String attributeId) {
		return this.get(new ModelAttributePK(modelId, attributeId));
	}

	@Override
	public ModelAttribute updateAttribute(String modelId, String attributeId, Map<String, Object> map) {
		return this.update(new ModelAttributePK(modelId, attributeId), map);
	}

	@Override
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void addAttributePermission(String modelId, String attributeId, PermissionRequestBody body) {
		if (body.getSysroles() != null) {
			body.getSysroles().forEach(f -> {
				this.sysroleModelAttributeRepository
						.save(new SysroleModelAttribute(new ModelAttributePK(modelId, attributeId), f.getSysroleId(),
								f.getPermissionType(), f.getPermission()));
			});
		}

		if (body.getUsernames() != null) {
			body.getUsernames().forEach(f -> {
				this.userModelAttributeRepository
						.save(new UserModelAttribute(new ModelAttributePK(modelId, attributeId), f.getUsername(),
								f.getPermissionType(), f.getPermission()));
			});
		}
	}

	@Override
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeAttributePermission(String modelId, String attributeId, PermissionRequestBody body) {
		if (body.getSysroles() != null) {
			body.getSysroles().forEach(f -> {
				this.sysroleModelAttributeRepository
						.deleteById(new SysroleModelAttributeId(new ModelAttributePK(modelId, attributeId),
								f.getSysroleId(), f.getPermissionType(), f.getPermission()));
			});
		}

		if (body.getUsernames() != null) {
			body.getUsernames().forEach(f -> {
				this.userModelAttributeRepository
						.deleteById(new UserModelAttributeId(new ModelAttributePK(modelId, attributeId),
								f.getUsername(), f.getPermissionType(), f.getPermission()));
			});
		}
	}

	@Override
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteAttribute(String modelId, List<String> attributeIds) {
		List<ModelAttributePK> modelAttributePKs = new ArrayList<ModelAttributePK>();
		attributeIds.forEach(attributeId -> {
			modelAttributePKs.add(new ModelAttributePK(modelId, attributeId));
		});
		this.delete(modelAttributePKs);
	}

	@Override
	public ModelAttributePermissionResponseBody attributePermissions(String modelId, String attributeId) {
		ModelAttributePermissionResponseBody permission = new ModelAttributePermissionResponseBody();
		permission.getSysroles().addAll(this.sysroleModelAttributeRepository
				.findByIdAttributeId(new ModelAttributePK(modelId, attributeId), Pageable.unpaged()).getContent());
		permission.getUsernames().addAll(this.userModelAttributeRepository
				.findByIdAttributeId(new ModelAttributePK(modelId, attributeId), Pageable.unpaged()).getContent());
		return permission;
	}

	// @PostMapping("/modelAttributes/permissions")
	// public void addPermission(@NotNull @RequestBody final
	// ModelAttributePermission modelAttributePermission) {
	// modelAttributePermissionService.addPermissions(modelAttributePermission);
	// }
	//
	// @DeleteMapping("/modelAttributes/permissions")
	// public void delPermission(@NotNull @RequestBody final
	// ModelAttributePermission modelAttributePermission) {
	// modelAttributePermissionService.delPermissions(modelAttributePermission);
	// }

}
