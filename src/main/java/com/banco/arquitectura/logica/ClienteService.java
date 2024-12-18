package com.banco.arquitectura.logica;

import com.banco.arquitectura.bd.jpa.ClienteJPA;
import com.banco.arquitectura.bd.orm.ClienteORM;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ClienteService {

    private final ClienteJPA clienteJPA;
    private static final String CEDULA_NO_ENCONTRADA = "No existe un cliente con la cédula: ";

    public boolean crearCliente(String nombre, String cedula, String correo){
        if (clienteJPA.findByCedula(cedula).isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "La cédula ya está registrada");
        }
        if(clienteJPA.findByCorreo(correo).isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "El correo ya está registrado");
        }
        ClienteORM nuevoCliente = new ClienteORM();
        nuevoCliente.setNombre(nombre);
        nuevoCliente.setCedula(cedula);
        nuevoCliente.setCorreo(correo);
        nuevoCliente.setFechaCreacion(LocalDate.now());
        clienteJPA.save(nuevoCliente);
        return true;
    }

    public List<ClienteORM> verClientes(){
        return clienteJPA.findAll();
    }

    public ClienteORM verCliente(String cedula){
        return clienteJPA.findByCedula(cedula).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, CEDULA_NO_ENCONTRADA + cedula)
        );
    }

    public void actualizarCliente(String nombre, String cedula, String correo){
        if (clienteJPA.findByCedula(cedula).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CEDULA_NO_ENCONTRADA + cedula);
        }
        ClienteORM cliente = clienteJPA.findByCedula(cedula).get();
        cliente.setNombre(nombre);
        cliente.setCorreo(correo);
        clienteJPA.save(cliente);
    }

    public void eliminarCliente(String cedula){
        if (clienteJPA.findByCedula(cedula).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CEDULA_NO_ENCONTRADA + cedula);
        }
        clienteJPA.deleteById(clienteJPA.findByCedula(cedula).get().getId());
    }   
}
