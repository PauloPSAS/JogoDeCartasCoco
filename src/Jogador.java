import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Jogador {
    private final String id;
    private Socket socket;
    private String nome;
    private List<Carta> mao;
    private boolean venceu;

    // Construtor
    public Jogador(String nome, Socket socket) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.mao = new ArrayList<>();
        this.venceu = false;
        this.socket = socket;
    }

    public void limparMao() {
        mao.clear();
    }

    // Método para receber uma carta
    public void receberCarta(Carta carta) {
        mao.add(carta);
    }

    // Método para jogar uma carta
    public Carta jogarCarta(int indice) {
        if (indice >= 0 && indice < mao.size()) {
            return mao.remove(indice);
        }
        return null;
    }

    // Método para verificar se o jogador venceu (mão vazia)
    public boolean verificarVitoria() {
        if (mao.isEmpty()) {
            venceu = true;
        }
        return venceu;
    }

    // Exibe as cartas na mão do jogador
    public void exibirMao() {
        System.out.println("Mão de " + nome + ": ");
        for (int i = 0; i < mao.size(); i++) {
            System.out.println(i + ". " + mao.get(i));
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<Carta> getMao() {
        return mao;
    }

    public boolean isVenceu() {
        return venceu;
    }

    public Socket getSocket() {
        return socket;
    }

    public void enviarMensagem(String texto) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(texto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receberMensagem() {
        String messagem = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            messagem = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return messagem;
    }

}
