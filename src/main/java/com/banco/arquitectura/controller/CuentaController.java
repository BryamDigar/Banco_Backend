package com.banco.arquitectura.controller;

import com.banco.arquitectura.controller.dto.CuentaDTO;
import com.banco.arquitectura.controller.dto.DepositoDTO;
import com.banco.arquitectura.controller.dto.NotificarDTO;
import com.banco.arquitectura.controller.publisher.Publisher;
import com.banco.arquitectura.logica.CuentaService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CuentaController {

    private CuentaService cuentaService;
    private final Publisher publisher;
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
    @PostMapping(path = "/notificarCliente")
    public String notificarCliente(@RequestBody NotificarDTO notificarDTO) {
        log.info("Notificando al cliente: " + notificarDTO);
        publisher.sendMessage(notificarDTO);
        return "Cliente notificado";
    }
    @GetMapping(path = "/cuentas")
    public List<CuentaDTO> verCuentas() {
        return cuentaService.verCuentas();
    }

    @GetMapping(path = "/cuentas/{cedula}")
    public ResponseEntity<List<CuentaDTO>> verCuentasPorCedula(@PathVariable String cedula) {
        List<CuentaDTO> cuentas = cuentaService.verCuentasPorCedula(cedula);
        return ResponseEntity.ok(cuentas);
    }

    @DeleteMapping(path = "/cuenta/eliminar/{id}")
    public String eliminarCuenta(@PathVariable long id) {
        cuentaService.eliminarCuenta(id);
        return "Cuenta eliminada";
    }
}
