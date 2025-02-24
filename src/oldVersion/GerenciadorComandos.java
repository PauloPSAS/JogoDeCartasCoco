package oldVersion;

public class GerenciadorComandos {

    /**
     * Processa o comando de jogar uma carta.
     * @param jogador Identificação do jogador
     * @param indiceCarta Índice da carta a ser jogada
     */
    public void jogarCartas(Jogador jogador, int indiceCarta) {
        System.out.println("oldVersion.Jogador " + jogador + " jogou a carta no índice " + indiceCarta);

        // Aqui será implementada a lógica para jogar a carta no jogo...
    }

    /**
     * Processa o comando de comprar uma carta.
     * @param jogador Identificação do jogador
     */
    public void comprarCarta(Jogador jogador) {
        System.out.println("oldVersion.Jogador " + jogador + " comprou uma carta.");

        // Aqui será implementada a lógica para o jogador comprar uma carta...
    }

    /**
     * Processa o comando de saída do jogador.
     * @param jogador Identificação do jogador
     */
    public void sairDoJogo(Jogador jogador) {
        System.out.println("oldVersion.Jogador " + jogador + " saiu do jogo.");

        // Aqui será implementada a lógica para remover o jogador da partida
    }
}
