package cn.sparrowmini.pem.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.sparrowmini.common.EntityManagerHelper;
import cn.sparrowmini.common.model.DeleteLog;
import cn.sparrowmini.pem.service.AuditLogRestService;
import cn.sparrowmini.pem.service.AuditLogService;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AuditLogServiceImpl_ implements AuditLogRestService {

	@Autowired
	AuditLogService auditLogService;
	static Class<?> keyClass = null;

	@Override
	public List<?> getLog(String className, String id) {

		try {

			ReflectionUtils.doWithLocalFields(Class.forName(className), field -> {
				if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
					keyClass = field.getType();
				}
			});
			try {
				if (keyClass.getName().equals(String.class.getName())) {
					return auditLogService.getLog(Class.forName(className), id.toString());
				} else {
					return auditLogService.getLog(Class.forName(className),
							new ObjectMapper().readValue(id.toString(), keyClass));
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<?> getLog(String className) {
		try {
			List<?> list = auditLogService.getLog(Class.forName(className));
			EntityManager entityManager = EntityManagerHelper.entityManagerFactory.createEntityManager();
			List<Object[]> logList = new ArrayList<>();
			list.stream().forEach(m -> {
				TypedQuery<DeleteLog> query = entityManager.createNamedQuery("DeleteLog.findByClassName",
						DeleteLog.class);
				query.setParameter("className", className);
				DeleteLog deleteLog = query.getSingleResult();
				Object[] a = (Object[]) m;
				logList.add(Arrays.asList(a[0], a[1], deleteLog.getOpUser()).toArray());
			});
			return logList;
//			return auditLogService.getLog(Class.forName(className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
