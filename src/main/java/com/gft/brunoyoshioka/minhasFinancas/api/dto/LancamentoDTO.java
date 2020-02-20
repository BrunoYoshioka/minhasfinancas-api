package com.gft.brunoyoshioka.minhasFinancas.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor // construtor sem argumentos
@AllArgsConstructor // construtor com argumentos
public class LancamentoDTO {
    private Long id;
    private String descricao;
    private Integer mes;
    private Integer ano;
    private BigDecimal valor;
    private Long usuario;
    private String tipo;
    private String status;
}
