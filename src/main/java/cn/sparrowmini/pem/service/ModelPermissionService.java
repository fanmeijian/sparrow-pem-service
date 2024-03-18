package cn.sparrowmini.pem.service;

import cn.sparrowmini.pem.model.ModelAttribute.ModelAttributePK;
import cn.sparrowmini.pem.model.constant.PermissionEnum;

public interface ModelPermissionService {
	@Deprecated
	boolean hasPermission(String modelId, PermissionEnum permission, String username);

	/**
	 * 增加规则引擎的判断
	 * 
	 * @param modelId
	 * @param permission
	 * @param username
	 * @param entity
	 * @return
	 */
	boolean hasPermission(String modelId, PermissionEnum permission, String username, Object entity);

	@Deprecated
	boolean hasPermission(ModelAttributePK attributePK, PermissionEnum permission, String username);

	/**
	 * 增加规则引擎的判断
	 * 
	 * @param attributePK
	 * @param permission
	 * @param username
	 * @param entity
	 * @return
	 */
	boolean hasPermission(ModelAttributePK attributePK, PermissionEnum permission, String username, Object entity);
}
