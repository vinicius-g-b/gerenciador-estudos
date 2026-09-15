package com.gerenciador_estudos.gerenciador_api.controller;

import com.gerenciador_estudos.gerenciador_api.model.Assinatura;
import com.gerenciador_estudos.gerenciador_api.repository.AssinaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/assinaturas") // Define que a URL base será http://localhost:8080/assinaturas
public class AssinaturaController {

    @Autowired
    private AssinaturaRepository repository;

    @GetMapping // Define que este método será chamado ao fazer uma requisição GET
    public List<Assinatura> listarTodos() {
        // Vai no banco, busca todas as assinaturas e converte automaticamente para JSON
        return repository.findAll();
    }
    @PostMapping
    public Assinatura criar(@RequestBody Assinatura novaAssinatura) {
        // O @RequestBody pega o JSON que vamos enviar e transforma no objeto Java
        return repository.save(novaAssinatura);
    }
    @PutMapping("/{id}") // A URL será ex: /assinaturas/1
    public Assinatura atualizar(@PathVariable Long id, @RequestBody Assinatura assinaturaAtualizada) {
        // Busca a assinatura antiga no banco, atualiza os campos e salva por cima
        return repository.findById(id).map(assinatura -> {
            assinatura.setPlataforma(assinaturaAtualizada.getPlataforma());
            assinatura.setTipoPlano(assinaturaAtualizada.getTipoPlano());
            assinatura.setDataInicio(assinaturaAtualizada.getDataInicio());
            assinatura.setDataVencimento(assinaturaAtualizada.getDataVencimento());
            return repository.save(assinatura);
        }).orElse(null); 
    }
    @DeleteMapping("/{id}") // A URL será ex: /assinaturas/1
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}