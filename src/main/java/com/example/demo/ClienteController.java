package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public ResponseEntity<?> listarClientes() {
        try {
            List<Cliente> clientes = clienteRepository.findAll();
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar clientes: " + e.getMessage());
        } finally {
            System.out.println("Operação de listagem de clientes finalizada.");
        }
    }

    @PostMapping
    public ResponseEntity<?> adicionarCliente(@RequestBody Cliente novoCliente) {
        try {
            Cliente salvo = clienteRepository.save(novoCliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar cliente: " + e.getMessage());
        } finally {
            System.out.println("Operação de cadastro de cliente finalizada.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(@PathVariable Integer id, @RequestBody Cliente clienteAtualizado) {
        try {
            Optional<Cliente> clienteOpt = clienteRepository.findById(id);

            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                cliente.setNome(clienteAtualizado.getNome());
                cliente.setCpf(clienteAtualizado.getCpf());
                Cliente atualizado = clienteRepository.save(cliente);
                return ResponseEntity.ok(atualizado);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente não encontrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar cliente: " + e.getMessage());
        } finally {
            System.out.println("Operação de atualização de cliente finalizada.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagarCliente(@PathVariable Integer id) {
        try {
            if (!clienteRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Cliente não encontrado.");
            }

            clienteRepository.deleteById(id);
            return ResponseEntity.ok("Cliente removido com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao apagar cliente: " + e.getMessage());
        } finally {
            System.out.println("Operação de exclusão de cliente finalizada.");
        }
    }
}