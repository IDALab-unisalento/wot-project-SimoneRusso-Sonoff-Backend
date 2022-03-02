package it.unisalento.sonoffbackend.hibernate.dto;

import java.util.Date;

import it.unisalento.sonoffbackend.model.EventCode;


public class EventDTO {
	int id;
	Date date;
	String event_type;
	UserDTO userDTO;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public UserDTO getUserDTO() {
		return userDTO;
	}
	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	
	
}
