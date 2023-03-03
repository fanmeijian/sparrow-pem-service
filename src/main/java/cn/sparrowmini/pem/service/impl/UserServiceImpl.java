package cn.sparrowmini.pem.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sparrowmini.pem.model.Sysrole;
import cn.sparrowmini.pem.model.relation.GroupUser;
import cn.sparrowmini.pem.model.relation.UserSysrole;
import cn.sparrowmini.pem.model.token.UserToken;
import cn.sparrowmini.pem.service.UserService;
import cn.sparrowmini.pem.service.repository.GroupUserRepository;
import cn.sparrowmini.pem.service.repository.SysroleRepository;
import cn.sparrowmini.pem.service.repository.UserMenuRepository;
import cn.sparrowmini.pem.service.repository.UserSysroleRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserMenuRepository userMenuRepository;
	@Autowired
	UserSysroleRepository userSysroleRepository;
	@Autowired
	SysroleRepository sysroleRepository;

	@Autowired
	private GroupUserRepository groupUserRepository;

	@Override
	public List<Sysrole> getSysroles(String username) {
		List<Sysrole> sysroles = new ArrayList<Sysrole>();
		userSysroleRepository.findByIdUsername(username).forEach(f -> {
			sysroles.add(sysroleRepository.findById(f.getId().getSysroleId()).get());
		});
		return sysroles;
	}

	@Override
	public List<UserSysrole> getUserSysroles(String username) {
		return userSysroleRepository.findByIdUsername(username);
	}

	@Override
	public UserToken userToken(String username) {
		UserToken userToken = new UserToken();
		userToken.setSysroles(this.userSysroleRepository.findByIdUsername(username));
		userToken.setGroups(this.groupUserRepository.findByIdUsername(username));
		return userToken;

	}

	public List<GroupUser> groups(String username) {
		return this.groupUserRepository.findByIdUsername(username);
	}

}
