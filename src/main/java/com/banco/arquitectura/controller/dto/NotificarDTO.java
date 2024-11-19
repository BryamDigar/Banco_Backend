package com.banco.arquitectura.controller.dto;

public record NotificarDTO(Long id, Long  idCliente, String cedula, String nombre, String correo, Double saldoAnterior, Double monto) {
}

