package com.gft.brunoyoshioka.minhasFinancas.model.repository;

import com.gft.brunoyoshioka.minhasFinancas.model.entity.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
}
