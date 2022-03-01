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
	public List<Event> findAll() {
		return eventRepository.findAll();
	}

}
