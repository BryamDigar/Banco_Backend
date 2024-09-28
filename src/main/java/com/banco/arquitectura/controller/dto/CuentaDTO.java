package com.banco.arquitectura.controller.dto;

import java.time.LocalDate;

public record CuentaDTO(Long id, String cedula, String nombre, Double saldo, LocalDate fechaCreacion) {
}
