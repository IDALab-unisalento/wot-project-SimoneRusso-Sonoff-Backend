package it.unisalento.sonoffbackend.wrapper;

import java.util.List;

import it.unisalento.sonoffbackend.hibernate.dto.EventDTO;
import it.unisalento.sonoffbackend.model.LoggedUser;

public class LogApiWrapper {
	LoggedUser loggedUser;
	List<EventDTO> eventDtoList;
	public LoggedUser getLoggedUser() {
		return loggedUser;
	}
	public void setLoggedUser(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}
	public List<EventDTO> getEventDtoList() {
		return eventDtoList;
	}
	public void setEventDtoList(List<EventDTO> eventDtoList) {
		this.eventDtoList = eventDtoList;
	}
	
	
}
