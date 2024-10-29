package com.bailaconsarabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bailaconsarabackend.model.SalaBaile;

/**
 * Repositorio para gestionar operaciones CRUD de SalaBaile.
 */
public interface SalaBaileRepository extends JpaRepository<SalaBaile, Long> {

}
