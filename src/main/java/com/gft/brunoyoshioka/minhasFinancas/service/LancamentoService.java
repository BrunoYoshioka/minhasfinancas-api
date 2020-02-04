package com.gft.brunoyoshioka.minhasFinancas.service;

import com.gft.brunoyoshioka.minhasFinancas.model.entity.Lancamento;
import com.gft.brunoyoshioka.minhasFinancas.model.enums.StatusLancamento;

import java.util.List;

public interface LancamentoService {
    Lancamento salvar (Lancamento lancamento);
    Lancamento atualizar (Lancamento lancamento);
    void deletar (Lancamento lancamento);
    List<Lancamento> buscar (Lancamento lancamentoFiltro);
    void atualizarStatus (Lancamento lancamento, StatusLancamento statusLancamento);
}
