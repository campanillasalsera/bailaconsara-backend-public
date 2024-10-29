package com.bailaconsarabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bailaconsarabackend.model.Taller;

/**
 * Repositorio para gestionar operaciones CRUD de Taller.
 */
public interface TallerRepository extends JpaRepository<Taller, Long> {

}
