package com.gft.brunoyoshioka.minhasFinancas.service;

import com.gft.brunoyoshioka.minhasFinancas.model.entity.Usuario;

public interface UsuarioService {
    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email);
}
