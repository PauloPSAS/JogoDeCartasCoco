package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    private Socket socket;      // Socket para comunicação com o servidor
    private BufferedReader in;  // Leitor de mensagens recebidas do servidor
    private PrintWriter out;    // Escritor para enviar mensagens ao servidor

    /**
     * Conecta o cliente ao servidor usando o IP e a porta especificados.
     * @param ip Endereço IP do servidor.
     * @param porta Porta do servidor.
     */
    public void conectar(String ip, int porta) {
        try {
            criarSocket(ip, porta);
            inicializarStreams();
            System.out.println("Conectado ao servidor.");
            // Implementar troca de mensagens conforme a lógica do jogo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cria um socket para conexão com o servidor.
     */
    private void criarSocket(String ip, int porta) throws IOException {
        socket = new Socket(ip, porta);
    }

    /**
     * Inicializa os fluxos de entrada e saída para comunicação com o servidor.
     */
    private void inicializarStreams() throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Envia uma mensagem ao servidor.
     * @param mensagem Texto da mensagem a ser enviada.
     */
    public void enviarMensagem(String mensagem) {
        out.println(mensagem);
    }

    /**
     * Lê uma mensagem recebida do servidor.
     * @return A mensagem recebida ou null em caso de erro.
     */
    public String lerMensagem() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fecha a conexão com o servidor e libera os recursos utilizados.
     */
    public void fechar() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
