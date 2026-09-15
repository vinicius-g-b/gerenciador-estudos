# 📚 Gerenciador de Estudos Full-Stack

Um sistema completo para gerenciamento de assinaturas e materiais de estudo, construído com arquitetura Client-Server separada. 

Este projeto demonstra a integração de uma interface desktop rica consumindo uma API RESTful, com banco de dados conteinerizado.

## 📄 Documentação de Arquitetura
Para uma visão detalhada das decisões técnicas e fluxo de dados, acesse o documento abaixo:
👉 **[Ver Documentação de Arquitetura (PDF)](./Arquitetura_Gerenciador_Estudos.pdf)**

---

## 🛠️ Tecnologias Utilizadas

**Backend & Infraestrutura:**
* **Java 21 & Spring Boot:** Criação da API RESTful.
* **Spring Data JPA & Hibernate:** ORM e persistência de dados.
* **PostgreSQL:** Banco de dados relacional.
* **Docker & Docker Compose:** Orquestração e isolamento do banco de dados.

**Frontend (Client):**
* **JavaFX:** Interface gráfica (GUI).
* **HttpClient (Java 11+):** Comunicação assíncrona com a API.
* **Gson:** Serialização/Desserialização de JSON.

---

## 🚀 Como executar o projeto localmente

### 1. Subir o Banco de Dados
Na raiz do projeto, execute o Docker Compose para iniciar o PostgreSQL:
```bash
docker compose up -d
2. Iniciar a API (Spring Boot)
Navegue até a pasta gerenciador-api e rode a aplicação pela sua IDE ou via Maven:

Bash
./mvnw spring-boot:run
3. Iniciar a Interface (JavaFX)
Em um novo terminal, navegue até a pasta gerenciador-ui e execute:

Bash
mvn clean compile javafx:run