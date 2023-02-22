package cn.sparrowmini.pem.service.listener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Id;

import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.sparrowmini.common.CurrentUser;
import cn.sparrowmini.pem.model.common.ModelPermission;
import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.service.ModelPermissionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
public class ReadEventListener implements PostLoadEventListener {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ModelPermissionService modelPermissionService;

	@Autowired

	@Override
	public void onPostLoad(PostLoadEvent event) {
		for (Annotation a : event.getEntity().getClass().getAnnotations()) {
			log.debug("annotation: {}", a.annotationType());
		}

		if (event.getEntity().getClass().isAnnotationPresent(ModelPermission.class)) {
			if (this.modelPermissionService.hasPermission(event.getEntity().getClass().getName(),
					PermissionEnum.DELETER, CurrentUser.get())) {
//				Model model = modelService.getModel(abstractEntity);
//				model.getModelAttributes().forEach(modelAttribute -> {
//					if (!permissionService.hasReaderPermission(abstractEntity, username, modelAttribute)) {
//						abstractEntity.getErrorMessage().add("无读字段权限: " + modelAttribute.getId().getName());
//						emptyDataField(abstractEntity, modelAttribute.getId().getName(), modelAttribute.getType());
//					}
//				});
			}
		}
	}

	private void emptyDataField(Object object, String fieldName, String type) {
		try {
			Method[] methods = object.getClass().getDeclaredMethods();
			Method setMethod = object.getClass().getMethod(
					"set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), Class.forName(type));
			Method getMethod = object.getClass()
					.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
			Object returnObj = getMethod.invoke(object, new Object[0]);
			returnObj = returnObj == null ? returnObj : null;
			// 不清空id字段
			boolean isId = false;
			List<Field> fieldList = new ArrayList<Field>();
			fieldList.addAll(Arrays.asList(object.getClass().getSuperclass().getDeclaredFields()));
			fieldList.addAll(Arrays.asList(object.getClass().getDeclaredFields()));
//			Field[] fields = ArrayStoreExcept.addAll(object.getClass().getDeclaredFields(),
//					object.getClass().getSuperclass().getDeclaredFields());

			Field[] fields = fieldList.toArray(new Field[] {});
			for (Field field : fields) {
				if (field.getName().equals(fieldName)) {
					for (Annotation annotation : field.getDeclaredAnnotations()) {
						if (annotation.annotationType().equals(Id.class)) {
							isId = true;
						}
					}
				}
			}

			if (!isId) {
				setMethod.invoke(object, returnObj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
