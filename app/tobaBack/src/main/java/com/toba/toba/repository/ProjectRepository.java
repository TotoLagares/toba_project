package com.toba.toba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toba.toba.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
