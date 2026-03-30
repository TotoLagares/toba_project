package com.toba.toba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toba.toba.entities.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
