package com.banco.arquitectura.bd.jpa;

import com.banco.arquitectura.bd.orm.ClienteORM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteJPA extends JpaRepository<ClienteORM, Long> {
    Optional<ClienteORM> findByCedula(String cedula);
    
}
