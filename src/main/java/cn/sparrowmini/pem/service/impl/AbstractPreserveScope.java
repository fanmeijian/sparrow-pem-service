package cn.sparrowmini.pem.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.sparrowmini.pem.service.scope.PreserveScope;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统保留的功能通用接口
 * @author fansword
 *
 */

@Slf4j
public abstract class AbstractPreserveScope implements PreserveScope {

	@Override
	public List<String> getScopes() {
		List<String> fields = new ArrayList<>();
		for (Field field : this.getClass().getFields()) {
			log.debug("field name: {}", field.getName());
			if (field.getName().startsWith("SCOPE_"))
				try {
					fields.add(field.get(this).toString());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
		}
		return fields;
	}
	
	@Override
	public List<String> getRoles() {
		List<String> fields = new ArrayList<>();
		for (Field field : this.getClass().getFields()) {
			log.debug("field name: {}", field.getName());
			if (field.getName().startsWith("ROLE_"))
				try {
					fields.add(field.get(this).toString());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
		}
		return fields;
	}

}
