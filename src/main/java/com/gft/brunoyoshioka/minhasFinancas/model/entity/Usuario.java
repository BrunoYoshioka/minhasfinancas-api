package com.gft.brunoyoshioka.minhasFinancas.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "usuario", schema = "financas")
@Builder // precisa do construtor com argumentos
@Data
@NoArgsConstructor // garantir construtor vazio
@AllArgsConstructor // garantir construtor com argumentos
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    @JsonIgnore
    private String senha;
}