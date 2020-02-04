package com.gft.brunoyoshioka.minhasFinancas.service;

import com.gft.brunoyoshioka.minhasFinancas.exception.ErroAutentication;
import com.gft.brunoyoshioka.minhasFinancas.exception.RegraNegocioException;
import com.gft.brunoyoshioka.minhasFinancas.model.entity.Usuario;
import com.gft.brunoyoshioka.minhasFinancas.model.repository.UsuarioRepository;
import com.gft.brunoyoshioka.minhasFinancas.service.impl.UsuarioServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl usuarioServiceImpl;

    @MockBean
    UsuarioRepository usuarioRepository;

//    @Before // será executado antes dos metodos abaixo
//    public void setUp(){
//        //usuarioRepository = Mockito.mock(UsuarioRepository.class);
//        usuarioService = new UsuarioServiceImpl(usuarioRepository);
//    }

    @Test(expected = Test.None.class) // espero que não lance erro
    public void deveSalvarUmUsuario(){
        // cenário
        Mockito.doNothing() // não faça nada
        .when(usuarioServiceImpl).validarEmail(Mockito.anyString());

        Usuario usuario = Usuario.builder()
                .id(1l)
                .nome("nome")
                .email("email@email.com")
                .senha("senha").build();

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        // ação
        Usuario usuarioSalvo = usuarioServiceImpl.salvarUsuario(new Usuario());

        // verificação
        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
    }

    @Test(expected = RegraNegocioException.class)
    public void naoDeveSalvarUmUsuarioComEmailJaCadastrado(){
        // cenário
        String email ="email@email.com";
        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraNegocioException.class).when(usuarioServiceImpl).validarEmail(email); // quando chamar service, valida email e joga na RegraNegocioException
        // ação
        usuarioServiceImpl.salvarUsuario(usuario);
        // verificação
        Mockito.verify( usuarioRepository, Mockito.never() ).save(usuario); // espero que ele nunca tenha chamado na execução da ação o método de salvar usuario.
    }

    @Test(expected = Test.None.class)
    public void deveAutenticarUmUsuarioComSucesso(){
        // cenário
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
        Mockito.when( usuarioRepository.findByEmail(email) ).thenReturn(Optional.of(usuario)); // quando o repository for na base e executar findByEmail passando o email, irá retornar um Optional com meu usuario

        //ação
        Usuario result = usuarioServiceImpl.autenticar(email, senha);

        // verificação
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado(){
        // cenário
        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty()); // Independente de qualquer email for passar, vai estar sempre retornar vazio
        // ação
        Throwable exception = Assertions.catchThrowable( () -> usuarioServiceImpl.autenticar("email@email.com", "senha") );
        // verificação
        Assertions.assertThat(exception)
                .isInstanceOf(ErroAutentication.class)
                .hasMessage("Usuário não encontrado para o email informado");
    }

    @Test
    public void deveRetornarErroQuandoSenhaNaoBater(){
        // cenário
        Usuario usuario = Usuario.builder().email("email@email.com").senha("senha").build();
        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
        // ação
        Throwable exception = Assertions.catchThrowable( () -> usuarioServiceImpl.autenticar("email@email.com","123") );
        Assertions.assertThat(exception).isInstanceOf(ErroAutentication.class).hasMessage("Senha inválida"); // verificar
    }

    @Test(expected = Test.None.class)
    public void deveValidarEmail(){
        // cenário
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false); // simular que quando chamar o método existsByEmail passando qualquer string por email irá retornar falso

        // ação
        usuarioServiceImpl.validarEmail("usuario@email.com");
    }

    @Test(expected = RegraNegocioException.class)
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
        // cenário
        //Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
        //usuarioRepository.save(usuario);
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        // ação
        usuarioServiceImpl.validarEmail("email@email.com");
    }
}
