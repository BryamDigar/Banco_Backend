package com.banco.arquitectura.controller;

import com.banco.arquitectura.controller.dto.CuentaDTO;
import com.banco.arquitectura.controller.dto.DepositoDTO;
import com.banco.arquitectura.logica.CuentaService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CuentaController {

    private CuentaService cuentaService;

    @PostMapping(path = "/cuenta")
    public String guardarCuenta(@RequestParam String cedula) {
        cuentaService.crearCuenta(cedula);
        return "Cuenta guardada";
    }

    @PutMapping(path = "/depositar")
    public String depositar(@RequestBody DepositoDTO depositoDTO) {
        cuentaService.depositar(depositoDTO.id(), depositoDTO.monto());
        return "Deposito realizado";
    }

    @GetMapping(path = "/cuentas")
    public List<CuentaDTO> verCuentas() {
        return cuentaService.verCuentas();
    }

    @GetMapping(path = "/cuentas/{cedula}")
    public List<CuentaDTO> verCuentasPorCedula(@PathVariable String cedula) {
        return cuentaService.verCuentasPorCedula(cedula);
    }

    @DeleteMapping(path = "/cuenta/eliminar/{id}")
    public String eliminarCuenta(@PathVariable long id) {
        cuentaService.eliminarCuenta(id);
        return "Cuenta eliminada";
    }
}
