package cn.sparrowmini.pem.service.scope;

public final class ScopeScope {

	private final static String TYPE = "scope";
	private final static String ADMIN = "admin";
	private final static String PREFIX = ADMIN + ":" + TYPE + ":";

	private ScopeScope() {

	}

	public final static String CREATE = PREFIX + ScopeOpConstant.CREATE;
	public final static String READ = PREFIX + ScopeOpConstant.READ;
	public final static String UPDATE = PREFIX + ScopeOpConstant.UPDATE;
	public final static String DELETE = PREFIX + ScopeOpConstant.DELETE;
	public final static String LIST = PREFIX + ScopeOpConstant.LIST;

	public final class ScopePemScope {
		private final static String PREFIX = ADMIN + ":" + ScopeScope.TYPE + ":pem:";
		public final static String ADD = PREFIX + ScopeOpConstant.ADD;
		public final static String REMOVE = PREFIX + ScopeOpConstant.REMOVE;
		public final static String LIST = PREFIX + ScopeOpConstant.LIST;
	}
}
