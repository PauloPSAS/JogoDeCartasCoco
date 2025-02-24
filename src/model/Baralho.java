package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Classe para gerenciar um baralho de cartas
public class Baralho {
    private List<Carta> cartas;

    public Baralho() {
        cartas = new ArrayList<>();
    }

    public void adicionarCarta(Carta carta) {
        cartas.add(carta);
    }

    public void embaralhar() {
        Collections.shuffle(cartas);
    }

    public Carta comprarCarta() {
        if (!cartas.isEmpty()) {
            return cartas.remove(0);
        }
        return null;
    }

    public int tamanho() {
        return cartas.size();
    }
}
