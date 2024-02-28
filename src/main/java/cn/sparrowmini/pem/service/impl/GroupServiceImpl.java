package cn.sparrowmini.pem.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.sparrowmini.common.SparrowTree;
import cn.sparrowmini.pem.model.Group;
import cn.sparrowmini.pem.model.constant.GroupTypeEnum;
import cn.sparrowmini.pem.model.relation.GroupRelation;
import cn.sparrowmini.pem.model.relation.GroupRelation.GroupRelationId;
import cn.sparrowmini.pem.model.relation.GroupSysrole;
import cn.sparrowmini.pem.model.relation.GroupSysrole.GroupSysrolePK;
import cn.sparrowmini.pem.model.relation.GroupUser;
import cn.sparrowmini.pem.model.relation.GroupUser.GroupUserId;
import cn.sparrowmini.pem.service.GroupService;
import cn.sparrowmini.pem.service.ScopePermission;
import cn.sparrowmini.pem.service.repository.GroupSysroleRepository;
import cn.sparrowmini.pem.service.repository.GroupUserRepository;
import cn.sparrowmini.pem.service.repository.PemGroupRelationRepository;
import cn.sparrowmini.pem.service.repository.PemGroupRepository;
import cn.sparrowmini.pem.service.scope.GroupScope;
import cn.sparrowmini.pem.service.scope.GroupScope.GroupMemberScope;

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
	@ScopePermission(scope = GroupScope.LIST, name = "")
	public Page<Group> list(Pageable pageable, Group group) {
		if (group != null) {
			Example<Group> example = Example.of(group);
			return this.groupRepository.findAll(example, pageable);
		} else {
			return this.groupRepository.findAll(pageable);
		}
	}

	@Override
	@ScopePermission(scope = GroupScope.CREATE, name = "")
	public String create(Group group) {
		return this.groupRepository.save(group).getId();
	}

	@Override
	@ScopePermission(scope = GroupScope.UPDATE, name = "")
	public void update(String groupId, Map<String, Object> map) {
		Group source = this.groupRepository.findById(groupId).get();
		PatchUpdateHelper.merge(source, map);
		this.groupRepository.save(source);

	}

	@ScopePermission(scope = GroupScope.DELETE, name = "")
	@Override
	public void delete(String groupId) {
		this.groupRepository.deleteById(groupId);
	}

	@ScopePermission(scope = GroupScope.READ, name = "")
	@Override
	public Group get(String groupId) {
		return this.groupRepository.findById(groupId).orElse(null);
	}

	@ScopePermission(scope = GroupMemberScope.ADD, name = "")
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
				this.groupRelationRepository.save(new GroupRelation(id, groupId));
				break;
			default:
				break;
			}
		}

	}

	@ScopePermission(scope = GroupMemberScope.REMOVE, name = "")
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

	@ScopePermission(scope = GroupMemberScope.LIST, name = "")
	@Override
	public Page<?> members(String groupId, GroupTypeEnum type, Pageable pageable) {
		switch (type) {
		case USER:
			return (Page<?>) this.groupUserRepository.findByIdGroupId(groupId, pageable);
		case SYSROLE:
			return (Page<?>) this.groupSysroleRepository.findByIdGroupId(groupId, pageable);
		case GROUP:
			return (Page<?>) this.groupRelationRepository.findByIdParentId(groupId, pageable);
		default:
			break;
		}
		return null;
	}

	@ScopePermission(scope = GroupScope.TREE, name = "")
	@Override
	public SparrowTree<Group, String> expandTree(String groupId) {
		SparrowTree<Group, String> myTree = new SparrowTree<Group, String>(
				this.groupRepository.findById(groupId).orElse(null));
		this.buildTree(myTree);
		return myTree;
	}

	@Override
	public List<?> expandFlat(String groupId, GroupTypeEnum type) {
		List<Group> groups = new ArrayList<>();
		this.getFlatGroups(groupId, groups);
		switch (type) {
		case GROUP:
			return groups;
		case SYSROLE:
			// get sysrole
			List<GroupSysrole> sysroles = new ArrayList<>();
			sysroles.addAll(this.groupSysroleRepository.findByIdGroupId(groupId, Pageable.unpaged()).toList());
			groups.forEach(group -> {
				sysroles.addAll(
						this.groupSysroleRepository.findByIdGroupId(group.getId(), Pageable.unpaged()).toList());
			});
			return sysroles;
		case USER:
			List<GroupUser> users = new ArrayList<>();
			users.addAll(this.groupUserRepository.findByIdGroupId(groupId, Pageable.unpaged()).toList());
			groups.forEach(group -> {
				users.addAll(this.groupUserRepository.findByIdGroupId(group.getId(), Pageable.unpaged()).toList());
			});
			return users;
		default:
			break;
		}
		return null;
	}

	private void getFlatGroups(String parentId, List<Group> members) {
		this.groupRelationRepository.findByIdParentId(parentId, Pageable.unpaged()).toList().forEach(groupRelation -> {
			members.add(this.groupRepository.findById(groupRelation.getId().getGroupId()).get());
			if (this.groupRelationRepository.findById(
					new GroupRelationId(groupRelation.getId().getParentId(), groupRelation.getId().getGroupId()))
					.orElse(null) == null) {
				this.getFlatGroups(groupRelation.getId().getGroupId(), members);
			}

		});
	}

	public void buildTree(SparrowTree<Group, String> myTree) {

		this.groupRelationRepository
				.findByIdParentId(myTree.getMe() == null ? null : myTree.getMe().getId(), Pageable.unpaged()).toList()
				.forEach(f -> {
					SparrowTree<Group, String> leaf = new SparrowTree<Group, String>(
							this.groupRepository.findById(f.getId().getGroupId()).get());
					// 防止死循环
					if (this.groupRelationRepository
							.findById(new GroupRelationId(f.getId().getParentId(), f.getId().getGroupId()))
							.orElse(null) == null)
						buildTree(leaf);

					myTree.getChildren().add(leaf);
				});
	}

}
