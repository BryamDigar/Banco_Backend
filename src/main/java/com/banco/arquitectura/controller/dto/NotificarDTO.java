package com.banco.arquitectura.controller.dto;


import java.io.Serializable;

public record NotificarDTO(Long id, Long idCliente, String cedula, String nombre, String correo, Double saldoAnterior, Double monto, String fechaDeDeposito) implements Serializable {
    private static final long serialVersionUID = 1L;
}