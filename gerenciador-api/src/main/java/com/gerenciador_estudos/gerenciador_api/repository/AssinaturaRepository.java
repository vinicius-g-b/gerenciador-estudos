package com.gerenciador_estudos.gerenciador_api.repository;

import com.gerenciador_estudos.gerenciador_api.model.Assinatura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssinaturaRepository extends JpaRepository<Assinatura, Long> {
    // Só de herdar o JpaRepository, o Spring já cria todos os métodos básicos de banco de dados!
}