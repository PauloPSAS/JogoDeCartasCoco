package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public void conectar(String ip, int porta) {
        try {
            socket = new Socket(ip, porta);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Conectado ao servidor.");
            // Implementar troca de mensagens conforme a lógica do jogo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarMensagem(String mensagem) {
        out.println(mensagem);
    }

    public String lerMensagem() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void fechar() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
