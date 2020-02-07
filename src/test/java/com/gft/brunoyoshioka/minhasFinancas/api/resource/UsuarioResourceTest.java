package com.gft.brunoyoshioka.minhasFinancas.api.resource;

import com.gft.brunoyoshioka.minhasFinancas.api.controller.UsuarioController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.gft.brunoyoshioka.minhasFinancas.api.dto.UsuarioDTO;
import com.gft.brunoyoshioka.minhasFinancas.exception.ErroAutentication;
import com.gft.brunoyoshioka.minhasFinancas.exception.RegraNegocioException;
import com.gft.brunoyoshioka.minhasFinancas.model.entity.Usuario;
import com.gft.brunoyoshioka.minhasFinancas.service.LancamentoService;
import com.gft.brunoyoshioka.minhasFinancas.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioController.class) // essa anoteichon, vae faze com que ele suba contexto REST
@AutoConfigureMockMvc // serve para mapear acesso
public class UsuarioResourceTest {
    // variável constante
    static final String API = "api/usuarios";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    UsuarioService usuarioService;

    @MockBean
    LancamentoService lancamentoService;

    @Test
    public void deveAutenticarUmUsuario() throws Exception{
        // cenário
        String email = "usuario@email.com";
        String senha = "123";
        // crio 2 objetos que representa json
        UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
        Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
        Mockito.when( usuarioService.autenticar(email, senha) ).thenReturn(usuario);
        // transformar dto em json
        String json = new ObjectMapper().writeValueAsString(dto);

        //execução e verificação
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders // quando fizer este post,
                .post( API.concat("/autenticar") )
                .accept( JSON ) // aceitar objeto do tipo json
                .contentType( JSON ) // enviar conteúdo
                .content( json );

        mvc
                .perform(requestBuilder)
                .andExpect( MockMvcResultMatchers.status().isOk() ) // espero que esteja ok e que retorna o json
                .andExpect( MockMvcResultMatchers.jsonPath("id").value(usuario.getId()) )
                .andExpect( MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()) )
                .andExpect( MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()) )
        ;
    }

    @Test
    public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception{
        //cenario
        String email = "usuario@email.com";
        String senha = "123";

        UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
        Mockito.when( usuarioService.autenticar(email, senha) ).thenThrow(ErroAutentication.class);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( API.concat("/autenticar") )
                .accept( JSON )
                .contentType( JSON )
                .content(json);


        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isBadRequest()  );

        ;
    }

    @Test
    public void deveCriarUmNovoUsuario() throws Exception {
        //cenario
        String email = "usuario@email.com";
        String senha = "123";

        UsuarioDTO dto = UsuarioDTO.builder().email("usuario@email.com").senha("123").build();
        Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();

        Mockito.when( usuarioService.salvarUsuario(Mockito.any(Usuario.class)) ).thenReturn(usuario);
        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( API  )
                .accept( JSON )
                .contentType( JSON )
                .content(json);


        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isCreated()  )
                .andExpect( MockMvcResultMatchers.jsonPath("id").value(usuario.getId())  )
                .andExpect( MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome())  )
                .andExpect( MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail())  )

        ;

    }

    @Test
    public void deveRetornarBadRequestAoTentarCriarUmUsuarioInvalido() throws Exception {
        //cenario
        String email = "usuario@email.com";
        String senha = "123";

        UsuarioDTO dto = UsuarioDTO.builder().email("usuario@email.com").senha("123").build();

        Mockito.when( usuarioService.salvarUsuario(Mockito.any(Usuario.class)) ).thenThrow(RegraNegocioException.class);
        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( API  )
                .accept( JSON )
                .contentType( JSON )
                .content(json);


        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isBadRequest()  );

        ;

    }

    @Test
    public void deveObterOSaldoDoUsuario() throws Exception {

        //cenário

        BigDecimal saldo = BigDecimal.valueOf(10);
        Usuario usuario = Usuario.builder().id(1l).email("usuario@email.com").senha( "123").build();
        Mockito.when(usuarioService.obterPorId(1l)).thenReturn(Optional.of(usuario));
        Mockito.when(lancamentoService.obterSaldoPorUsuario(1l)).thenReturn(saldo);


        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get( API.concat("/1/saldo")  )
                .accept( JSON )
                .contentType( JSON );
        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.content().string("10") );

    }

    @Test
    public void deveRetornarResourceNotFoundQuandoUsuarioNaoExisteParaObterOSaldo() throws Exception {

        //cenário
        Mockito.when(usuarioService.obterPorId(1l)).thenReturn(Optional.empty());


        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get( API.concat("/1/saldo")  )
                .accept( JSON )
                .contentType( JSON );
        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isNotFound() );

    }
}