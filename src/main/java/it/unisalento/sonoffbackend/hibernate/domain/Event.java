package it.unisalento.sonoffbackend.hibernate.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import it.unisalento.sonoffbackend.model.EventCode;

@Entity
public class Event {
	
	
	
	public Event() {}
	
	@GeneratedValue
	@Id
	int id;
	String date;
	String event_type;
	
	@ManyToOne
	User user;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	
	
}
