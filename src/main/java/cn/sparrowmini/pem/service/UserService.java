package cn.sparrowmini.pem.service;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sparrowmini.pem.model.Sysrole;
import cn.sparrowmini.pem.model.relation.UserSysrole;
import cn.sparrowmini.pem.model.token.UserToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "user", description = "用户服务")
@RequestMapping("/users")
public interface UserService {
	@Operation(summary = "用户token")
	@GetMapping("")
	@ResponseBody
	public UserToken userToken(String username);

	public List<Sysrole> getSysroles(String username);

	public List<UserSysrole> getUserSysroles(String username);
}
