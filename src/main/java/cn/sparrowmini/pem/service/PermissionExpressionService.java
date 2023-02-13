package cn.sparrowmini.pem.service;

import cn.sparrowmini.pem.model.common.PermissionExpression;

public interface PermissionExpressionService<ID> {

	public boolean evaluate(ID id ,PermissionExpression<?> permissionExpression);
}
