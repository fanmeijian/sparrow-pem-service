package cn.sparrowmini.pem.service.impl;

import java.util.Arrays;

import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.sparrowmini.common.CurrentUser;
import cn.sparrowmini.pem.model.constant.PermissionEnum;
import cn.sparrowmini.pem.service.ModelPermissionService;

@Component
public class InsertEventListenerClass implements PreInsertEventListener {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ModelPermissionService modelPermissionService;

	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		// Subscriber to the insert events on your entities.
		System.out.println("The Event comes here with data: " + Arrays.toString(event.getState()));
		System.out.println("event.getEntityName(): " + event.getEntityName());
//		return false;
//		System.out.println("event.getEntityName(): " + event.getEntityName());
		return !this.modelPermissionService.hasPermission(event.getEntityName(), PermissionEnum.AUTHOR, CurrentUser.get());
	}

}
