package com.barentzconnection.demo.repositories;

import com.barentzconnection.demo.entities.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUnregisteredUsersRepository extends JpaRepository<UserDAO, Long> {
    Optional<UserDAO> findUserDAOByRegistrationToken(String token);
}
