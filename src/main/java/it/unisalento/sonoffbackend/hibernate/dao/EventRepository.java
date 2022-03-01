package it.unisalento.sonoffbackend.hibernate.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unisalento.sonoffbackend.hibernate.domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

}
