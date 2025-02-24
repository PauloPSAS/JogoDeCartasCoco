package network;

import model.Jogador;
import model.Jogo;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private ServerSocket serverSocket;
    private List<Socket> clientes;
    private List<Jogador> jogadores;
    private final int porta = 12345;

    public Servidor() {
        clientes = new ArrayList<>();
        jogadores = new ArrayList<>();
    }

    public void iniciar() {
        try {
            serverSocket = new ServerSocket(porta);
            System.out.println("Servidor iniciado na porta " + porta);

            // Aguarda conexões por até 1 minuto ou até atingir o máximo de 5 jogadores
            long tempoInicio = System.currentTimeMillis();
            while (System.currentTimeMillis() - tempoInicio < 60000 && clientes.size() < 5) {
                Socket cliente = serverSocket.accept();
                clientes.add(cliente);
                jogadores.add(new Jogador("Jogador" + clientes.size()));
            }

            // Verifica se temos o número mínimo de jogadores (2)
            if (jogadores.size() >= 2) {
                iniciarPartida();
            } else {
                System.out.println("Número insuficiente de jogadores. Encerrando servidor.");
                encerrar();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void iniciarPartida() {
        System.out.println("Iniciando a partida com " + jogadores.size() + " jogadores.");
        Jogo jogo = new Jogo(jogadores);
        // Aqui você deve implementar a comunicação e sincronização das jogadas entre os clientes
    }

    private void encerrar() {
        try {
            for (Socket s : clientes) {
                s.close();
            }
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
