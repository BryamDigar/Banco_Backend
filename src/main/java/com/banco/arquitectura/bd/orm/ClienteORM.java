package com.banco.arquitectura.bd.orm;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Table(name = "clientes")
@Entity
@Data
@NoArgsConstructor
public class ClienteORM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nombre;
    @Column(unique = true)
    private String cedula;
    @Column
    private LocalDate fecha_creacion;
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CuentaORM> cuentas;
    
}
