package com.banco.arquitectura.logica;

import com.banco.arquitectura.bd.jpa.ClienteJPA;
import com.banco.arquitectura.bd.jpa.CuentaJPA;
import com.banco.arquitectura.bd.orm.ClienteORM;
import com.banco.arquitectura.bd.orm.CuentaORM;
import com.banco.arquitectura.controller.dto.CuentaDTO;
import com.banco.arquitectura.controller.dto.NotificarDTO;
import com.banco.arquitectura.controller.publisher.Publisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@AllArgsConstructor
@Slf4j
public class CuentaService {

    private final CuentaJPA cuentaJPA;
    private final ClienteJPA clienteJPA;
    private final Publisher publisher;

    public void crearCuenta(String cedula) {
        ClienteORM cliente = clienteJPA.findByCedula(cedula).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No existe un cliente con la cédula: " + cedula));

        CuentaORM nuevaCuenta = new CuentaORM();
        nuevaCuenta.setCliente(cliente);
        nuevaCuenta.setSaldo(0.0);
        nuevaCuenta.setFechaCreacion(LocalDate.now());
        cuentaJPA.save(nuevaCuenta);

    }

    public void depositar(long id, double monto) {
        if (monto <= 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "El monto a depositar debe ser mayor a 0");
        }
        CuentaORM cuenta = cuentaJPA.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe una cuenta con el id: " + id));
        cuenta.setSaldo(cuenta.getSaldo() + monto);
        notificarCliente(cuenta, monto);
        cuentaJPA.save(cuenta);
    }

    public List<CuentaDTO> verCuentas() {
        return cuentaJPA.findAll().stream()
                .map(cuenta -> new CuentaDTO(
                        cuenta.getId(),
                        cuenta.getCliente().getCedula(),
                        cuenta.getCliente().getNombre(),
                        cuenta.getSaldo(),
                        cuenta.getFechaCreacion()))
                .toList();
    }

    public List<CuentaDTO> verCuentasPorCedula(String cedula) {
        List<CuentaORM> cuentas = cuentaJPA.findByCliente_Cedula(cedula);
        if (cuentas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No se encontraron cuentas para la cédula: " + cedula);
        }

        return cuentas.stream().map(cuenta -> new CuentaDTO(
                cuenta.getId(),
                cuenta.getCliente().getCedula(),
                cuenta.getCliente().getNombre(),
                cuenta.getSaldo(),
                cuenta.getFechaCreacion())).toList();
    }

    public void eliminarCuenta(long id) {
        if (cuentaJPA.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una cuenta con el id: " + id);
        }
        cuentaJPA.deleteById(id);
    }

    public void notificarCliente(CuentaORM cuenta, double monto) {
        {
            ClienteORM clienteNotificar = clienteJPA.findByCedula(cuenta.getCliente().getCedula()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "No existe un cliente con la cédula: " + cuenta.getCliente().getCedula()));
            NotificarDTO notificarDTO = new NotificarDTO(
                    cuenta.getId(),
                    clienteNotificar.getId(),
                    clienteNotificar.getCedula(),
                    clienteNotificar.getNombre(),
                    clienteNotificar.getCorreo(),
                    cuenta.getSaldo(),
                    monto,
                    LocalDateTime.now().toString());
            log.info("Notificando al cliente: " + notificarDTO);
            publisher.sendMessage(notificarDTO);
        }
    }
}
