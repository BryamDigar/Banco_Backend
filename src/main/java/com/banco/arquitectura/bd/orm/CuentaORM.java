package com.banco.arquitectura.bd.orm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "cuentas")
@Entity
@Data
@NoArgsConstructor
public class CuentaORM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cedula", referencedColumnName = "cedula", nullable = false)
    @JsonIgnore
    private ClienteORM cliente;
    @Column
    private Double saldo;
    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;
    
}
