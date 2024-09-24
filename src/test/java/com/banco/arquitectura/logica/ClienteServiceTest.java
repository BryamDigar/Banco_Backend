package com.banco.arquitectura.logica;

import com.banco.arquitectura.bd.jpa.ClienteJPA;
import com.banco.arquitectura.bd.orm.ClienteORM;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    ClienteJPA clienteJPA;

    @InjectMocks
    ClienteService service;

    @Test
    void When_crearCliente_Then_returnTrue() {
        boolean resultado = service.crearCliente("Juan", "123");
        Assertions.assertTrue(resultado);
        Mockito.verify(clienteJPA).save(Mockito.any());
    }

    @Test
    void Given_cedulaExistente_When_crearCliente_Then_throwArithmeticException() {
        Mockito.when(clienteJPA.findByCedula("123")).thenReturn(java.util.Optional.of(new ClienteORM()));
        Assertions.assertThrows(ResponseStatusException.class,
                () -> service.crearCliente("Juan", "123")
        );
    }
    @Test
    void When_verClientes_Then_returnList() {
        service.verClientes();
        Mockito.verify(clienteJPA).findAll();
    }

    @Test
    void When_verCliente_Then_returnCliente() {
        Mockito.when(clienteJPA.findByCedula("123")).thenReturn(java.util.Optional.of(new ClienteORM()));
        service.verCliente("123");
        Mockito.verify(clienteJPA).findByCedula("123");
    }

    @Test
    void Given_cedulaNoExistente_When_verCliente_Then_throwArithmeticException() {
        Mockito.when(clienteJPA.findByCedula("123")).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class,
                () -> service.verCliente("123")
        );
    }

    @Test
    void When_actualizarCliente_Then_updateCliente() {
        Mockito.when(clienteJPA.findByCedula("123")).thenReturn(java.util.Optional.of(new ClienteORM()));
        service.actualizarCliente("Juan", "123");
        Mockito.verify(clienteJPA).save(Mockito.any());
    }

    @Test
    void Given_cedulaNoExistente_When_actualizarCliente_Then_throwArithmeticException() {
        Mockito.when(clienteJPA.findByCedula("123")).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class,
                () -> service.actualizarCliente("Juan", "123")
        );
    }

    @Test
    void When_eliminarCliente_Then_deleteCliente() {
        Mockito.when(clienteJPA.findByCedula("123")).thenReturn(java.util.Optional.of(new ClienteORM()));
        service.eliminarCliente("123");
        Mockito.verify(clienteJPA).deleteById(Mockito.any());
    }

    @Test
    void Given_cedulaNoExistente_When_eliminarCliente_Then_throwArithmeticException() {
        Mockito.when(clienteJPA.findByCedula("123")).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class,
                () -> service.eliminarCliente("123")
        );
    }
}