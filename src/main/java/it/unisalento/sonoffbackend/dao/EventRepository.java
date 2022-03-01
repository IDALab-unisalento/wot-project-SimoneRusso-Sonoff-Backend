package it.unisalento.sonoffbackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.unisalento.sonoffbackend.domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

}
