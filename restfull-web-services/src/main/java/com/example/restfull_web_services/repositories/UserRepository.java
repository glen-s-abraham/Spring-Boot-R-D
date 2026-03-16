package com.example.restfull_web_services.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restfull_web_services.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    java.util.Optional<User> findByEmail(String email);
}
