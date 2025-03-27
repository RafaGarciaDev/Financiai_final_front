package org.example.financiai_final_front.financiai.util;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import org.example.financiai_final_front.financiai.controller.FinanciamentoController;
import org.example.financiai_final_front.financiai.model.enums.TipoAmortizacao;

public class SimulacaoHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            // Lê o corpo da requisição (ex.: JSON com dados da simulação)
            String requestBody = new String(exchange.getRequestBody().readAllBytes());

            // Parseia os dados (simplificado - use uma lib como GSON se preferir)
            double renda = Double.parseDouble(extrairCampo(requestBody, "renda"));
            double valorImovel = Double.parseDouble(extrairCampo(requestBody, "valorImovel"));
            TipoAmortizacao tipo = TipoAmortizacao.valueOf(extrairCampo(requestBody, "tipoAmortizacao"));

            // Chama o controlador existente
            FinanciamentoController.calcularFinanciamento(
                    renda, valorImovel, 0, 10.0, 120, tipo
            );

            // Resposta (simulada)
            String response = "Simulação realizada com sucesso!";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Método não permitido
        }
    }

    private String extrairCampo(String json, String campo) {
        // Implementação simplificada (substitua por um parser JSON real)
        return json.split(campo + "\":")[1].split(",")[0].replace("\"", "").trim();
    }
}
