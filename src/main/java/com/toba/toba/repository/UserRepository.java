package com.toba.toba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toba.toba.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
