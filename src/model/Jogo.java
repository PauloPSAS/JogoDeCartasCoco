package model;

import java.util.ArrayList;
import java.util.List;

// Classe que orquestra o jogo, gerencia os turnos e as regras
public class Jogo {
    private List<Jogador> jogadores;
    private Baralho baralhoPrivada;
    private Baralho baralhoCoco;
    private Baralho descartePrivada;
    public List<Carta> pilhaPrivada;
    public CartaPrivada cartaEntupimento;
    private int indiceJogadorAtual;
    private boolean ordemNormal;

    public Jogo(List<Jogador> jogadores) {
        this.jogadores = jogadores;
        this.baralhoPrivada = criarBaralhoPrivada();
        this.baralhoCoco = criarBaralhoCoco();
        this.descartePrivada = new Baralho();
        this.pilhaPrivada = new ArrayList<>();
        this.ordemNormal = true;
        this.indiceJogadorAtual = 0;
        distribuirCartas();
        iniciarCartaEntupimento();
    }

    // Cria o baralho de Privada com 11 cartas (2x8, 2x9, 3x10, 2x11, 2x12)
    private Baralho criarBaralhoPrivada() {
        Baralho b = new Baralho();
        for (int i = 0; i < 2; i++) b.adicionarCarta(new CartaPrivada(8));
        for (int i = 0; i < 2; i++) b.adicionarCarta(new CartaPrivada(9));
        for (int i = 0; i < 3; i++) b.adicionarCarta(new CartaPrivada(10));
        for (int i = 0; i < 2; i++) b.adicionarCarta(new CartaPrivada(11));
        for (int i = 0; i < 2; i++) b.adicionarCarta(new CartaPrivada(12));
        b.embaralhar();
        return b;
    }

    // Cria o baralho de Cartas Cocô com as cores Amarelo, Vermelho e Roxo
    // Cada cor tem: 2 cartas numéricas para cada valor de 0 a 4 e 1 carta de cada especial (Pular e Reverter)
    private Baralho criarBaralhoCoco() {
        Baralho b = new Baralho();
        String[] cores = {"Amarelo", "Vermelho", "Roxo"};
        for (String cor : cores) {
            for (int valor = 0; valor <= 4; valor++) {
                b.adicionarCarta(new CartaNumerica(cor, valor));
                b.adicionarCarta(new CartaNumerica(cor, valor));
            }
            b.adicionarCarta(new CartaEspecial(cor, CartaEspecial.Tipo.PULAR));
            b.adicionarCarta(new CartaEspecial(cor, CartaEspecial.Tipo.REVERTER));
        }
        b.embaralhar();
        return b;
    }

    // Distribui 5 cartas de Cocô para cada jogador
    private void distribuirCartas() {
        for (int i = 0; i < 5; i++) {
            for (Jogador jogador : jogadores) {
                jogador.adicionarCarta(baralhoCoco.comprarCarta());
            }
        }
    }

    // Revela a carta de entupimento (carta privada) para definir o limite da partida
    private void iniciarCartaEntupimento() {
        Carta carta = baralhoPrivada.comprarCarta();
        if (carta instanceof CartaPrivada) {
            cartaEntupimento = (CartaPrivada) carta;
        }
        System.out.println("Carta de Entupimento: " + cartaEntupimento);
    }

    // Define a nova carta de entupimento (revela a próxima carta do baralho de privadas)
    private void definirCartaEntupimento() {
        Carta nova = baralhoPrivada.comprarCarta();
        if (nova instanceof CartaPrivada) {
            cartaEntupimento = (CartaPrivada) nova;
            System.out.println("Nova carta de entupimento: " + cartaEntupimento);
        } else {
            System.out.println("Erro: A carta comprada não é do tipo CartaPrivada!");
        }
    }

    // Avança para o próximo jogador conforme a ordem
    public void proximoJogador() {
        if (ordemNormal) {
            indiceJogadorAtual = (indiceJogadorAtual + 1) % jogadores.size();
        } else {
            indiceJogadorAtual = (indiceJogadorAtual - 1 + jogadores.size()) % jogadores.size();
        }
    }

    // Exemplo de método para jogar uma carta (a lógica completa deverá incluir as regras de entupimento e descarga)
    public void jogarCarta(Jogador jogador, Carta carta) {
        pilhaPrivada.add(carta);
        jogador.removerCarta(carta);
        System.out.println(jogador.getNome() + " jogou: " + carta);
        // Aqui deverão ser implementadas as verificações de:
        // 1. Mão vazia (vitória)
        // 2. Condição de Descarga (três cartas consecutivas da mesma cor)
        // 3. Verificação do limite da carta privada (entupimento)
    }

    public boolean verificarVitoria(Jogador jogador) {
        boolean venceu = jogador.temVencido();
        if (venceu) {
            System.out.println("Vitória! " + jogador.getNome() + " venceu o jogo!");
        }
        return venceu;
    }

    public CartaPrivada getCartaEntupimento() {
        return cartaEntupimento;
    }

    // Verifica se há uma descarga e a executa se a condição for atendida.
    public boolean verificarDescarga(Jogador jogadorAtual) {
        if (pilhaPrivada.size() >= 3 && descargaPodeSerAtivada()) {
            executarDescarga(jogadorAtual);
            return true;
        }
        return false;
    }

