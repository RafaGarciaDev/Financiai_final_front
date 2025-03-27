package org.example.financiai_final_front.financiai.util;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import org.financiai.dao.ClienteDAO;
import org.example.financiai_final_front.financiai.model.entities.Cliente;

public class ClientesHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String requestBody = new String(exchange.getRequestBody().readAllBytes());

            // Parseia os dados (simplificado)
            String nome = extrairCampo(requestBody, "nome");
            String cpf = extrairCampo(requestBody, "cpf");
            double renda = Double.parseDouble(extrairCampo(requestBody, "renda"));

            // Cadastra o cliente
            Cliente cliente = new Cliente(nome, cpf, renda);
            ClienteDAO clienteDAO = new ClienteDAO();
            clienteDAO.adicionarCliente(cliente);

            String response = "Cliente cadastrado com sucesso!";
            exchange.sendResponseHeaders(201, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private String extrairCampo(String json, String campo) {
        // Mesma lógica simplificada
        return json.split(campo + "\":")[1].split(",")[0].replace("\"", "").trim();
    }
}
