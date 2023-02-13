package cn.sparrowmini.pem.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sparrowmini.pem.model.Sysrole;
import cn.sparrowmini.pem.model.UserSysrole;
import cn.sparrowmini.pem.model.UserSysrole.UserSysrolePK;
import cn.sparrowmini.pem.model.constant.PreserveSysroleEnum;
import cn.sparrowmini.pem.service.repository.SysroleRepository;
import cn.sparrowmini.pem.service.repository.UserMenuRepository;
import cn.sparrowmini.pem.service.repository.UserSysroleRepository;

@Service
public class UserService {

	@Autowired
	UserMenuRepository userMenuRepository;
	@Autowired
	UserSysroleRepository userSysroleRepository;
	@Autowired
	SysroleRepository sysroleRepository;
//  @Autowired UserModelPermissionRepository userModelPermissionRepository;
//  @Autowired UserDataPermissionRepository userDataPermissionRepository;

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

//  public void addModelPermissions(String username,Set<AbstractModelPermissionPK> modelPermissionPKs) {
//    modelPermissionPKs.forEach(f->{
//      userModelPermissionRepository.save(new UserModelPermission(new UserModelPermissionPK(f.getModelName(), f.getPermission(), f.getPermissionType(), username)));
//    });
//  }
//  
//  @Transactional
//  public void delModelPermissions(String username,Set<AbstractModelPermissionPK> modelPermissionPKs) {
//    modelPermissionPKs.forEach(f->{
//      userModelPermissionRepository.delete(new UserModelPermission(new UserModelPermissionPK(f.getModelName(), f.getPermission(), f.getPermissionType(), username)));
//    });
//  }
//  
//  public void addDataPermissions(String username, Set<AbstractDataPermissionPK> dataPermissionPKs) {
//    dataPermissionPKs.forEach(f->{
//      userDataPermissionRepository.save(new UserDataPermission(new UserDataPermissionPK(f.getModelName(), f.getPermission(), f.getPermissionType(), f.getDataId(), username)));
//    });
//  }
//  
//  @Transactional
//  public void delDataPermissions(String username, Set<AbstractDataPermissionPK> dataPermissionPKs) {
//    dataPermissionPKs.forEach(f->{
//      userDataPermissionRepository.deleteById(new UserDataPermissionPK(f.getModelName(), f.getPermission(), f.getPermissionType(), f.getDataId(), username));
//    });
//  }
//  

	public List<Sysrole> getSysroles(String username) {
		List<Sysrole> sysroles = new ArrayList<Sysrole>();
		userSysroleRepository.findByIdUsername(username).forEach(f -> {
			sysroles.add(sysroleRepository.findById(f.getId().getSysroleId()).get());
		});
		return sysroles;
	}

	public List<UserSysrole> getUserSysroles(String username) {
		return userSysroleRepository.findByIdUsername(username);
	}

	public void init(String username) {
		userSysroleRepository.save(new UserSysrole(new UserSysrolePK(username, PreserveSysroleEnum.SYSADMIN.name())));
		logger.info("Grant user {} with role", username, PreserveSysroleEnum.SYSADMIN.name());
	}

}
