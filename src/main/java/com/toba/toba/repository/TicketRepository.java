package com.toba.toba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toba.toba.entities.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
