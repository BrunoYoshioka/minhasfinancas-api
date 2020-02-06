package com.gft.brunoyoshioka.minhasFinancas.api.controller;

import com.gft.brunoyoshioka.minhasFinancas.api.dto.UsuarioDTO;
import com.gft.brunoyoshioka.minhasFinancas.exception.ErroAutentication;
import com.gft.brunoyoshioka.minhasFinancas.exception.RegraNegocioException;
import com.gft.brunoyoshioka.minhasFinancas.model.entity.Usuario;
import com.gft.brunoyoshioka.minhasFinancas.service.LancamentoService;
import com.gft.brunoyoshioka.minhasFinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    /*@GetMapping("/")
    public String helloWord(){
        return "Hello World! (Olá mundo!)";
    }*/

    private final UsuarioService usuarioService;

    private final LancamentoService lancamentoService;

    /*public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }*/

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO usuarioDTO){
        try{
            Usuario usuarioAutenticado = usuarioService.autenticar(usuarioDTO.getEmail(), usuarioDTO.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (ErroAutentication e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity salvar (@RequestBody UsuarioDTO dto){
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha()).build();
        try{
            Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity obterSaldo (@PathVariable("id") Long id){
        Optional<Usuario> usuario = usuarioService.obterPorId(id);

        if(!usuario.isPresent()){
            return new ResponseEntity( HttpStatus.NOT_FOUND);
        }

        BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
        return ResponseEntity.ok(saldo);
    }
}
