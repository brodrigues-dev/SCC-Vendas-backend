package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar usuários: " + e.getMessage());
        } finally {
            System.out.println("Operação de listagem de usuários finalizada.");
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
        try {
            if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("E-mail já cadastrado.");
            }

            String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
            usuario.setSenha(senhaCriptografada);

            Usuario salvo = usuarioRepository.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar usuário: " + e.getMessage());
        } finally {
            System.out.println("Processamento de cadastro finalizado.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario dadosLogin) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(dadosLogin.getEmail());

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                if (passwordEncoder.matches(dadosLogin.getSenha(), usuario.getSenha())) {
                    return ResponseEntity.ok(usuario);
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("E-mail ou senha inválidos.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao realizar login: " + e.getMessage());
        } finally {
            System.out.println("Tentativa de login finalizada.");
        }
    }
}