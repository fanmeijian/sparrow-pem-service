package cn.sparrowmini.pem.service.listener;

import java.lang.annotation.Annotation;

import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import cn.sparrowmini.pem.model.common.ModelPermission;
import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.service.ModelPermissionService;
import cn.sparrowmini.pem.service.exception.DenyPermissionException;
import cn.sparrowmini.pem.service.exception.NoPermissionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InsertEventListener implements PreInsertEventListener {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ModelPermissionService modelPermissionService;

	@Override
	public boolean onPreInsert(PreInsertEvent event) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		for (Annotation a : event.getEntity().getClass().getAnnotations()) {
			log.debug("annotation: {}", a.annotationType());
		}

		if (event.getEntity().getClass().isAnnotationPresent(ModelPermission.class)) {
			return !this.modelPermissionService.hasPermission(event.getEntityName(), PermissionEnum.AUTHOR, username);

		} else {
			return false;
		}

	}

}