    // Verifica se as três últimas cartas da pilha possuem a mesma cor.
    private boolean descargaPodeSerAtivada() {
        int tamanho = pilhaPrivada.size();
        if (tamanho < 3) {
            return false;
        }
        Carta ultima = pilhaPrivada.get(tamanho - 1);
        Carta penultima = pilhaPrivada.get(tamanho - 2);
        Carta antepenultima = pilhaPrivada.get(tamanho - 3);
        return ultima.getCor().equals(penultima.getCor()) && penultima.getCor().equals(antepenultima.getCor());
    }

    // Executa a descarga:
    // 1. Adiciona todas as cartas da pilha privada ao descartePrivada e a limpa.
    // 2. Todos os jogadores, exceto o que acionou a descarga, compram uma carta do baralhoCoco.
    private void executarDescarga(Jogador jogadorAtual) {
        System.out.println("Descarga ativada! As cartas da pilha privada foram descartadas.");

        // Move todas as cartas da pilha privada para o descartePrivada.
        for (Carta carta : pilhaPrivada) {
            descartePrivada.adicionarCarta(carta);
        }
        pilhaPrivada.clear();

        // Os demais jogadores compram uma carta do baralhoCoco.
        for (Jogador jogador : jogadores) {
            if (!jogador.equals(jogadorAtual)) {
                jogador.adicionarCarta(baralhoCoco.comprarCarta());
            }
        }
    }

    // Verifica se a soma dos valores das cartas numéricas na pilha da privada
    // alcançou ou ultrapassou o valor da carta de entupimento.
    public void verificarEntupimento(Jogador jogadorAtual) {
        int soma = 0;
        for (Carta carta : pilhaPrivada) {

            // Considera apenas as cartas numéricas; cartas especiais não somam valor.
            if (carta instanceof CartaNumerica) {
                soma += ((CartaNumerica) carta).getValor();
            }
        }
        int limite = cartaEntupimento.getValor();
        if (soma >= limite) {
            executarEntupimento(jogadorAtual);
        }
    }

    // Executa o entupimento: o jogador atual pega todas as cartas da pilha,
    // a carta de entupimento é descartada e uma nova é definida.
    private void executarEntupimento(Jogador jogadorAtual) {
        System.out.println("A privada entupiu! " + jogadorAtual.getNome() + " pegou todas as cartas.");

        // O jogador pega todas as cartas da pilha privada
        jogadorAtual.getMao().addAll(pilhaPrivada);
        pilhaPrivada.clear();

        // Adiciona a carta de entupimento atual ao descarte das privadas
        descartePrivada.adicionarCarta(cartaEntupimento);

        // Define uma nova carta de entupimento a partir do baralho de privadas
        definirCartaEntupimento();
    }

    // Método para aplicar o efeito das cartas especiais
    public void aplicarEfeito(Carta carta, Jogador jogadorAtual) {
        if (carta instanceof CartaEspecial) {
            CartaEspecial especial = (CartaEspecial) carta;
            switch (especial.getTipo()) {
                case PULAR:
                    System.out.println("Efeito PULAR: pulando o próximo jogador.");

                    // Avança duas vezes para pular o próximo jogador.
                    proximoJogador(); // pula o próximo jogador
                    break;
                case REVERTER:
                    System.out.println("Efeito REVERTER: invertendo a ordem dos turnos.");
                    ordemNormal = !ordemNormal;
                    break;
            }
        }
    }

    public void processarJogada(Jogador jogador, Carta carta) {
        // Jogador joga uma carta obrigatoriamente
        pilhaPrivada.add(carta);
        jogador.removerCarta(carta);
        System.out.println(jogador.getNome() + " jogou: " + carta);

        // Se a carta for especial, aplica seu efeito
        aplicarEfeito(carta, jogador);

        // Verifica se a condição de descarga foi atendida
        if (verificarDescarga(jogador)) {
            System.out.println("Descarga ativada!");
        } else {
            // Se não houver descarga, verifica o entupimento
            verificarEntupimento(jogador);
        }

        // Verifica se o jogador venceu (mão vazia)
        if (verificarVitoria(jogador)) {
            System.out.println("Fim de jogo! Campeão: " + jogador.getNome());
            // Aqui, você pode implementar a lógica de encerramento do jogo.
            return;
        }

        // Se o jogo não terminou, passa a vez para o próximo jogador
        proximoJogador();
    }

    // Retorna o jogador da vez
    public Jogador getJogadorAtual() {
        return jogadores.get(indiceJogadorAtual);
    }

    // Método para adicionar um jogador ao jogo
    public void adicionarJogador(Jogador jogador) {
        if (jogadores.size() < 5) {
            jogadores.add(jogador);
            System.out.println(jogador.getNome() + " entrou no jogo.");
        } else {
            System.out.println("O jogo já atingiu o número máximo de jogadores.");
        }
    }

    // Método para retornar a lista de jogadores (se precisar usar em outra parte do código)
    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public List<Carta> getPilhaPrivada() {
        return pilhaPrivada;
    }
}
