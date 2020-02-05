package com.gft.brunoyoshioka.minhasFinancas.model.repository;

import com.gft.brunoyoshioka.minhasFinancas.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // método para retornar Optional (pq ele pode existir ou não)
    // select * from email where email = email
    //Optional<Usuario> findByEmail(String email);

    // select * from usuario where exists ()
    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);
}
