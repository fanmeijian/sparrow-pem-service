package cn.sparrowmini.pem.service;

import java.util.ArrayList;
import java.util.List;

import cn.sparrowmini.pem.model.SysroleModelAttribute;
import cn.sparrowmini.pem.model.UserModelAttribute;
import lombok.Data;

@Data
public class ModelAttributePermissionResponseBody {

	List<SysroleModelAttribute> sysroles = new ArrayList<>();
	List<UserModelAttribute> usernames = new ArrayList<>();
}
