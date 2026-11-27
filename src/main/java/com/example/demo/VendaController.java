package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaRepository vendaRepository;

    @GetMapping
    public ResponseEntity<?> listarVendas() {
        try {
            List<Venda> vendas = vendaRepository.findAll();
            return ResponseEntity.ok(vendas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar vendas: " + e.getMessage());
        } finally {
            System.out.println("Operação de listagem de vendas finalizada.");
        }
    }

    @PostMapping
    public ResponseEntity<?> registrarVenda(@RequestBody Venda novaVenda) {
        try {
            if (novaVenda.getDataVenda() == null || novaVenda.getDataVenda().isBlank()) {
                novaVenda.setDataVenda(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            if (novaVenda.getStatus() == null || novaVenda.getStatus().isBlank()) {
                novaVenda.setStatus("Concluído");
            }
            Venda salva = vendaRepository.save(novaVenda);
            return ResponseEntity.status(HttpStatus.CREATED).body(salva);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao registrar venda: " + e.getMessage());
        } finally {
            System.out.println("Operação de registro de venda finalizada.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarVenda(@PathVariable Integer id, @RequestBody Venda vendaAtualizada) {
        try {
            Optional<Venda> vendaOpt = vendaRepository.findById(id);

            if (vendaOpt.isPresent()) {
                Venda venda = vendaOpt.get();
                venda.setCliente(vendaAtualizada.getCliente());
                venda.setProduto(vendaAtualizada.getProduto());
                venda.setQuantidade(vendaAtualizada.getQuantidade());
                venda.setValorTotal(vendaAtualizada.getValorTotal());
                if (vendaAtualizada.getDataVenda() != null && !vendaAtualizada.getDataVenda().isBlank()) {
                    venda.setDataVenda(vendaAtualizada.getDataVenda());
                }
                if (vendaAtualizada.getStatus() != null && !vendaAtualizada.getStatus().isBlank()) {
                    venda.setStatus(vendaAtualizada.getStatus());
                }
                Venda atualizada = vendaRepository.save(venda);
                return ResponseEntity.ok(atualizada);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Venda não encontrada.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar venda: " + e.getMessage());
        } finally {
            System.out.println("Operação de atualização de venda finalizada.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagarVenda(@PathVariable Integer id) {
        try {
            if (!vendaRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Venda não encontrada.");
            }

            vendaRepository.deleteById(id);
            return ResponseEntity.ok("Venda removida com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao apagar venda: " + e.getMessage());
        } finally {
            System.out.println("Operação de exclusão de venda finalizada.");
        }
    }
}