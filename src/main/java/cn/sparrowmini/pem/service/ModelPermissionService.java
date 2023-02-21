package cn.sparrowmini.pem.service;

import cn.sparrowmini.pem.model.constant.PermissionEnum;

public interface ModelPermissionService {
	boolean hasPermission(String modelId,PermissionEnum permission, String username);
}
