package cn.sparrowmini.pem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import cn.sparrowmini.pem.service.impl.SysroleServiceImpl;

@SpringBootTest
public class TestPermission {
	@Autowired
	private SysroleServiceImpl sysroleService;

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	public void test() {
		this.sysroleService.addMenus("",null);
	}
}
