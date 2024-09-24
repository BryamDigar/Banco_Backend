package com.banco.arquitectura.logica;

import com.banco.arquitectura.bd.jpa.ClienteJPA;
import com.banco.arquitectura.bd.jpa.CuentaJPA;
import com.banco.arquitectura.bd.orm.ClienteORM;
import com.banco.arquitectura.bd.orm.CuentaORM;
import com.banco.arquitectura.controller.dto.CuentaDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class CuentaService {

    private final CuentaJPA cuentaJPA;
    private final ClienteJPA clienteJPA;

    public void crearCuenta(String cedula) {
        ClienteORM cliente = clienteJPA.findByCedula(cedula).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe un cliente con la cédula: " + cedula)
        );

        CuentaORM nuevaCuenta = new CuentaORM();
        nuevaCuenta.setCliente(cliente);
        nuevaCuenta.setSaldo(0.0);
        nuevaCuenta.setFecha_creacion(LocalDate.now());
        cuentaJPA.save(nuevaCuenta);

    }

    public void depositar(long id, double monto){
        if(monto <= 0){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "El monto a depositar debe ser mayor a 0");
        }
        CuentaORM cuenta = cuentaJPA.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una cuenta con el id: " + id));
        cuenta.setSaldo(cuenta.getSaldo() + monto);
        cuentaJPA.save(cuenta);
    }

    public List<CuentaDTO> verCuentas() {
        return cuentaJPA.findAll().stream()
                .map(cuenta -> new CuentaDTO(
                        cuenta.getId(),
                        cuenta.getCliente().getCedula(),
                        cuenta.getCliente().getNombre(),
                        cuenta.getSaldo(),
                        cuenta.getFecha_creacion()
                ))
                .collect(Collectors.toList());
    }

    public List<CuentaDTO> verCuentasPorCedula(String cedula) {
        List<CuentaORM> cuentas = cuentaJPA.findByCliente_Cedula(cedula);
        if (cuentas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron cuentas para la cédula: " + cedula);
        }

        return cuentas.stream().map(cuenta -> new CuentaDTO(
                cuenta.getId(),
                cuenta.getCliente().getCedula(),
                cuenta.getCliente().getNombre(),
                cuenta.getSaldo(),
                cuenta.getFecha_creacion()
        )).collect(Collectors.toList());
    }

    public void eliminarCuenta(long id){
        if (cuentaJPA.findById(id).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una cuenta con el id: " + id);
        }
        cuentaJPA.deleteById(id);
    }


}
