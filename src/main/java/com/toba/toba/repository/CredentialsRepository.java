package com.toba.toba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toba.toba.entities.Credentials;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
}
