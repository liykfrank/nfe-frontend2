package org.iata.bsplink.user.model.repository;

import java.util.Optional;

import org.iata.bsplink.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
}
