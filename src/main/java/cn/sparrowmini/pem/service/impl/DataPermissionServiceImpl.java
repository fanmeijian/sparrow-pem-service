package cn.sparrowmini.pem.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.sparrowmini.pem.model.DataPermission;
import cn.sparrowmini.pem.model.DataPermissionGroup;
import cn.sparrowmini.pem.model.DataPermissionGroup.DataPermissionGroupId;
import cn.sparrowmini.pem.model.DataPermissionSysrole;
import cn.sparrowmini.pem.model.DataPermissionSysrole.DataPermissionSysroleId;
import cn.sparrowmini.pem.model.DataPermissionUsername;
import cn.sparrowmini.pem.model.DataPermissionUsername.DataPermissionUsernameId;
import cn.sparrowmini.pem.model.common.DataPermissionBean;
import cn.sparrowmini.pem.service.DataPermissionService;
import cn.sparrowmini.pem.service.repository.DataPermissionGroupRepository;
import cn.sparrowmini.pem.service.repository.DataPermissionRepository;
import cn.sparrowmini.pem.service.repository.DataPermissionSysroleRepository;
import cn.sparrowmini.pem.service.repository.DataPermissionUsernameRepository;

@Service
public class DataPermissionServiceImpl implements DataPermissionService {
	@Autowired
	private DataPermissionRepository dataPermissionRepository;
	@Autowired
	private DataPermissionSysroleRepository dataPermissionSysroleRepository;
	@Autowired
	private DataPermissionUsernameRepository dataPermissionUsernameRepository;
	@Autowired
	private DataPermissionGroupRepository dataPermissionGroupRepository;

	@Transactional
	@Override
	public String create(DataPermissionBean dataPermissionBean) {
		DataPermission dataPermission = this.dataPermissionRepository.save(new DataPermission());

		List<DataPermissionSysrole> sysroles = dataPermissionBean.getSysroleIds().stream()
				.map(m -> new DataPermissionSysrole(dataPermission.getId(), m.getSysroleId(), m.getPermissionType(),
						m.getPermission()))
				.collect(Collectors.toList());

		List<DataPermissionUsername> usernames = dataPermissionBean.getUsernames().stream()
				.map(m -> new DataPermissionUsername(dataPermission.getId(), m.getUsername(), m.getPermissionType(),
						m.getPermission()))
				.collect(Collectors.toList());

		List<DataPermissionGroup> groups = dataPermissionBean.getGroups().stream()
				.map(m -> new DataPermissionGroup(dataPermission.getId(), m.getGroupId(), m.getPermissionType(),
						m.getPermission()))
				.collect(Collectors.toList());

		this.dataPermissionSysroleRepository.saveAll(sysroles);
		this.dataPermissionUsernameRepository.saveAll(usernames);
		this.dataPermissionGroupRepository.saveAll(groups);

		return Collections.singletonMap("id", dataPermission.getId()).toString();
	}

	@Override
	public void update(String id, DataPermissionBean dataPermissionBean) {
		// TODO Auto-generated method stub

	}

	@Transactional
	@Override
	public void remove(String id, DataPermissionBean dataPermissionBean) {
		this.dataPermissionSysroleRepository.deleteAllById(dataPermissionBean.getSysroleIds());
		this.dataPermissionUsernameRepository.deleteAllById(dataPermissionBean.getUsernames());
		this.dataPermissionGroupRepository.deleteAllById(dataPermissionBean.getGroups());
	}

	@Override
	public Page<DataPermission> list(Pageable pageable) {
		return this.dataPermissionRepository.findAll(pageable);
	}

	@Override
	public DataPermissionBean get(String id) {
		List<DataPermissionSysroleId> sysroleIds = this.dataPermissionSysroleRepository.findByIdDataPermissionId(id)
				.stream().map(m -> m.getId()).collect(Collectors.toList());

		List<DataPermissionUsernameId> usernames = this.dataPermissionUsernameRepository.findByIdDataPermissionId(id)
				.stream().map(m -> m.getId()).collect(Collectors.toList());

		List<DataPermissionGroupId> groupIds = this.dataPermissionGroupRepository.findByIdDataPermissionId(id).stream()
				.map(m -> m.getId()).collect(Collectors.toList());
		return new DataPermissionBean(sysroleIds, usernames, groupIds);
	}

}
