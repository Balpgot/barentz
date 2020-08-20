package com.barentzconnection.demo.repositories;

import com.barentzconnection.demo.entities.EventDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEventRepository extends JpaRepository <EventDAO, Long> {
    Optional<EventDAO> findEventDAOByName(String name);
}
