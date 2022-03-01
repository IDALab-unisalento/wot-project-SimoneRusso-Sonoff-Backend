package it.unisalento.sonoffbackend.domain;

import java.util.Date;

public class Event {
	int id;
	Date date;
	int event_type;
	User user;
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
	public int getEvent_type() {
		return event_type;
	}
	public void setEvent_type(int event_type) {
		this.event_type = event_type;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	
	
}
