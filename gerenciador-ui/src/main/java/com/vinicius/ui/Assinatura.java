package com.vinicius.ui;

public class Assinatura {
    private Long id;
    private String plataforma;
    private String tipoPlano;
    private String dataInicio;
    private String dataVencimento;

    // NOVO: Construtor para criar uma nova Assinatura a partir do formulário
    public Assinatura(String plataforma, String tipoPlano, String dataInicio, String dataVencimento) {
        this.plataforma = plataforma;
        this.tipoPlano = tipoPlano;
        this.dataInicio = dataInicio;
        this.dataVencimento = dataVencimento;
    }

    // Getters (obrigatórios para a tabela ler os dados)
    public Long getId() { return id; }
    public String getPlataforma() { return plataforma; }
    public String getTipoPlano() { return tipoPlano; }
    public String getDataInicio() { return dataInicio; }
    public String getDataVencimento() { return dataVencimento; }
}