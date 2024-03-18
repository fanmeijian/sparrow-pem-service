package cn.sparrowmini.pem.service;

import java.util.ArrayList;
import java.util.List;

import cn.sparrowmini.pem.model.ModelRule;
import cn.sparrowmini.pem.model.relation.SysroleModel;
import cn.sparrowmini.pem.model.relation.UserModel;
import lombok.Data;

@Data
public class ModelPermissionResponseBody {

	List<SysroleModel> sysroles = new ArrayList<>();
	List<UserModel> usernames = new ArrayList<>();
	List<ModelRule> rules = new ArrayList<>();
}
