package cn.sparrowmini.pem.service;

import java.util.ArrayList;
import java.util.List;

import cn.sparrowmini.pem.model.ModelAttributeRule;
import cn.sparrowmini.pem.model.relation.SysroleModelAttribute;
import cn.sparrowmini.pem.model.relation.UserModelAttribute;
import lombok.Data;

@Data
public class ModelAttributePermissionResponseBody {

	List<SysroleModelAttribute> sysroles = new ArrayList<>();
	List<UserModelAttribute> usernames = new ArrayList<>();
	List<ModelAttributeRule> rules = new ArrayList<>();
}
