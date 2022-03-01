package it.unisalento.sonoffbackend.iService;

import java.util.List;

import it.unisalento.sonoffbackend.domain.Event;

public interface IEventService {

	List<Event> findAll();

}
