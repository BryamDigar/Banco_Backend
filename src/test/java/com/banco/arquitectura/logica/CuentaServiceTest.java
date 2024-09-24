package com.banco.arquitectura.logica;

import com.banco.arquitectura.bd.jpa.ClienteJPA;
import com.banco.arquitectura.bd.jpa.CuentaJPA;
import com.banco.arquitectura.bd.orm.ClienteORM;
import com.banco.arquitectura.bd.orm.CuentaORM;
import com.banco.arquitectura.controller.dto.CuentaDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    CuentaJPA cuentaJPA;

    @Mock
    ClienteJPA clienteJPA;

    @InjectMocks
    CuentaService serviceCuenta;

    @Test
    void When_crearCuenta_Then_returnId() {
        Mockito.when(clienteJPA.findByCedula("123")).thenReturn(java.util.Optional.of(new ClienteORM()));
        serviceCuenta.crearCuenta("123");
        Mockito.verify(cuentaJPA).save(Mockito.any());
    }

    @Test
    void Given_cedulaNoExistente_When_crearCuenta_Then_throwArithmeticException() {
        Mockito.when(clienteJPA.findByCedula("123")).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class,
                () -> serviceCuenta.crearCuenta("123")
        );
    }

    @Test
    void When_depositar_Then_returnTrue() {
        CuentaORM cuentaORM = new CuentaORM();
        cuentaORM.setSaldo(0.0);
        Mockito.when(cuentaJPA.findById(1L)).thenReturn(java.util.Optional.of(cuentaORM));
        serviceCuenta.depositar(1L, 100.0);
        assertEquals(100.0, cuentaORM.getSaldo());
    }

    @Test
    void Given_montoNegativo_When_depositar_Then_throwArithmeticException() {
        Assertions.assertThrows(ResponseStatusException.class,
                () -> serviceCuenta.depositar(1L, -100.0)
        );
    }

    @Test
    void Given_cuentaNoExistente_When_depositar_Then_throwArithmeticException() {
        Mockito.when(cuentaJPA.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class,
                () -> serviceCuenta.depositar(1L, 100.0)
        );
    }

    @Test
    void When_verCuentas_Then_returnList() {
        ClienteORM mockCliente = new ClienteORM();
        mockCliente.setCedula("123");
        mockCliente.setNombre("Juan");

        CuentaORM mockCuenta = new CuentaORM();
        mockCuenta.setId(1L);
        mockCuenta.setCliente(mockCliente);
        mockCuenta.setSaldo(500.0);
        mockCuenta.setFecha_creacion(LocalDate.now());

        Mockito.when(cuentaJPA.findAll()).thenReturn(List.of(mockCuenta));

        List<CuentaDTO> result = serviceCuenta.verCuentas();
        Mockito.verify(cuentaJPA).findAll();

        assertEquals(1, result.size());
        assertEquals("123", result.get(0).cedula());
        assertEquals("Juan", result.get(0).nombre());
        assertEquals(500.0, result.get(0).saldo());
    }


    @Test
    void When_verCuentasPorCedula_Then_returnList() {
        ClienteORM mockCliente = new ClienteORM();
        mockCliente.setCedula("123");
        mockCliente.setNombre("Juan");

        CuentaORM mockCuenta = new CuentaORM();
        mockCuenta.setId(1L);
        mockCuenta.setCliente(mockCliente);
        mockCuenta.setSaldo(500.0);
        mockCuenta.setFecha_creacion(LocalDate.now());

        Mockito.when(cuentaJPA.findByCliente_Cedula("123")).thenReturn(List.of(mockCuenta));

        List<CuentaDTO> result = serviceCuenta.verCuentasPorCedula("123");
        Mockito.verify(cuentaJPA).findByCliente_Cedula("123");

        assertEquals(1, result.size());
        assertEquals("123", result.get(0).cedula());
        assertEquals("Juan", result.get(0).nombre());
        assertEquals(500.0, result.get(0).saldo());
    }

    @Test
    void Given_cedulaNoExistente_When_verCuentasPorCedula_Then_throwArithmeticException() {
        Mockito.when(cuentaJPA.findByCliente_Cedula("123")).thenReturn(List.of());
        Assertions.assertThrows(ResponseStatusException.class,
                () -> serviceCuenta.verCuentasPorCedula("123")
        );
    }

    @Test
    void When_eliminarCuenta_Then_returnTrue() {
        CuentaORM cuentaORM = new CuentaORM();
        cuentaORM.setId(1L);
        Mockito.when(cuentaJPA.findById(1L)).thenReturn(java.util.Optional.of(cuentaORM));
        serviceCuenta.eliminarCuenta(1L);
        Mockito.verify(cuentaJPA).deleteById(1L);
    }

    @Test
    void Given_cuentaNoExistente_When_eliminarCuenta_Then_throwArithmeticException() {
        Mockito.when(cuentaJPA.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class,
                () -> serviceCuenta.eliminarCuenta(1L)
        );
    }


}