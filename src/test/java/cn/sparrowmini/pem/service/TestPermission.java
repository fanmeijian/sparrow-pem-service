package cn.sparrowmini.pem.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import cn.sparrowmini.pem.model.DataPermission;
import cn.sparrowmini.pem.model.DataPermissionSysrole;
import cn.sparrowmini.pem.model.DataPermissionUsername;
import cn.sparrowmini.pem.model.DataReadPermission;
import cn.sparrowmini.pem.model.Scope;
import cn.sparrowmini.pem.model.Sysrole;
import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.model.constant.PermissionTypeEnum;
import cn.sparrowmini.pem.model.relation.UserScope;
import cn.sparrowmini.pem.model.relation.UserSysrole;
import cn.sparrowmini.pem.service.exception.NoPermissionException;
import cn.sparrowmini.pem.service.impl.SysroleServiceImpl;
import cn.sparrowmini.pem.service.repository.DataPermissionRepository;
import cn.sparrowmini.pem.service.repository.DataPermissionSysroleRepository;
import cn.sparrowmini.pem.service.repository.DataPermissionUsernameRepository;
import cn.sparrowmini.pem.service.repository.DataReadPermissionRepository;
import cn.sparrowmini.pem.service.repository.ScopeRepository;
import cn.sparrowmini.pem.service.repository.SysroleRepository;
import cn.sparrowmini.pem.service.repository.UserScopeRepository;
import cn.sparrowmini.pem.service.repository.UserSysroleRepository;

@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("h2")
public class TestPermission {

	private final static String USERNAME = "admin";

	@Autowired
	private SysroleServiceImpl sysroleService;

	@Autowired
	private ScopeRepository scopeRepository;

	@Autowired
	private UserScopeRepository userScopeRepository;

	@Autowired
	private SysroleRepository sysroleRepository;

	@Autowired
	private DataReadPermissionRepository dataReadPermissionRepository;

	@Autowired
	private DataPermissionRepository dataPermissionRepository;

	@Autowired
	private DataPermissionSysroleRepository dataPermissionSysroleRepository;

	@Autowired
	private DataPermissionUsernameRepository dataPermissionUsernameRepository;

	@Autowired
	private UserSysroleRepository userSysroleRepository;

	@BeforeEach
	public void init() {
		Scope scope = this.scopeRepository.save(new Scope("admin:sysrole:menu:add", "admin:sysrole:menu:add"));
		this.userScopeRepository.save(new UserScope(USERNAME, scope.getId()));

		scope = this.scopeRepository.save(new Scope("新增角色", "admin:sysrole:add"));
		this.userScopeRepository.save(new UserScope(USERNAME, scope.getId()));

	}

	@Test
	@WithMockUser(username = USERNAME, roles = { "USER", "ADMIN" })
	public void test() {
		Exception exception = assertThrows(NoPermissionException.class, () -> {
			Object savedObject = this.sysroleService.create(new Sysrole("test", "test"));
			assertNotNull(savedObject);
		});

	}

	@Autowired
	private TestDataPermissionRepository testDataPermissionRepository;

	@Autowired
	private TestDataPermissionRepository2 testDataPermissionRepository2;

	@Test
	@WithMockUser(username = USERNAME, roles = { "USER", "ADMIN" })
	public void test1() {
		for (int i = 0; i < 10; i++) {
			if (i < 5) {
				Sysrole sysrole = this.sysroleRepository.save(new Sysrole("ADMIN" + i, "ADMIN" + i));
				DataReadPermission dataReadPermission = this.dataReadPermissionRepository
						.save(new DataReadPermission(null, Collections.singleton(sysrole.getId()), null));
				TestEntity testEntity = new TestEntity("aaaa" + i);
				testEntity.setDataReadPermissionId(dataReadPermission.getId());
				this.testDataPermissionRepository.save(testEntity);
			} else {
				this.testDataPermissionRepository.save(new TestEntity("bbbb" + i));
			}

		}

		List<TestEntity> testEntities = this.testDataPermissionRepository.findAll();
		testEntities.forEach(f -> {
			System.out.println(f.getName());
		});

	}

	@Test
	@WithMockUser(username = USERNAME, roles = { "USER", "ADMIN1" })
	public void test2() {
		SecurityContextHolder.getContext().getAuthentication().getAuthorities().forEach(f -> {
			this.userSysroleRepository.save(new UserSysrole(this.sysroleRepository
					.save(new Sysrole(f.getAuthority().replace("ROLE_", ""), f.getAuthority().replace("ROLE_", "")))
					.getId(), USERNAME));
		});
		for (int i = 0; i < 10; i++) {
			if (i < 5) {
				Sysrole sysrole = this.sysroleRepository.findByCode("ADMIN" + i).size() == 0
						? this.sysroleRepository.save(new Sysrole("ADMIN" + i, "ADMIN" + i))
						: this.sysroleRepository.findByCode("ADMIN" + i).get(0);
				DataPermission dataReadPermission = this.dataPermissionRepository.save(new DataPermission());
				TestEntity2 testEntity = new TestEntity2("aaaa" + i);
				testEntity.setDataPermissionId(dataReadPermission.getId());
				this.dataPermissionSysroleRepository.save(new DataPermissionSysrole(dataReadPermission.getId(),
						sysrole.getId(), PermissionTypeEnum.ALLOW, PermissionEnum.READER));

				this.testDataPermissionRepository2.save(testEntity);
			} else {

				if (i == 7 ) {
					DataPermission dataReadPermission = this.dataPermissionRepository.save(new DataPermission());
					this.testDataPermissionRepository2.save(new TestEntity2("bbbb" + i, dataReadPermission.getId()));
					this.dataPermissionUsernameRepository.save(new DataPermissionUsername(dataReadPermission.getId(),
							USERNAME, PermissionTypeEnum.DENY, PermissionEnum.READER));
					this.dataPermissionUsernameRepository.save(new DataPermissionUsername(dataReadPermission.getId(),
							USERNAME, PermissionTypeEnum.ALLOW, PermissionEnum.READER));
				} else {
					TestEntity2 testEntity = this.testDataPermissionRepository2.save(new TestEntity2("bbbb" + i));
				}

			}

		}

		List<TestEntity2> testEntities = this.testDataPermissionRepository2.findAll();
		testEntities.forEach(f -> {
			System.out.println(f.getName());
		});

	}
}
