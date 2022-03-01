package it.unisalento.sonoffbackend.hibernate.iService;

import java.util.List;

import it.unisalento.sonoffbackend.hibernate.domain.Event;

public interface IEventService {

	List<Event> findAll();

	Event save(Event event) throws IllegalArgumentException;

}
