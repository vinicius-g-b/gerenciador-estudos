package com.vinicius.ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Main extends Application {

    private TableView<Assinatura> tabela = new TableView<>();
    private Gson gson = new Gson();
    
    // Variável para guardar o ID de quem estamos editando/deletando
    private Long idSelecionado = null; 

    // Campos de texto transformados em variáveis globais para podermos acessá-los em vários métodos
    private TextField txtPlataforma = new TextField();
    private TextField txtPlano = new TextField();
    private TextField txtInicio = new TextField();
    private TextField txtVencimento = new TextField();

    @Override
    public void start(Stage palco) {
        configurarTabela();

        txtPlataforma.setPromptText("Plataforma");
        txtPlano.setPromptText("Plano (ex: Anual)");
        txtInicio.setPromptText("Início (AAAA-MM-DD)");
        txtVencimento.setPromptText("Vencimento (AAAA-MM-DD)");

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Button btnDeletar = new Button("Deletar");
        btnDeletar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        btnDeletar.setDisable(true); // Começa desativado até clicarmos em alguém

        // Lógica do clique na Tabela
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, selecaoAntiga, selecaoNova) -> {
            if (selecaoNova != null) {
                // Preenche o formulário com os dados da linha clicada
                idSelecionado = selecaoNova.getId();
                txtPlataforma.setText(selecaoNova.getPlataforma());
                txtPlano.setText(selecaoNova.getTipoPlano());
                txtInicio.setText(selecaoNova.getDataInicio());
                txtVencimento.setText(selecaoNova.getDataVencimento());
                btnDeletar.setDisable(false); // Ativa o botão deletar
            }
        });

        // Lógica do botão Salvar (Decide se é POST ou PUT)
        btnSalvar.setOnAction(e -> {
            Assinatura obj = new Assinatura(txtPlataforma.getText(), txtPlano.getText(), txtInicio.getText(), txtVencimento.getText());
            
            if (idSelecionado == null) {
                enviarParaApi(obj, "POST", null); // Cria novo
            } else {
                enviarParaApi(obj, "PUT", idSelecionado); // Atualiza existente
            }
        });

        // Lógica do botão Deletar
        btnDeletar.setOnAction(e -> {
            if (idSelecionado != null) {
                enviarParaApi(null, "DELETE", idSelecionado);
            }
        });

        Button btnLimpar = new Button("Limpar Seleção");
        btnLimpar.setOnAction(e -> limparFormulario(btnDeletar));

        HBox botoes = new HBox(10, btnSalvar, btnDeletar, btnLimpar);
        HBox formulario = new HBox(10, txtPlataforma, txtPlano, txtInicio, txtVencimento);
        VBox layoutBase = new VBox(15, formulario, botoes, tabela);
        layoutBase.setStyle("-fx-padding: 20px;");

        Scene cena = new Scene(layoutBase, 900, 500);
        palco.setTitle("Gerenciador de Assinaturas (CRUD Completo)");
        palco.setScene(cena);
        palco.show();

        buscarDadosDaApi();
    }

    private void limparFormulario(Button btnDeletar) {
        idSelecionado = null;
        txtPlataforma.clear();
        txtPlano.clear();
        txtInicio.clear();
        txtVencimento.clear();
        tabela.getSelectionModel().clearSelection();
        btnDeletar.setDisable(true);
    }

    // --- MÉTODOS DE COMUNICAÇÃO COM A API ---

    private void buscarDadosDaApi() {
        try {
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest requisicao = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/assinaturas"))
                    .GET().build();

            cliente.sendAsync(requisicao, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(resposta -> {
                        Platform.runLater(() -> {
                            if (resposta.statusCode() == 200) {
                                Type tipo = new TypeToken<List<Assinatura>>(){}.getType();
                                List<Assinatura> dados = gson.fromJson(resposta.body(), tipo);
                                tabela.setItems(FXCollections.observableArrayList(dados));
                            }
                        });
                    });
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Método universal que faz POST, PUT e DELETE dependendo dos parâmetros passados
    private void enviarParaApi(Assinatura dados, String metodo, Long id) {
        try {
            HttpClient cliente = HttpClient.newHttpClient();
            String url = "http://localhost:8080/assinaturas" + (id != null ? "/" + id : "");
            
            HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url));
            
            if (metodo.equals("POST")) {
                builder.header("Content-Type", "application/json")
                       .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(dados)));
            } else if (metodo.equals("PUT")) {
                builder.header("Content-Type", "application/json")
                       .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(dados)));
            } else if (metodo.equals("DELETE")) {
                builder.DELETE();
            }

            cliente.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString())
                   .thenAccept(resposta -> {
                       Platform.runLater(() -> {
                           buscarDadosDaApi(); // Recarrega a tabela
                           // Não podemos acessar o btnDeletar aqui sem passar por parâmetro,
                           // então simplesmente limpamos os textos. O ideal seria chamar o limparFormulario().
                           idSelecionado = null;
                           txtPlataforma.clear(); txtPlano.clear(); txtInicio.clear(); txtVencimento.clear();
                       });
                   });
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void configurarTabela() {
        TableColumn<Assinatura, String> colPlat = new TableColumn<>("Plataforma");
        colPlat.setCellValueFactory(new PropertyValueFactory<>("plataforma")); colPlat.setPrefWidth(200);

        TableColumn<Assinatura, String> colPlano = new TableColumn<>("Plano");
        colPlano.setCellValueFactory(new PropertyValueFactory<>("tipoPlano")); colPlano.setPrefWidth(200);

        TableColumn<Assinatura, String> colInicio = new TableColumn<>("Início");
        colInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio")); colInicio.setPrefWidth(120);

        TableColumn<Assinatura, String> colVenc = new TableColumn<>("Vencimento");
        colVenc.setCellValueFactory(new PropertyValueFactory<>("dataVencimento")); colVenc.setPrefWidth(120);

        tabela.getColumns().addAll(colPlat, colPlano, colInicio, colVenc);
    }

    public static void main(String[] args) { launch(args); }
}