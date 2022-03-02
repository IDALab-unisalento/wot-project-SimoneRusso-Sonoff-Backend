package it.unisalento.sonoffbackend.hibernate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unisalento.sonoffbackend.hibernate.dao.EventRepository;
import it.unisalento.sonoffbackend.hibernate.domain.Event;
import it.unisalento.sonoffbackend.hibernate.iService.IEventService;

@Service
public class EventService implements IEventService {
	
	@Autowired
	EventRepository eventRepository;
	@Override
	public List<Event> findAll() throws Exception {
		try {
			return eventRepository.findAll();
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@Override
	public Event save(Event event) throws Exception {
		try {
			return eventRepository.save(event);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
