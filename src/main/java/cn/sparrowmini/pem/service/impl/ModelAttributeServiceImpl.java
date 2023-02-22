package cn.sparrowmini.pem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.pem.model.ModelAttribute.ModelAttributePK;
import cn.sparrowmini.pem.model.relation.SysroleModelAttribute;
import cn.sparrowmini.pem.model.relation.UserModelAttribute;
import cn.sparrowmini.pem.model.relation.SysroleModelAttribute.SysroleModelAttributeId;
import cn.sparrowmini.pem.model.relation.UserModelAttribute.UserModelAttributeId;
import cn.sparrowmini.pem.service.ModelAttributePermissionResponseBody;
import cn.sparrowmini.pem.service.ModelAttributeService;
import cn.sparrowmini.pem.service.PermissionRequestBody;
import cn.sparrowmini.pem.service.repository.SysroleModelAttributeRepository;
import cn.sparrowmini.pem.service.repository.UserModelAttributeRepository;

@Service
public class ModelAttributeServiceImpl implements ModelAttributeService {

	@Autowired
	private UserModelAttributeRepository userModelAttributeRepository;
	@Autowired
	private SysroleModelAttributeRepository sysroleModelAttributeRepository;

	@Override
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void addPermission(String modelId, String attributeId, PermissionRequestBody body) {
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
	public void removePermission(String modelId, String attributeId, PermissionRequestBody body) {
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
	public ModelAttributePermissionResponseBody attributePermissions(String modelId, String attributeId) {
		ModelAttributePermissionResponseBody permission = new ModelAttributePermissionResponseBody();
		permission.getSysroles().addAll(this.sysroleModelAttributeRepository
				.findByIdAttributeId(new ModelAttributePK(modelId, attributeId), Pageable.unpaged()).getContent());
		permission.getUsernames().addAll(this.userModelAttributeRepository
				.findByIdAttributeId(new ModelAttributePK(modelId, attributeId), Pageable.unpaged()).getContent());
		return permission;
	}

}
