package cn.sparrowmini.pem.service;

public enum ScopeTestEnum {
	Org("sdfadffds"),;

	ScopeTestEnum(String alias) {
		this.alias = alias;
	}
	
	private String alias;
	 
    public String getAlias() {
        return alias;
    }
}
