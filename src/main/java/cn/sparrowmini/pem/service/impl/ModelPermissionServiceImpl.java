package cn.sparrowmini.pem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.model.constant.PermissionTypeEnum;
import cn.sparrowmini.pem.model.relation.SysroleModel;
import cn.sparrowmini.pem.model.relation.SysroleModel.SysroleModelId;
import cn.sparrowmini.pem.model.relation.UserSysrole;
import cn.sparrowmini.pem.service.ModelPermissionService;
import cn.sparrowmini.pem.service.exception.DenyPermissionException;
import cn.sparrowmini.pem.service.exception.NoPermissionException;
import cn.sparrowmini.pem.service.repository.GroupSysroleRepository;
import cn.sparrowmini.pem.service.repository.GroupUserRepository;
import cn.sparrowmini.pem.service.repository.PemGroupRelationRepository;
import cn.sparrowmini.pem.service.repository.PemGroupRepository;
import cn.sparrowmini.pem.service.repository.SysroleModelRepository;
import cn.sparrowmini.pem.service.repository.UserSysroleRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ModelPermissionServiceImpl implements ModelPermissionService {
	@Autowired
	private SysroleModelRepository sysroleModelRepository;
	@Autowired
	private UserSysroleRepository userSysroleRepository;
	@Autowired
	private GroupUserRepository groupUserRepository;
	@Autowired
	private PemGroupRepository groupRepository;
	@Autowired
	private PemGroupRelationRepository groupRelationRepository;
	@Autowired
	private GroupSysroleRepository groupSysroleRepository;

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
			if (this.sysroleModelRepository.countByIdModelIdAndIdPermissionAndIdPermissionType(modelId, permission,
					PermissionTypeEnum.ALLOW) > 0) {
				SysroleModel allowPermission = this.sysroleModelRepository.findById(new SysroleModelId(modelId,
						sysrole.getId().getSysroleId(), PermissionTypeEnum.ALLOW, permission)).orElse(null);
				if (allowPermission != null) {
					allowPermissions = true;
				}
				;
			}
		}

		if (!allowPermissions) {
			throw new NoPermissionException(String.join(" ", "没有权限", modelId, permission.name(), username));
		}
		return true;
	}

}
