package org.example.financiai_final_front.financiai.util;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import org.financiai.dao.ClienteDAO;
import org.financiai.dao.FinanciamentoDAO;
import org.financiai.dao.ImovelDAO;
import org.financiai.dao.ParcelasDAO;
import org.example.financiai_final_front.financiai.model.entities.Cliente;
import org.example.financiai_final_front.financiai.model.entities.Financiamento;
import org.example.financiai_final_front.financiai.model.entities.Imovel;
import org.example.financiai_final_front.financiai.model.entities.Parcelas;
import org.example.financiai_final_front.financiai.model.enums.TipoAmortizacao;
import org.example.financiai_final_front.financiai.model.enums.TipoImovel;
import org.financiai.util.GeradorPDF;
import java.util.List;
import java.util.ArrayList;

public class ContratarHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                // Lê o corpo da requisição (JSON)
                String requestBody = new String(exchange.getRequestBody().readAllBytes());

                // Parseia os dados (simplificado - considere usar GSON)
                String cpfCliente = extrairCampo(requestBody, "cpfCliente");
                double valorImovel = Double.parseDouble(extrairCampo(requestBody, "valorImovel"));
                String tipoImovelStr = extrairCampo(requestBody, "tipoImovel");
                double valorEntrada = Double.parseDouble(extrairCampo(requestBody, "valorEntrada"));
                double taxaJurosAnual = Double.parseDouble(extrairCampo(requestBody, "taxaJurosAnual"));
                int prazo = Integer.parseInt(extrairCampo(requestBody, "prazo"));
                String tipoAmortizacaoStr = extrairCampo(requestBody, "tipoAmortizacao");

                // Converte enums
                TipoImovel tipoImovel = TipoImovel.valueOf(tipoImovelStr.toUpperCase());
                TipoAmortizacao tipoAmortizacao = TipoAmortizacao.valueOf(tipoAmortizacaoStr.toUpperCase());

                // Busca cliente e imóvel (simulados - ajuste conforme sua lógica)
                ClienteDAO clienteDAO = new ClienteDAO();
                Cliente cliente = clienteDAO.buscarClientePorCpf(cpfCliente);
                if (cliente == null) {
                    enviarResposta(exchange, 404, "Cliente não encontrado");
                    return;
                }

                Imovel imovel = new Imovel(tipoImovel, valorImovel);
                ImovelDAO imovelDAO = new ImovelDAO();
                imovelDAO.adicionarImovel(imovel);

                // Calcula valores do financiamento
                double valorFinanciado = valorImovel - valorEntrada;
                double totalPagar = calcularTotalPagar(valorFinanciado, taxaJurosAnual / 12 / 100, prazo, tipoAmortizacao);

                // Cria o financiamento
                Financiamento financiamento = new Financiamento(
                        prazo, taxaJurosAnual, tipoAmortizacao,
                        valorEntrada, valorFinanciado, totalPagar
                );

                // Persiste no banco
                FinanciamentoDAO financiamentoDAO = new FinanciamentoDAO();
                financiamentoDAO.adicionarFinanciamento(financiamento, cliente, imovel);

                // Gera parcelas (apenas as 5 primeiras e últimas, como no Program.java)
                List<Parcelas> parcelas = gerarParcelas(financiamento, tipoAmortizacao, valorFinanciado, taxaJurosAnual);
                ParcelasDAO parcelasDAO = new ParcelasDAO();
                for (Parcelas parcela : parcelas) {
                    parcelasDAO.adicionarParcela(parcela);
                }

                // Gera PDF (opcional)
                GeradorPDF.gerarPDF(financiamento, cliente, imovel, parcelas);

                // Resposta de sucesso
                enviarResposta(exchange, 201, "Financiamento contratado com sucesso! ID: " + financiamento.getFinanciamentoId());

            } catch (Exception e) {
                enviarResposta(exchange, 500, "Erro interno: " + e.getMessage());
            }
        } else {
            enviarResposta(exchange, 405, "Método não permitido");
        }
    }

    // Métodos auxiliares (reaproveite do Program.java)
    private List<Parcelas> gerarParcelas(Financiamento financiamento, TipoAmortizacao tipo, double valorFinanciado, double taxaJurosAnual) {
        // Implementação similar à do Program.java
        // Retorna lista de parcelas (5 primeiras + 5 últimas)
        return new ArrayList<>(); // Ajuste conforme sua lógica
    }

    private double calcularTotalPagar(double valorFinanciado, double taxaJurosMensal, int prazo, TipoAmortizacao tipo) {
        // Reaproveite o método do Program.java
        return 0; // Ajuste conforme sua lógica
    }

    private String extrairCampo(String json, String campo) {
        return json.split(campo + "\":")[1].split(",")[0].replace("\"", "").trim();
    }

    private void enviarResposta(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
