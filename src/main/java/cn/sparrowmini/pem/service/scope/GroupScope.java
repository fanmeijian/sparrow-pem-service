package cn.sparrowmini.pem.service.scope;

public final class GroupScope {

	private final static String TYPE = "group";
	private final static String ADMIN = "admin";
	private final static String PREFIX = ADMIN + ":" + TYPE + ":";

	private GroupScope() {

	}

	public final static String CREATE = PREFIX + ScopeOpConstant.CREATE;
	public final static String READ = PREFIX + ScopeOpConstant.READ;
	public final static String UPDATE = PREFIX + ScopeOpConstant.UPDATE;
	public final static String DELETE = PREFIX + ScopeOpConstant.DELETE;
	public final static String LIST = PREFIX + ScopeOpConstant.LIST;

	public final static String TREE = ADMIN + ":" + TYPE + ":" + "tree";

	public final class GroupMemberScope {
		private final static String PREFIX = ADMIN + ":" + GroupScope.TYPE + ":member:";
		public final static String ADD = PREFIX + ScopeOpConstant.ADD;
		public final static String REMOVE = PREFIX + ScopeOpConstant.REMOVE;
		public final static String LIST = PREFIX + ScopeOpConstant.LIST;
	}

	public final class GroupParentOrgScope {
		private final static String PREFIX = ADMIN + ":" + GroupScope.TYPE + ":parent:org:";
		public final static String ADD = PREFIX + ScopeOpConstant.ADD;
		public final static String REMOVE = PREFIX + ScopeOpConstant.REMOVE;
		public final static String LIST = PREFIX + ScopeOpConstant.LIST;
	}

}
