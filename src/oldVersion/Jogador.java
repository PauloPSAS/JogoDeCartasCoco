package oldVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Jogador {
    private final String id;
    private String nome;
    private List<Carta> mao;
    private boolean venceu;

    // Construtor
    public Jogador(String nome) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.mao = new ArrayList<>();
        this.venceu = false;
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
}
