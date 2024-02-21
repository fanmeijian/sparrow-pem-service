package cn.sparrowmini.pem.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import cn.sparrowmini.pem.model.Scope;
import cn.sparrowmini.pem.model.Sysrole;
import cn.sparrowmini.pem.model.relation.UserScope;
import cn.sparrowmini.pem.service.exception.NoPermissionException;
import cn.sparrowmini.pem.service.impl.SysroleServiceImpl;
import cn.sparrowmini.pem.service.repository.ScopeRepository;
import cn.sparrowmini.pem.service.repository.UserScopeRepository;

@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
public class TestPermission {

	private final static String USERNAME = "admin";

	@Autowired
	private SysroleServiceImpl sysroleService;

	@Autowired
	private ScopeRepository scopeRepository;

	@Autowired
	private UserScopeRepository userScopeRepository;

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
}
