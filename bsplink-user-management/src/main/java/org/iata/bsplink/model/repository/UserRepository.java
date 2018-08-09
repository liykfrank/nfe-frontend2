package org.iata.bsplink.model.repository;

import java.util.Optional;

import org.iata.bsplink.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
