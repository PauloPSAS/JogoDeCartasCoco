package oldVersion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baralho {
    private List<Carta> cartas; // Lista de cartas no baralho
    private String tipoBaralho; // Tipo do baralho ("Privada" ou "Cocô")

    // Construtor para inicializar o baralho
    public Baralho(String tipoBaralho) {
        this.tipoBaralho = tipoBaralho;
        this.cartas = new ArrayList<>();
        inicializarBaralho();
        embaralhar();
    }

    private void inicializarBaralho() {
        if (tipoBaralho.equals("Privada")) {

            // Adiciona cartas Privadas (pretas, números de 8 a 12)
            adicionarCartasPrivadas();
        } else {

            // Adiciona Cartas Cocô (coloridas, números de 0 a 4 + especiais)
            adicionarCartasCoco();
        }
    }

    // Embaralha o baralho
    public void embaralhar() {
        Collections.shuffle(cartas);
    }

    private void adicionarCartasPrivadas() {
        int[] numerosPrivada = {8, 8, 9, 9, 10, 10, 10, 11, 11, 12, 12};
        for (int numero : numerosPrivada) {
            cartas.add(Carta.criarCartaPrivada(numero));
        }
    }

    private void adicionarCartasCoco() {
        String[] cores = {"Amarelo", "Vermelho", "Roxo"};
        for (String cor : cores) {
            for (int i = 0; i < 2; i++) {
                for (int num = 0; num <= 4; num++) {
                    cartas.add(new Carta(num, cor));
                }
                cartas.add(new Carta("Pular", cor));
                cartas.add(new Carta("Reverter", cor));
            }
        }
    }

    // Compra uma carta do baralho
    public Carta comprarCarta() {
        return cartas.isEmpty() ? null : cartas.remove(0);
    }

    // Retorna o número de cartas restantes no baralho
    public int qtdBaralho() {
        return cartas.size();
    }

    // Reabastece o baralho com cartas de descarte (se necessário)
    public void reporBaralho(List<Carta> descarte) {
        cartas.addAll(descarte);
        descarte.clear();
        embaralhar();
    }
}
