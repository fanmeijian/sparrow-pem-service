package cn.sparrowmini.pem.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sparrowmini.pem.model.SysroleModel;
import cn.sparrowmini.pem.model.SysroleModel.SysroleModelId;
import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.model.constant.PermissionTypeEnum;
import cn.sparrowmini.pem.service.ModelPermissionService;
import cn.sparrowmini.pem.service.exception.DenyPermissionException;
import cn.sparrowmini.pem.service.repository.SysroleModelRepository;
import cn.sparrowmini.pem.service.repository.UserSysroleRepository;

@Service
public class ModelPermissionServiceImpl implements ModelPermissionService {
	@Autowired
	private SysroleModelRepository sysroleModelRepository; 
	@Autowired
	private UserSysroleRepository userSysroleRepository;

	@Override
	@Transactional
	public boolean hasPermission(String modelId, PermissionEnum permission, String username) {
		
		this.userSysroleRepository.findByIdUsername(username).forEach(sysrole->{
			// check deny permission
			SysroleModel sysroleModel = this.sysroleModelRepository.findById(new SysroleModelId(modelId, sysrole.getId().getSysroleId(), PermissionTypeEnum.DENY, permission)).orElse(null);
			if(sysroleModel!=null) {
				throw new DenyPermissionException(String.join(" ", "拒绝权限",modelId,permission.name(),sysrole.getSysrole().getName()));
			};
		});
//		
		return true;
	}

}
