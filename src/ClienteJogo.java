import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteJogo {
    private static final int PORTA = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClienteJogo() {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("Digite o IP do servidor (pressione Enter para usar o padrão 127.0.0.1): ");
            String servidorIP = input.nextLine().trim();
            if (servidorIP.isEmpty()) {
                servidorIP = "127.0.0.1";
            }

            System.out.println("Digite seu nome (pressione ENTER para nome padrão): ");
            String nomeJogador = input.nextLine().trim();

            socket = new Socket(servidorIP, PORTA);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("NOME " + (nomeJogador.isEmpty() ? "User" + System.currentTimeMillis() % 1000 : nomeJogador));

            System.out.println("Conectado ao servidor " + servidorIP);

            // Thread para receber mensagens do servidor
            new Thread(this::receberMensagens).start();

            // Captura entrada do usuário e envia para o servidor
            while (true) {
                String mensagem = input.nextLine();
                enviarMensagem(mensagem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envia uma mensagem para o servidor.
     * 
     * @param mensagem Mensagem a ser enviada
     */
    void enviarMensagem(String mensagem) {
        out.println(mensagem);
    }

    /**
     * Recebe mensagens do servidor e as exibe no console.
     */
    private void receberMensagens() {
        try {
            String mensagem;
            while ((mensagem = in.readLine()) != null) {
                System.out.println("Servidor: " + mensagem);
            }
        } catch (IOException e) {
            System.out.println("Conexão com o servidor perdida.");
        }
    }

    public static void main(String[] args) {
        new ClienteJogo();
    }
}
