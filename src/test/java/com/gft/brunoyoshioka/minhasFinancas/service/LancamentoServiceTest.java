package com.gft.brunoyoshioka.minhasFinancas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gft.brunoyoshioka.minhasFinancas.exception.RegraNegocioException;
import com.gft.brunoyoshioka.minhasFinancas.model.entity.Lancamento;
import com.gft.brunoyoshioka.minhasFinancas.model.entity.Usuario;
import com.gft.brunoyoshioka.minhasFinancas.model.enums.StatusLancamento;
import com.gft.brunoyoshioka.minhasFinancas.model.enums.TipoLancamento;
import com.gft.brunoyoshioka.minhasFinancas.model.repository.LancamentoRepository;
import com.gft.brunoyoshioka.minhasFinancas.model.repository.LancamentoRepositoryTest;
import com.gft.brunoyoshioka.minhasFinancas.service.impl.LancamentoServiceImpl;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
/*@DataJpaTest // para teste de integração
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)*/
@ActiveProfiles("test")
public class LancamentoServiceTest {
    @SpyBean // vai ser a classe que de teste
    LancamentoServiceImpl lancamentoServiceImpl;

    @MockBean
    LancamentoRepository lancamentoRepository;

    @Test
    public void deveSalvarUmLancamento(){
        // cenário
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doNothing().when(lancamentoServiceImpl).validar(lancamentoASalvar); // dessa forma ele nao vai lancar erro quando chamar validar

        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(lancamentoRepository.save(lancamentoASalvar))./*Quando fizer isso ele vai*/thenReturn(lancamentoSalvo);

        // execução
        Lancamento lancamento = lancamentoServiceImpl.salvar(lancamentoASalvar);

        // verificação
        Assertions.assertThat( lancamento.getId() ).isEqualTo(lancamentoSalvo.getId());
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao(){
        // cenário
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doThrow( RegraNegocioException.class ).when(lancamentoServiceImpl).validar(lancamentoASalvar);

        // execução e verificação
        Assertions.catchThrowableOfType( () -> lancamentoServiceImpl.salvar(lancamentoASalvar), RegraNegocioException.class );

        // verifico que o meu repository nunca chamou o método save
        Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamentoASalvar);
    }

    @Test
    public void deveAtualizarUmLancamento(){
        // cenário
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);

        Mockito.doNothing().when(lancamentoServiceImpl).validar(lancamentoSalvo); // dessa forma ele nao vai lancar erro quando chamar validar

        Mockito.when(lancamentoRepository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        // execução
        lancamentoServiceImpl.atualizar(lancamentoSalvo);

        // verificação
        Mockito.verify(lancamentoRepository, Mockito.times(1)).save(lancamentoSalvo);
    }

    @Test
    public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo(){
        // cenário
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();

        // execução e verificação
        Assertions.catchThrowableOfType( () -> lancamentoServiceImpl.atualizar(lancamentoASalvar), NullPointerException.class );
        Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamentoASalvar);
    }

    @Test
    public void deveDeletarUmLancamento(){
        // cenário
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        lancamentoASalvar.setId(1l);

        // execução
        lancamentoServiceImpl.deletar(lancamentoASalvar);

        // verificação
        Mockito.verify( lancamentoRepository ).delete(lancamentoASalvar);
    }

    @Test
    public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo(){
        // cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();

        // execução
        Assertions.catchThrowableOfType( () -> lancamentoServiceImpl.deletar(lancamento), NullPointerException.class ); // capturar erro

        // verificação
        Mockito.verify( lancamentoRepository, Mockito.never() ).delete(lancamento);
    }

    @Test
    public void deveFiltrarLancamentos(){
        // cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);

        List<Lancamento> lista = Arrays.asList(lancamento);
        Mockito.when(lancamentoRepository.findAll(Mockito.any(Example.class))).thenReturn(lista);

        // execução
        List<Lancamento> resultado = lancamentoServiceImpl.buscar(lancamento);

        // verificações
        Assertions
                .assertThat(resultado)
                .isNotEmpty()
                .hasSize(1)
                .contains(lancamento);
    }

    @Test
    public void deveAtualizarOsStatusDeUmLancamento(){
        // cenário
        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(1l);
        lancamento.setStatus(StatusLancamento.PENDENTE);

        StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
        Mockito.doReturn(lancamento).when(lancamentoServiceImpl).atualizar(lancamento); // não faça que quando ele for atualizar status ele não chama de fato o método atualizar

        // execução
        lancamentoServiceImpl.atualizarStatus(lancamento, novoStatus);

        // verificação
        Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
        Mockito.verify(lancamentoServiceImpl).atualizar(lancamento);
    }

    @Test
    public void deveObterUmLancamentoPorId(){
        // cenário
        Long id = 1l;

        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);

        Mockito.when(lancamentoRepository.findById(id)).thenReturn(Optional.of(lancamento));

        // execução
        Optional<Lancamento> resultado = lancamentoServiceImpl.obterPorId(id);

        // verificação
        Assertions.assertThat(resultado.isPresent()).isTrue(); // verifique que ele deve obter resultado
    }

    @Test
    public void deveRetornarVazioQuandoOLancamentoNaoExiste(){
        // cenário
        Long id = 1l;

        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);

        Mockito.when(lancamentoRepository.findById(id)).thenReturn( Optional.empty() );

        // execução
        Optional<Lancamento> resultado = lancamentoServiceImpl.obterPorId(id);

        // verificação
        Assertions.assertThat(resultado.isPresent()).isFalse(); // verifique que ele nao deve obter resultado
    }

    @Test
    public void deveLancarErrosAoValidarUmLancamento(){
        Lancamento lancamento = new Lancamento(); // precisa estar vazio

        Throwable erro = Assertions.catchThrowable( () -> lancamentoServiceImpl.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");

        lancamento.setDescricao("");

        erro = Assertions.catchThrowable( () -> lancamentoServiceImpl.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");

        lancamento.setDescricao("Salario");

        erro = Assertions.catchThrowable( () -> lancamentoServiceImpl.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

        lancamento.setAno(0);

        erro = catchThrowable( () -> lancamentoServiceImpl.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

        lancamento.setAno(13);

        erro = catchThrowable( () -> lancamentoServiceImpl.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

        lancamento.setMes(1);

        erro = catchThrowable( () -> lancamentoServiceImpl.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

        lancamento.setAno(202);

        erro = catchThrowable( () -> lancamentoServiceImpl.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

        lancamento.setAno(2020);

        erro = catchThrowable( () -> lancamentoServiceImpl.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");

        lancamento.setUsuario(new Usuario());

        erro = catchThrowable( () -> lancamentoServiceImpl.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");

        lancamento.getUsuario().setId(1l);

        erro = catchThrowable( () -> lancamentoServiceImpl.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");

        lancamento.setValor(BigDecimal.ZERO);

        erro = catchThrowable( () -> lancamentoServiceImpl.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");

        lancamento.setValor(BigDecimal.valueOf(1));

        erro = catchThrowable( () -> lancamentoServiceImpl.validar(lancamento) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de Lançamento.");
    }
}
