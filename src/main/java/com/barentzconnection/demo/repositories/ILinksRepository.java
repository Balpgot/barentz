package com.barentzconnection.demo.repositories;


import com.barentzconnection.demo.entities.LinkDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ILinksRepository extends JpaRepository<LinkDAO, Long> {
    Optional<LinkDAO> findByName(String name);
}
