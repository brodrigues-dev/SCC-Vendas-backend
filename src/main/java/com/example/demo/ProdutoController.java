package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public ResponseEntity<?> listarProdutos() {
        try {
            if (produtoRepository.count() == 0) {
                produtoRepository.save(new Produto("Notebook", 3500.00));
                produtoRepository.save(new Produto("Mouse Sem Fio", 85.50));
            }
            List<Produto> produtos = produtoRepository.findAll();
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar produtos: " + e.getMessage());
        } finally {
            System.out.println("Operação de listagem de produtos finalizada.");
        }
    }

    @PostMapping
    public ResponseEntity<?> adicionarProduto(@RequestBody Produto novoProduto) {
        try {
            Produto salvo = produtoRepository.save(novoProduto);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar produto: " + e.getMessage());
        } finally {
            System.out.println("Operação de cadastro de produto finalizada.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(@PathVariable Integer id, @RequestBody Produto produtoAtualizado) {
        try {
            Optional<Produto> produtoOpt = produtoRepository.findById(id);

            if (produtoOpt.isPresent()) {
                Produto produto = produtoOpt.get();
                produto.setNome(produtoAtualizado.getNome());
                produto.setPreco(produtoAtualizado.getPreco());
                Produto atualizado = produtoRepository.save(produto);
                return ResponseEntity.ok(atualizado);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produto não encontrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar produto: " + e.getMessage());
        } finally {
            System.out.println("Operação de atualização de produto finalizada.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagarProduto(@PathVariable Integer id) {
        try {
            if (!produtoRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Produto não encontrado.");
            }

            produtoRepository.deleteById(id);
            return ResponseEntity.ok("Produto removido com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao apagar produto: " + e.getMessage());
        } finally {
            System.out.println("Operação de exclusão de produto finalizada.");
        }
    }
}