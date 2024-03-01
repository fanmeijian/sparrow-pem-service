package cn.sparrowmini.pem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sparrowmini.pem.model.ModelAttribute.ModelAttributePK;
import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.model.constant.PermissionTypeEnum;
import cn.sparrowmini.pem.model.relation.SysroleModel;
import cn.sparrowmini.pem.model.relation.SysroleModel.SysroleModelId;
import cn.sparrowmini.pem.model.relation.SysroleModelAttribute;
import cn.sparrowmini.pem.model.relation.SysroleModelAttribute.SysroleModelAttributeId;
import cn.sparrowmini.pem.model.relation.UserSysrole;
import cn.sparrowmini.pem.service.ModelPermissionService;
import cn.sparrowmini.pem.service.exception.DenyPermissionException;
import cn.sparrowmini.pem.service.exception.NoPermissionException;
import cn.sparrowmini.pem.service.repository.SysroleModelAttributeRepository;
import cn.sparrowmini.pem.service.repository.SysroleModelRepository;
import cn.sparrowmini.pem.service.repository.UserSysroleRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ModelPermissionServiceImpl implements ModelPermissionService {
	@Autowired
	private SysroleModelRepository sysroleModelRepository;
	@Autowired
	private SysroleModelAttributeRepository sysroleModelAttributeRepository;
	@Autowired
	private UserSysroleRepository userSysroleRepository;

	@Override
	public boolean hasPermission(String modelId, PermissionEnum permission, String username) {
		boolean allowPermissions = false;

		for (UserSysrole sysrole : this.userSysroleRepository.findByIdUsername(username)) {
			// check deny permission
			log.debug("sysrole: {}", sysrole.getSysrole());
			SysroleModel denyPermission = this.sysroleModelRepository.findById(
					new SysroleModelId(modelId, sysrole.getId().getSysroleId(), PermissionTypeEnum.DENY, permission))
					.orElse(null);

			if (denyPermission != null) {
				throw new DenyPermissionException(
						String.join(" ", "拒绝权限", modelId, permission.name(), sysrole.getSysrole().getName()));
			}
			;

			// check allow permission
			SysroleModel allowPermission = this.sysroleModelRepository.findById(
					new SysroleModelId(modelId, sysrole.getId().getSysroleId(), PermissionTypeEnum.ALLOW, permission))
					.orElse(null);
			if (allowPermission != null) {
				allowPermissions = true;
			}
			;
		}
		if (this.sysroleModelRepository.countByIdModelIdAndIdPermissionAndIdPermissionType(modelId, permission,
				PermissionTypeEnum.ALLOW) == 0) {
			allowPermissions = true;
		}

		if (!allowPermissions) {
			throw new NoPermissionException(String.join(" ", "没有权限", modelId, permission.name(), username));
		}
		return true;
	}

	@Override
	public boolean hasPermission(ModelAttributePK attributePK, PermissionEnum permission, String username) {
		boolean allowPermissions = false;

		for (UserSysrole sysrole : this.userSysroleRepository.findByIdUsername(username)) {
			// check deny permission
			log.debug("sysrole: {}", sysrole.getSysrole());
			SysroleModelAttribute denyPermission = this.sysroleModelAttributeRepository
					.findById(new SysroleModelAttributeId(attributePK, sysrole.getId().getSysroleId(),
							PermissionTypeEnum.DENY, permission))
					.orElse(null);

			if (denyPermission != null) {
				throw new DenyPermissionException(String.join(" ", "拒绝权限", attributePK.getModelId(),
						attributePK.getAttributeId(), permission.name(), sysrole.getSysrole().getName()));
			}
			;

			// check allow permission
			if (this.sysroleModelAttributeRepository.countByIdAttributeIdAndIdPermissionAndIdPermissionType(attributePK,
					permission, PermissionTypeEnum.ALLOW) > 0) {
				SysroleModelAttribute allowPermission = this.sysroleModelAttributeRepository
						.findById(new SysroleModelAttributeId(attributePK, sysrole.getId().getSysroleId(),
								PermissionTypeEnum.ALLOW, permission))
						.orElse(null);
				if (allowPermission != null) {
					allowPermissions = true;
				}
				;
			}
		}

		if (!allowPermissions) {
			throw new NoPermissionException(
					String.join(" ", "没有权限", attributePK.getAttributeId(), permission.name(), username));
		}
		return true;
	}

}
