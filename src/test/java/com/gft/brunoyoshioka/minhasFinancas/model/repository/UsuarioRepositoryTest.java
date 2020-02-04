package com.gft.brunoyoshioka.minhasFinancas.model.repository;

import com.gft.brunoyoshioka.minhasFinancas.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest // essa anotação cria uma instancia do banco de dados em memória e ao finalizar, encerra a base de dados
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // sobresbre qualquer configuração que tenha feito no test
public class UsuarioRepositoryTest {
    // primeiro teste de integração

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        // cenário
        Usuario usuario = criarUsuario();
        //usuarioRepository.save(usuario); // salvei na base de dados
        testEntityManager.persist(usuario);

        // ação / execução
        boolean result = usuarioRepository.existsByEmail("usuario@email.com"); // verificar se existe email, que no caso deve retornar true

        // verificação
        Assertions.assertThat(result).isTrue(); // verificar se o resultado for verdade
    }

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastrado(){
        // cenario
        // usuarioRepository.deleteAll();

        // ação / execução
        boolean result = usuarioRepository.existsByEmail("naoexiste@email.com");

        // verificação
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void devePersistirUmUsuarioNaBaseDeDados(){
        // cenário
        Usuario usuario = criarUsuario();

        // ação
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        // verificação
        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    public void deveBuscarUmUsuarioPorEmail(){
        // cenário
        Usuario usuario = criarUsuario();
        testEntityManager.persist(usuario);

        // verificação
        Optional<Usuario> result = usuarioRepository.findByEmail("usuario@email.com");

        Assertions.assertThat( result.isPresent() ).isTrue(); // para saber se o resultado esta presente
    }

    @Test
    public void deveBuscarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase(){
        // verificação
        Optional<Usuario> result = usuarioRepository.findByEmail("usuario@email.com");

        Assertions.assertThat( result.isPresent() ).isFalse(); // para saber se o resultado esta presente
    }

    public static Usuario criarUsuario(){
        return Usuario
                .builder()
                .nome("usuario")
                .email("usuario@email.com")
                .senha("senha")
                .build();
    }
}
