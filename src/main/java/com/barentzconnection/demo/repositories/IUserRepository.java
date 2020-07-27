package com.barentzconnection.demo.repositories;

import com.barentzconnection.demo.entities.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository <UserDAO, Long> {
    Optional<UserDAO> findUserDAOByLogin(String login);
    List<UserDAO> findAllByScoreAfterOrderByScoreDesc(Integer score);
}
