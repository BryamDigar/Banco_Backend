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

    public boolean crearCliente(String nombre, String Cedula){
        if (clienteJPA.findByCedula(Cedula).isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "La cédula ya está registrada");
        }
        ClienteORM nuevoCliente = new ClienteORM();
        nuevoCliente.setNombre(nombre);
        nuevoCliente.setCedula(Cedula);
        nuevoCliente.setFecha_creacion(LocalDate.now());
        clienteJPA.save(nuevoCliente);
        return true;
    }

    public List<ClienteORM> verClientes(){
        return clienteJPA.findAll();
    }

    public ClienteORM verCliente(String cedula){
        return clienteJPA.findByCedula(cedula).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un cliente con la cédula: " + cedula)
        );
    }

    public void actualizarCliente(String nombre, String cedula){
        if (clienteJPA.findByCedula(cedula).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un cliente con la cédula: " + cedula);
        }
        ClienteORM cliente = clienteJPA.findByCedula(cedula).get();
        cliente.setNombre(nombre);
        clienteJPA.save(cliente);
    }

    public void eliminarCliente(String cedula){
        if (clienteJPA.findByCedula(cedula).isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un cliente con la cédula: " + cedula);
        }
        clienteJPA.deleteById(clienteJPA.findByCedula(cedula).get().getId());
    }
    
}
