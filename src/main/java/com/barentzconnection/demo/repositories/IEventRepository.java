package com.barentzconnection.demo.repositories;

import com.barentzconnection.demo.entities.EventDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IEventRepository extends JpaRepository <EventDAO, Long> {
    List<EventDAO> findAllByDay(Integer day);

}
