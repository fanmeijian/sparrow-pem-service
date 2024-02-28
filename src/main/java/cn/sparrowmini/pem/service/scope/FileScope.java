package cn.sparrowmini.pem.service.scope;

public final class FileScope {
	private final static String TYPE = "scope";
	private final static String ADMIN = "admin";

	private FileScope() {

	}

	public final static String CREATE = ADMIN + ":" + TYPE + ":" + ScopeOpConstant.CREATE;
	public final static String READ = ADMIN + ":" + TYPE + ":" + ScopeOpConstant.READ;
	public final static String UPDATE = ADMIN + ":" + TYPE + ":" + ScopeOpConstant.UPDATE;
	public final static String DELETE = ADMIN + ":" + TYPE + ":" + ScopeOpConstant.DELETE;
	public final static String LIST = ADMIN + ":" + TYPE + ":" + ScopeOpConstant.LIST;

	public final static String PEM_ADD = ADMIN + ":" + TYPE + ":pem:" + ScopeOpConstant.ADD;
	public final static String PEM_REMOVE = ADMIN + ":" + TYPE + ":pem:" + ScopeOpConstant.REMOVE;
	public final static String PEM_LIST = ADMIN + ":" + TYPE + ":pem:" + ScopeOpConstant.LIST;
}
