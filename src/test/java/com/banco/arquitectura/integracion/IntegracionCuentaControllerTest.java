package com.banco.arquitectura.integracion;


import com.banco.arquitectura.controller.dto.ClienteDTO;
import com.banco.arquitectura.controller.dto.DepositoDTO;
import com.banco.arquitectura.controller.dto.CuentaDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "h2")
public class IntegracionCuentaControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void guardarYVerCuenta() {
        ClienteDTO nuevoCliente = new ClienteDTO("Juan Perez", "1234567890");
        testRestTemplate.postForEntity("/cliente", nuevoCliente, String.class);
        ResponseEntity<String> respuestaInsercion = testRestTemplate.postForEntity("/cuenta?cedula=1234567890", null, String.class);
        Assertions.assertEquals("Cuenta guardada", respuestaInsercion.getBody());

        ResponseEntity<List> cuentas = testRestTemplate.getForEntity("/cuentas/1234567890", List.class);
        Assertions.assertFalse(Objects.requireNonNull(cuentas.getBody()).isEmpty());
    }

    @Test
    void depositarYVerCuenta() {
        ClienteDTO nuevoCliente = new ClienteDTO("Juan Perez", "1234567890");
        testRestTemplate.postForEntity("/cliente", nuevoCliente, String.class);
        testRestTemplate.postForEntity("/cuenta?cedula=1234567890", null, String.class);
        DepositoDTO nuevoDeposito = new DepositoDTO(1, 5000.0);
        ResponseEntity<String> respuestaDeposito = testRestTemplate.postForEntity("/depositar", nuevoDeposito, String.class);
        Assertions.assertEquals("Deposito realizado", respuestaDeposito.getBody());

        ResponseEntity<List> cuentas = testRestTemplate.getForEntity("/cuentas/1234567890", List.class);
        Assertions.assertTrue(Objects.requireNonNull(cuentas.getBody()).size() > 0);
    }

    @Test
    void eliminarCuenta() {
        ClienteDTO nuevoCliente = new ClienteDTO("Juan Perez", "1234567890");
        testRestTemplate.postForEntity("/cliente", nuevoCliente, String.class);
        testRestTemplate.postForEntity("/cuenta?cedula=1234567890", null, String.class);
        ResponseEntity<String> respuestaEliminacion = testRestTemplate.exchange("/cuenta/eliminar/1", HttpMethod.DELETE, null, String.class);
        Assertions.assertEquals("Cuenta eliminada", respuestaEliminacion.getBody());

        ResponseEntity<List> cuentas = testRestTemplate.getForEntity("/cuentas/1234567890", List.class);
        Assertions.assertTrue(Objects.requireNonNull(cuentas.getBody()).isEmpty());
    }
}
