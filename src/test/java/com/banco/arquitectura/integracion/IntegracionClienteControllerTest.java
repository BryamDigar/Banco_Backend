package com.banco.arquitectura.integracion;

import com.banco.arquitectura.controller.dto.ClienteDTO;
import com.banco.arquitectura.bd.orm.ClienteORM;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "h2")
class IntegracionClienteControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void When_crearCliente_Then_clienteGuardadoYVisible() {
        ClienteDTO nuevoCliente = new ClienteDTO("Juan Perez", "123456789090","prueba10@gmail.com");
        ResponseEntity<String> respuestaInsercion = testRestTemplate.postForEntity("/cliente", nuevoCliente, String.class);
        Assertions.assertEquals("Cliente guardado", respuestaInsercion.getBody());

        ResponseEntity<ClienteORM> clienteGuardado = testRestTemplate.getForEntity("/cliente/123456789090", ClienteORM.class);
        Assertions.assertEquals("Juan Perez", Objects.requireNonNull(clienteGuardado.getBody()).getNombre());
    }

    @Test
    void When_actualizarCliente_Then_clienteActualizadoYVisible() {
        ClienteDTO nuevoCliente = new ClienteDTO("Juan Perez", "1234567890","prueba@gmail.com");
        testRestTemplate.postForEntity("/cliente", nuevoCliente, String.class);
        ClienteDTO clienteActualizado = new ClienteDTO("Juan Perez Actualizado", "1234567890","prueba@gmail.com");
        HttpEntity<ClienteDTO> requestEntity = new HttpEntity<>(clienteActualizado);
        ResponseEntity<String> respuestaActualizacion = testRestTemplate.exchange("/cliente/actualizar", HttpMethod.PUT, requestEntity, String.class);

        Assertions.assertEquals("Cliente actualizado", respuestaActualizacion.getBody());

        ResponseEntity<ClienteORM> clienteGuardado = testRestTemplate.getForEntity("/cliente/1234567890", ClienteORM.class);
        Assertions.assertEquals("Juan Perez Actualizado", Objects.requireNonNull(clienteGuardado.getBody()).getNombre());
    }

    @Test
    void When_eliminarCliente_Then_clienteEliminadoYNoVisible() {
        ClienteDTO nuevoCliente = new ClienteDTO("Juan Perez", "1234567890","prueba@gmail.com");
        testRestTemplate.postForEntity("/cliente", nuevoCliente, String.class);
        ResponseEntity<String> respuestaEliminacion = testRestTemplate.exchange("/cliente/eliminar/1234567890", HttpMethod.DELETE, null, String.class);
        Assertions.assertEquals("Cliente eliminado", respuestaEliminacion.getBody());

        ResponseEntity<ClienteORM> clienteEliminado = testRestTemplate.getForEntity("/cliente/1234567890", ClienteORM.class);
        Assertions.assertTrue(clienteEliminado.getStatusCode().is4xxClientError() || 
                              (clienteEliminado.getBody() != null && 
                               clienteEliminado.getBody().getId() == null && 
                               clienteEliminado.getBody().getNombre() == null && 
                               clienteEliminado.getBody().getCedula() == null && 
                               clienteEliminado.getBody().getFechaCreacion() == null &&
                               clienteEliminado.getBody().getCuentas() == null));
    }
}
