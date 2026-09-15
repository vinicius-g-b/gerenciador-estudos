package com.gerenciador_estudos.gerenciador_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "assinaturas") // Nome exato da tabela no banco
public class Assinatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plataforma;
    private String tipoPlano; // O JPA traduz automaticamente para tipo_plano
    private LocalDate dataInicio;
    private LocalDate dataVencimento;

    // Construtor vazio (obrigatório para o JPA)
    public Assinatura() {
    }

    // Getters e Setters (Para acessar e modificar os dados)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlataforma() { return plataforma; }
    public void setPlataforma(String plataforma) { this.plataforma = plataforma; }

    public String getTipoPlano() { return tipoPlano; }
    public void setTipoPlano(String tipoPlano) { this.tipoPlano = tipoPlano; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
}