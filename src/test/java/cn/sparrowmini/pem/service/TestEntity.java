package cn.sparrowmini.pem.service;

import javax.persistence.Entity;

import cn.sparrowmini.pem.model.DataReadPermissionEntity;

@Entity
public class TestEntity extends DataReadPermissionEntity {
	private String name;

	public TestEntity(String name) {
		super();
		this.name = name;
	}

	public TestEntity() {
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
