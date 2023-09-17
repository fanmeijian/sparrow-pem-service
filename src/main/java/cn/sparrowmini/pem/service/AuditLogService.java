package cn.sparrowmini.pem.service;

import java.util.List;

public interface AuditLogService {
	public List<?> getLog(Class<?> c, Object id);
	
	// only return delete log
	public List<?> getLog(Class<?> c);
}
