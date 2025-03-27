package org.example.financiai_final_front.financiai.util;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Servidor {
    public static void main(String[] args) throws IOException {
        // Cria o servidor na porta 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Configura os endpoints
        server.createContext("/clientes", new ClientesHandler());
        server.createContext("/financiamentos/simular", new SimulacaoHandler());
        server.createContext("/financiamentos/contratar", new ContratarHandler());

        server.setExecutor(null); // Usa o executor padrão
        server.start();
        System.out.println("Servidor rodando na porta 8080");
    }
}