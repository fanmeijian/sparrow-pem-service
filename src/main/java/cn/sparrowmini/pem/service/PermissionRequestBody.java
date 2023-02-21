package cn.sparrowmini.pem.service;

import java.util.ArrayList;
import java.util.List;

import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.model.constant.PermissionTypeEnum;
import lombok.Data;

@Data
public class PermissionRequestBody {
	private List<SysrolePermission> sysroles = new ArrayList<>();
	private List<UserPermission> usernames= new ArrayList<>();
	
	@Data
	public static class SysrolePermission{
		private String sysroleId;
		private PermissionTypeEnum permissionType;
		private PermissionEnum permission;
	}
	
	@Data
	public static class UserPermission{
		private String username;
		private PermissionTypeEnum permissionType;
		private PermissionEnum permission;
	}
}
