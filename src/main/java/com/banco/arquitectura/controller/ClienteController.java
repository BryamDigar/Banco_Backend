package com.banco.arquitectura.controller;

import com.banco.arquitectura.bd.orm.ClienteORM;
import com.banco.arquitectura.controller.dto.ClienteDTO;
import com.banco.arquitectura.logica.ClienteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ClienteController {

    private ClienteService clienteService;

    @PostMapping(path = "/cliente")
    public String guardarCliente(@RequestBody ClienteDTO clienteDTO) {
        clienteService.crearCliente(clienteDTO.nombre(), clienteDTO.cedula(), clienteDTO.correo());
        return "Cliente guardado";
    }

    @GetMapping(path = "/clientes")
    public List<ClienteORM> verClientes() {
        return clienteService.verClientes();
    }

    @GetMapping(path = "/cliente/{cedula}")
    public ClienteORM verCliente(@PathVariable String cedula) {
        return clienteService.verCliente(cedula);
    }

    @PutMapping(path = "/cliente/actualizar")
    public String actualizarCliente(@RequestBody ClienteDTO clienteDTO) {
        clienteService.actualizarCliente(clienteDTO.nombre(), clienteDTO.cedula(), clienteDTO.correo());
        return "Cliente actualizado";
    }

    @DeleteMapping(path = "/cliente/eliminar/{cedula}")
    public String eliminarCliente(@PathVariable String cedula) {
        clienteService.eliminarCliente(cedula);
        return "Cliente eliminado";
    }
}
