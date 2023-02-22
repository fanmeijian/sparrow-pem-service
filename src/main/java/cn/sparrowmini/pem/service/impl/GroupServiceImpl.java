package cn.sparrowmini.pem.service.impl;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.sparrowmini.pem.model.Group;
import cn.sparrowmini.pem.model.constant.GroupTypeEnum;
import cn.sparrowmini.pem.model.relation.GroupRelation;
import cn.sparrowmini.pem.model.relation.GroupSysrole;
import cn.sparrowmini.pem.model.relation.GroupUser;
import cn.sparrowmini.pem.model.relation.GroupRelation.GroupRelationId;
import cn.sparrowmini.pem.model.relation.GroupSysrole.GroupSysrolePK;
import cn.sparrowmini.pem.model.relation.GroupUser.GroupUserId;
import cn.sparrowmini.pem.service.GroupService;
import cn.sparrowmini.pem.service.repository.PemGroupRelationRepository;
import cn.sparrowmini.pem.service.repository.PemGroupRepository;
import cn.sparrowmini.pem.service.repository.GroupSysroleRepository;
import cn.sparrowmini.pem.service.repository.GroupUserRepository;

@Service("pemGroupServiceImpl")
public class GroupServiceImpl implements GroupService {

	@Autowired
	private PemGroupRepository groupRepository;
	@Autowired
	private GroupUserRepository groupUserRepository;
	@Autowired
	private GroupSysroleRepository groupSysroleRepository;
	@Autowired
	private PemGroupRelationRepository groupRelationRepository;

	@Override
	public Page<Group> list(Pageable pageable, Group group) {
		if (group != null) {
			Example<Group> example = Example.of(group);
			return this.groupRepository.findAll(example, pageable);
		} else {
			return this.groupRepository.findAll(pageable);
		}
	}

	@Override
	public String create(Group group) {
		return this.groupRepository.save(group).getId();
	}

	@Override
	public void update(String groupId, Map<String, Object> map) {
		Group source = this.groupRepository.findById(groupId).get();
		PatchUpdateHelper.merge(source, map);
		this.groupRepository.save(source);

	}

	@Override
	public void delete(String groupId) {
		this.groupRepository.deleteById(groupId);
	}

	@Override
	public Group get(String groupId) {
		return this.groupRepository.findById(groupId).orElse(null);
	}

	@Transactional
	@Override
	public void addMembers(String groupId, GroupTypeEnum type, String[] ids) {
		for (String id : ids) {

			switch (type) {
			case USER:
				this.groupUserRepository.save(new GroupUser(groupId, id));

				break;
			case SYSROLE:
				this.groupSysroleRepository.save(new GroupSysrole(groupId, id));
				break;
			case GROUP:
				this.groupRelationRepository.save(new GroupRelation(groupId, id));
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void removeMembers(String groupId, GroupTypeEnum type, String[] ids) {
		for (String id : ids) {
			switch (type) {
			case USER:
				this.groupUserRepository.deleteById(new GroupUserId(groupId, id));

				break;
			case SYSROLE:
				this.groupSysroleRepository.deleteById(new GroupSysrolePK(groupId, id));
				break;
			case GROUP:
				this.groupRelationRepository.deleteById(new GroupRelationId(groupId, id));
				break;
			default:
				break;
			}
		}
	}

	@Override
	public Page<?> members(String groupId, GroupTypeEnum type, Pageable pageable) {
		switch (type) {
		case USER:
			return (Page<?>) this.groupUserRepository.findAll();
		case SYSROLE:
			return (Page<?>) this.groupSysroleRepository.findAll();
		case GROUP:
			return (Page<?>) this.groupRelationRepository.findAll();
		default:
			break;
		}
		return null;
	}

}
