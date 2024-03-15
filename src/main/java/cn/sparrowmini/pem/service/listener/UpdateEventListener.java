package cn.sparrowmini.pem.service.listener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.reflections.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.sparrowmini.common.EntityManagerHelper;
import cn.sparrowmini.pem.model.ModelAttribute.ModelAttributePK;
import cn.sparrowmini.pem.model.common.AttributePermission;
import cn.sparrowmini.pem.model.common.ModelPermission;
import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.service.ModelPermissionService;
import cn.sparrowmini.pem.service.exception.DenyPermissionException;
import cn.sparrowmini.pem.service.exception.NoPermissionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UpdateEventListener implements PreUpdateEventListener {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ModelPermissionService modelPermissionService;

	@SuppressWarnings("deprecation")
	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		for (Annotation a : event.getEntity().getClass().getAnnotations()) {
			log.debug("annotation: {}", a.annotationType());
		}
		Object o = EntityManagerHelper.entityManagerFactory.createEntityManager().find(event.getEntity().getClass(),
				event.getId());

		if (event.getEntity().getClass().isAnnotationPresent(ModelPermission.class)) {

			@SuppressWarnings("unchecked")
			Set<Field> fields = ReflectionUtils.getAllFields(event.getEntity().getClass(),
					ReflectionUtils.withAnnotation(AttributePermission.class));

			for (Field field : fields) {
				try {
					this.modelPermissionService.hasPermission(
							new ModelAttributePK(event.getEntityName(), field.getName()), PermissionEnum.EDITOR,
							username);
				} catch (NoPermissionException | DenyPermissionException ex) {
					Method method = org.springframework.util.ReflectionUtils.findMethod(event.getEntity().getClass(),
							"set" + StringUtils.capitalize(field.getName()), field.getType());
					Method getMethod = org.springframework.util.ReflectionUtils.findMethod(o.getClass(),
							"get" + StringUtils.capitalize(field.getName()));

					org.springframework.util.ReflectionUtils.invokeMethod(method, event.getEntity(),
							org.springframework.util.ReflectionUtils.invokeMethod(getMethod, o));
				}

			}

//			return !this.modelPermissionService.hasPermission(event.getEntityName(), PermissionEnum.EDITOR, username);
			return !this.modelPermissionService.hasPermission(event.getEntityName(), PermissionEnum.EDITOR, username, event.getEntity());

		} else {
			return false;
		}
	}

}
