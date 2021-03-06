package it.unisalento.sonoffbackend.hibernate.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
	
	public User() {}
	
	@Id
	String id;
	//String name;
	//String surname;
	String username;
	
	@OneToMany(mappedBy = "user", targetEntity = Event.class, cascade = CascadeType.ALL , fetch = FetchType.LAZY)
	List<Event> eventList;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<Event> getEventList() {
		return eventList;
	}
	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}

	
}
