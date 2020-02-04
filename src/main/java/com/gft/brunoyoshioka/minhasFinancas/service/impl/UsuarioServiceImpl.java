package com.gft.brunoyoshioka.minhasFinancas.service.impl;

import com.gft.brunoyoshioka.minhasFinancas.exception.ErroAutentication;
import com.gft.brunoyoshioka.minhasFinancas.exception.RegraNegocioException;
import com.gft.brunoyoshioka.minhasFinancas.model.entity.Usuario;
import com.gft.brunoyoshioka.minhasFinancas.model.repository.UsuarioRepository;
import com.gft.brunoyoshioka.minhasFinancas.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // as assinaturas dos métodos
    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if(!usuario.isPresent()){ // caso não esteja presente
            throw new ErroAutentication("Usuário não encontrado para o email informado");
        }

        if(!usuario.get().getSenha().equals(senha)){ // se a senha informada não for = a senha no banco
            throw new ErroAutentication("Senha inválida");
        }

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return usuarioRepository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = usuarioRepository.existsByEmail(email);
        if(existe){
            throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
        }
    }
}
