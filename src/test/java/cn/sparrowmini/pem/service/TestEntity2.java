package cn.sparrowmini.pem.service;

import javax.persistence.Entity;

import cn.sparrowmini.pem.model.DataPermissionEntity;

@Entity
public class TestEntity2 extends DataPermissionEntity {
	private String name;

	public TestEntity2(String name) {
		super();
		this.name = name;
	}

	public TestEntity2() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
