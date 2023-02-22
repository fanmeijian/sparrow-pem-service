package cn.sparrowmini.pem.service.listener;

import java.lang.annotation.Annotation;

import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.sparrowmini.common.CurrentUser;
import cn.sparrowmini.pem.model.common.ModelPermission;
import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.service.ModelPermissionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UpdateEventListener implements PreUpdateEventListener {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ModelPermissionService modelPermissionService;

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		for (Annotation a : event.getEntity().getClass().getAnnotations()) {
			log.debug("annotation: {}", a.annotationType());
		}

		if (event.getEntity().getClass().isAnnotationPresent(ModelPermission.class)) {
			return !this.modelPermissionService.hasPermission(event.getEntityName(), PermissionEnum.EDITOR,
					CurrentUser.get());
		} else {
			return false;
		}
	}

}
