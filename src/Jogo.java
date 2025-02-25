import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jogo {
    private List<Jogador> jogadores; // Lista de jogadores
    private Baralho baralhoCoco; // Baralho das Cartas Cocô
    private Baralho baralhoPrivada; // Baralho das Cartas Privadas
    private List<Carta> privada; // Pilha de cartas na privada
    private int indiceJogadorAtual; // Índice do jogador que está na vez
    private int numeroEntupimento; // Número atual da privada
    private boolean sentidoHorario; // Direção do jogo
    private List<Carta> descartePrivadas; // Pilha de descarte das cartas privadas
    private ArrayList<Carta> descarte; // Pilha de descarte das cartas da privada
    private boolean descargaAtivada; // Indica se a descarga foi ativada

    // Construtor
    public Jogo(List<Jogador> jogadores) {
        if (jogadores.size() < 2 || jogadores.size() > 5) {
            throw new IllegalArgumentException("O número de jogadores deve ser entre 2 e 5.");
        }

        this.jogadores = jogadores;
        this.baralhoCoco = new Baralho("Cocô");
        this.baralhoPrivada = new Baralho("Privada");
        this.privada = new ArrayList<>();
        this.sentidoHorario = true;
        this.descargaAtivada = false;

        inicializarJogo();
    }

    public void novoTurno() {
        Jogador atual = getJogadorAtual();

        atual.enviarMensagem("Seu Turno!");

        String mensagem = atual.receberMensagem();

        System.out.println(mensagem);
        for (Jogador jogador : jogadores)
            jogador.enviarMensagem(atual.getNome() + ": " + mensagem);

        proximoTurno();
        novoTurno();
    }

    // Inicializa o jogo distribuindo cartas e escolhendo o primeiro jogador
    private void inicializarJogo() {
        distribuirCartasIniciais();
        definirNumeroEntupimento();
        selecionarJogadorInicial();
        novoTurno();
    }

    // Distribui 5 cartas Cocô para cada jogador
    private void distribuirCartasIniciais() {
        for (Jogador jogador : jogadores) {
            jogador.limparMao();
            for (int i = 0; i < 5; i++) {
                jogador.receberCarta(baralhoCoco.comprarCarta());
            }
        }
    }

    // Define o número de entupimento inicial
    private void definirNumeroEntupimento() {
        Carta cartaPrivada = baralhoPrivada.comprarCarta();
        if (cartaPrivada != null) {
            this.numeroEntupimento = cartaPrivada.getNumero();
        }
    }

    // Escolhe o primeiro jogador de forma aleatória
    private void selecionarJogadorInicial() {
        Random random = new Random();
        this.indiceJogadorAtual = random.nextInt(jogadores.size());
    }

    // Avança para o próximo jogador
    private void proximoTurno() {
        if (!descargaAtivada) {
            if (sentidoHorario) {
                indiceJogadorAtual = (indiceJogadorAtual + 1) % jogadores.size();
            } else {
                indiceJogadorAtual = (indiceJogadorAtual - 1 + jogadores.size()) % jogadores.size();
            }
        }
        descargaAtivada = false;
    }

    // Método para jogar uma carta
    public void JogarCarta(int indiceCarta) {
        Jogador jogadorAtual = jogadores.get(indiceJogadorAtual);
        Carta cartaJogada = jogadorAtual.jogarCarta(indiceCarta);

        if (cartaJogada != null) {
            privada.add(cartaJogada);
            aplicarEfeitoCartaEspecial(cartaJogada);
            if (verificarDescarga(jogadorAtual)) {
                descargaAtivada = true;
                return;
            }
            verificarEntupimento(jogadorAtual);
        }

        // Verifica se o jogador venceu
        if (jogadorAtual.verificarVitoria()) {
            System.out.println(jogadorAtual.getNome() + " venceu o jogo!");
            return;
        }

        // Passa para o próximo jogador
        proximoTurno();
    }

    // Aplica o efeito das cartas especiais
    private void aplicarEfeitoCartaEspecial(Carta carta) {
        if (carta.getTipo().equals("Pular")) {
            pularProximoJogador();
        } else if (carta.getTipo().equals("Reverter")) {
            reverterSentidoJogo();
        }
    }

    // Pula o próximo jogador
    private void pularProximoJogador() {
        proximoTurno();
    }

    // Inverte o sentido do jogo
    private void reverterSentidoJogo() {
        sentidoHorario = !sentidoHorario;
    }

    // Verifica se há uma descarga
    private boolean verificarDescarga(Jogador jogadorAtual) {
        if (privada.size() >= 3 && descargaPodeSerAtivada()) {
            executarDescarga(jogadorAtual);
            return true;
        }
        return false;
    }

    // Verifica se três cartas consecutivas têm a mesma cor
    private boolean descargaPodeSerAtivada() {
        int tamanho = privada.size();
        if (tamanho < 3) {
            return false;
        }
        Carta ultima = privada.get(tamanho - 1);
        Carta penultima = privada.get(tamanho - 2);
        Carta antepenultima = privada.get(tamanho - 3);
        return ultima.getCor().equals(penultima.getCor()) && penultima.getCor().equals(antepenultima.getCor());
    }

    // Executa a descarga
    private void executarDescarga(Jogador jogadorAtual) {
        System.out.println("Descarga ativada! As cartas da privada foram descartadas.");
        descarte.addAll(privada);
        privada.clear();
        for (Jogador jogador : jogadores) {
            if (!jogador.equals(jogadorAtual)) {
                jogador.receberCarta(baralhoCoco.comprarCarta());
            }
        }
    }

    // Verifica se a privada entupiu
    private void verificarEntupimento(Jogador jogadorAtual) {
        int soma = privada.stream().mapToInt(Carta::getNumero).sum();
        if (soma >= numeroEntupimento) {
            executarEntupimento(jogadorAtual);
        }
    }

    // Executa o entupimento
    private void executarEntupimento(Jogador jogadorAtual) {
        System.out.println("A privada entupiu! " + jogadorAtual.getNome() + " pegou todas as cartas.");
        jogadorAtual.getMao().addAll(privada);
        privada.clear();
        descartePrivadas.add(new Carta(numeroEntupimento, "Preto"));
        definirNumeroEntupimento();
    }

    // Getters
    public Jogador getJogadorAtual() {
        return jogadores.get(indiceJogadorAtual);
    }

    public int getNumeroEntupimento() {
        return numeroEntupimento;
    }
}
