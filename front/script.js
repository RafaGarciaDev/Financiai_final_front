// URL base da sua API Java (ajuste para o endereço do seu servidor)
const API_URL = "http://localhost:8080";

// Cadastro de Cliente
document.getElementById("form-cliente").addEventListener("submit", async (e) => {
    e.preventDefault();
    const nome = document.getElementById("nome").value;
    const cpf = document.getElementById("cpf").value;
    const renda = document.getElementById("renda").value;

    try {
        const response = await fetch(`${API_URL}/clientes`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nome, cpf, renda })
        });

        const data = await response.text();
        document.getElementById("resposta-cliente").textContent = data;
    } catch (error) {
        console.error("Erro:", error);
    }
});

// Simulação de Financiamento
document.getElementById("form-simulacao").addEventListener("submit", async (e) => {
    e.preventDefault();
    const cpf = document.getElementById("cpf-simulacao").value;
    const valorImovel = document.getElementById("valor-imovel").value;
    const tipoAmortizacao = document.getElementById("tipo-amortizacao").value;

    try {
        const response = await fetch(`${API_URL}/financiamentos/simular`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ 
                cpfCliente: cpf,
                valorImovel: valorImovel,
                tipoAmortizacao: tipoAmortizacao
            })
        });

        const data = await response.json();
        exibirResultadoSimulacao(data);
    } catch (error) {
        console.error("Erro:", error);
    }
});

function exibirResultadoSimulacao(data) {
    const divResultado = document.getElementById("resultado-simulacao");
    divResultado.innerHTML = `
        <h3>Resultado da Simulação</h3>
        <p><strong>Valor Financiado:</strong> R$ ${data.valorFinanciado.toFixed(2)}</p>
        <p><strong>Total a Pagar:</strong> R$ ${data.totalPagar.toFixed(2)}</p>
        <button onclick="contratarFinanciamento()">Contratar</button>
    `;
}

async function contratarFinanciamento() {
    // Implemente a chamada para /financiamentos/contratar
    alert("Financiamento contratado com sucesso!");
}