package network;

import model.Jogador;
import model.Jogo;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private ServerSocket serverSocket;  // Socket do servidor
    private List<Socket> clientes;      // Lista de clientes conectados
    private List<Jogador> jogadores;    // Lista de jogadores conectados
    private final int porta = 12345;    // Porta fixa do servidor

    public Servidor() {
        clientes = new ArrayList<>();
        jogadores = new ArrayList<>();
    }

    /**
     * Método principal que inicia o servidor, aguarda conexões e verifica se pode iniciar a partida.
     */
    public void iniciar() {
        try {
            iniciarServerSocket();
            aguardarConexoes();
            verificarEIniciarPartida();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicializa o servidor na porta definida.
     */
    private void iniciarServerSocket() throws Exception {
        serverSocket = new ServerSocket(porta);
        System.out.println("Servidor iniciado na porta " + porta);
    }

    /**
     * Aguarda conexões de jogadores por até 1 minuto ou até atingir 5 jogadores.
     */
    private void aguardarConexoes() throws Exception {
        long tempoInicio = System.currentTimeMillis();
        while (System.currentTimeMillis() - tempoInicio < 60000 && clientes.size() < 5) {
            aceitarCliente();
        }
    }

    /**
     * Aceita um novo cliente e adiciona um jogador à lista.
     */
    private void aceitarCliente() throws Exception {
        Socket cliente = serverSocket.accept();
        clientes.add(cliente);
        jogadores.add(new Jogador("Jogador" + clientes.size()));
        System.out.println("Novo jogador conectado: Jogador" + clientes.size());
    }

    /**
     * Verifica se há jogadores suficientes para iniciar a partida.
     * Se houver pelo menos 2 jogadores, inicia o jogo. Caso contrário, encerra o servidor.
     */
    private void verificarEIniciarPartida() {
        if (jogadores.size() >= 2) {
            iniciarPartida();
        } else {
            System.out.println("Número insuficiente de jogadores. Encerrando servidor.");
            encerrar();
        }
    }

    /**
     * Inicia a partida do jogo com os jogadores conectados.
     */
    private void iniciarPartida() {
        System.out.println("Iniciando a partida com " + jogadores.size() + " jogadores.");
        Jogo jogo = new Jogo(jogadores);

        // Aqui você deve implementar a comunicação e sincronização das jogadas entre os clientes
    }

    /**
     * Encerra o servidor e fecha todas as conexões abertas.
     */
    private void encerrar() {
        try {
            fecharConexoes();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fecha todas as conexões dos clientes conectados.
     */
    private void fecharConexoes() throws Exception {
        for (Socket s : clientes) {
            s.close();
        }
    }
}
