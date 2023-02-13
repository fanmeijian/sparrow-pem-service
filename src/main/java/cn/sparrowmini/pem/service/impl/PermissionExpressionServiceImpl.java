package cn.sparrowmini.pem.service.impl;

import cn.sparrowmini.pem.model.common.PermissionExpression;
import cn.sparrowmini.pem.service.PermissionExpressionService;

public class PermissionExpressionServiceImpl<ID> implements PermissionExpressionService<ID> {

	@Override
	public boolean evaluate(ID id, PermissionExpression<?> permissionExpression) {
		switch (permissionExpression.getExpression()) {
		case IN:
			if (permissionExpression.getIds().contains(id)) {
				return true;
			}
			break;
		case NOT_IN:
			if (permissionExpression.getIds().contains(id)) {
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}

}
