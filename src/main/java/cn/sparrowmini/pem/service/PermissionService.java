package cn.sparrowmini.pem.service;

import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.model.constant.PermissionTypeEnum;
import cn.sparrowmini.pem.model.token.PermissionToken;

public interface PermissionService {

	public boolean hasPermission(String username, PermissionToken permissionToken, PermissionEnum permissionEnum);

	public boolean isConfigPermission(PermissionToken permissionToken, PermissionEnum permissionEnum);

	public boolean isConfigPermission(PermissionToken permissionToken, PermissionEnum permissionEnum,
			PermissionTypeEnum permissionTypeEnum);
}
