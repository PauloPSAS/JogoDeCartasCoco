package model;

import java.util.ArrayList;
import java.util.List;

// Representa um jogador no jogo
public class Jogador {
    private String nome;
    private List<Carta> mao;

    public Jogador(String nome) {
        this.nome = nome;
        this.mao = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public List<Carta> getMao() {
        return mao;
    }

    public void adicionarCarta(Carta carta) {
        mao.add(carta);
    }

    public void removerCarta(Carta carta) {
        mao.remove(carta);
    }

    public boolean temVencido() {
        return mao.isEmpty();
    }

    @Override
    public String toString() {
        return nome;
    }
}
