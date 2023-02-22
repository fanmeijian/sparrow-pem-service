package cn.sparrowmini.pem.service.listener;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HibernateListener {
	private final EntityManagerFactory entityManagerFactory;
	private final InsertEventListener insertEventListenerClass;
	private final UpdateEventListener updateEventListener;
	private final DeleteEventListener deleteEventListener;

	@PostConstruct
	private void init() {
		SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
		EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
		registry.getEventListenerGroup(EventType.PRE_INSERT).prependListener(insertEventListenerClass);
		registry.getEventListenerGroup(EventType.PRE_UPDATE).prependListener(updateEventListener);
		registry.getEventListenerGroup(EventType.PRE_DELETE).prependListener(deleteEventListener);
	}
}
