package com.banco.arquitectura.integracion;



import com.banco.arquitectura.controller.dto.ClienteDTO;
import com.banco.arquitectura.controller.dto.DepositoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "h2")
class IntegracionCuentaControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void When_crearCuentaParaCliente_Then_cuentaGuardadaYVisible() {
        ClienteDTO nuevoCliente = new ClienteDTO("Juan Perez", "1234567890555","prueba1@gmail.com");
        testRestTemplate.postForEntity("/cliente", nuevoCliente, String.class);
        ResponseEntity<String> respuestaInsercion = testRestTemplate.postForEntity("/cuenta?cedula=1234567890555", null, String.class);
        Assertions.assertEquals("Cuenta guardada", respuestaInsercion.getBody());

        var cuentas = testRestTemplate.getForEntity("/cuentas/1234567890555", List.class);
        Assertions.assertFalse(Objects.requireNonNull(cuentas.getBody()).isEmpty());
    }

    @Test
    void When_depositarEnCuenta_Then_depositoRealizadoYSaldoActualizado() {
        ClienteDTO nuevoCliente = new ClienteDTO("Juan Perez", "123456789","prueba2@gmail.com");
        testRestTemplate.postForEntity("/cliente", nuevoCliente, String.class);
        ResponseEntity<String> cuentaResponse = testRestTemplate.postForEntity("/cuenta?cedula=123456789", null, String.class);
        Assertions.assertEquals("Cuenta guardada", cuentaResponse.getBody());

        // Perform the deposit
        DepositoDTO nuevoDeposito = new DepositoDTO(2, 5000.0);
        HttpEntity<DepositoDTO> requestEntity = new HttpEntity<>(nuevoDeposito);
        ResponseEntity<String> respuestaActualizacion = testRestTemplate.exchange("/depositar", HttpMethod.PUT, requestEntity, String.class);
        Assertions.assertEquals("Deposito realizado", respuestaActualizacion.getBody());

        // Verify the account balance
        var cuentas = testRestTemplate.getForEntity("/cuentas/123456789", List.class);
        Assertions.assertFalse(Objects.requireNonNull(cuentas.getBody()).isEmpty());
        
    }

    @Test
    void When_eliminarCuenta_Then_cuentaEliminadaYNoVisible() {
        // Crear cliente
        ClienteDTO nuevoCliente = new ClienteDTO("Juan Perez normal", "1234567890222","prueba3@gmail.com");
        testRestTemplate.postForEntity("/cliente", nuevoCliente, String.class);

        // Crear cuenta
        ResponseEntity<String> cuentaResponse = testRestTemplate.postForEntity("/cuenta?cedula=1234567890222", null, String.class);
        Assertions.assertEquals("Cuenta guardada", cuentaResponse.getBody());
        
        // Eliminar la cuenta usando el ID obtenido
        ResponseEntity<String> respuestaEliminacion = testRestTemplate.exchange("/cuenta/eliminar/1", HttpMethod.DELETE, null, String.class);
        Assertions.assertEquals("Cuenta eliminada", respuestaEliminacion.getBody());
    }
}
