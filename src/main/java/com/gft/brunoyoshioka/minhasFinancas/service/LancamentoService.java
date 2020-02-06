package com.gft.brunoyoshioka.minhasFinancas.service;

import com.gft.brunoyoshioka.minhasFinancas.model.entity.Lancamento;
import com.gft.brunoyoshioka.minhasFinancas.model.enums.StatusLancamento;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public interface LancamentoService {
    Lancamento salvar (Lancamento lancamento);
    Lancamento atualizar (Lancamento lancamento);
    void deletar (Lancamento lancamento);
    List<Lancamento> buscar (Lancamento lancamentoFiltro);
    void atualizarStatus (Lancamento lancamento, StatusLancamento statusLancamento);
    void validar(Lancamento lancamento);
    Optional<Lancamento> obterPorId(Long id);
    BigDecimal obterSaldoPorUsuario(Long id);
}
