package cn.sparrowmini.pem.service.impl;

import javax.persistence.EntityManager;

import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.model.constant.PermissionTypeEnum;
import cn.sparrowmini.pem.model.token.PermissionToken;
import cn.sparrowmini.pem.service.PermissionExpressionService;
import cn.sparrowmini.pem.service.PermissionService;

public class PermissionServiceImpl implements PermissionService {
	PermissionExpressionService<String> permissionExpressionService;

	public PermissionServiceImpl() {

	}

	public PermissionServiceImpl(EntityManager entityManager) {
		this.permissionExpressionService = new PermissionExpressionServiceImpl<String>();
	}

	@Override
	public boolean isConfigPermission(PermissionToken permissionToken, PermissionEnum permissionEnum) {
		if (isConfigPermission(permissionToken, permissionEnum, PermissionTypeEnum.ALLOW)
				|| isConfigPermission(permissionToken, permissionEnum, PermissionTypeEnum.DENY))
			return true;
		else
			return false;
	}

	@Override
	public boolean isConfigPermission(PermissionToken permissionToken, PermissionEnum permissionEnum,
			PermissionTypeEnum permissionTypeEnum) {

		if (permissionTypeEnum.equals(PermissionTypeEnum.ALLOW)) {
			if (permissionToken.getAllowPermissions().get(permissionEnum) != null)
				return true;
		}

		if (permissionTypeEnum.equals(PermissionTypeEnum.DENY)) {
			if (permissionToken.getDenyPermissions().get(permissionEnum) != null)
				return true;
		}

		return false;
	}

	@Override
	public boolean hasPermission(String username, PermissionToken permissionToken, PermissionEnum permissionEnum) {
		if (username.equals("ROOT"))
			return true;
		else {

			// TODO 晚上仅仅检查组、用户名和角色三个的权限检查
			return false;
		}
	}
}
