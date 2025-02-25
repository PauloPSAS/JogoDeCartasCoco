package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TelaJogo extends JFrame {
    private JPanel painelMao;
    private JPanel painelPilha;
    private JLabel labelCartaEntupimento;
    private JLabel labelJogadorAtual;
    private JLabel labelMensagem;
    private Jogo jogo;
    private boolean podeJogar = true;

    public TelaJogo(Jogo jogo) {
        this.jogo = jogo;
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("Jogo Cocô");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        configurarPaineis();
        configurarLabels();
    }

    private void configurarPaineis() {
        painelPilha = new JPanel(new FlowLayout());
        add(painelPilha, BorderLayout.CENTER);

        painelMao = new JPanel(new FlowLayout());
        add(painelMao, BorderLayout.SOUTH);
    }

    private void configurarLabels() {
        JPanel painelTopo = new JPanel(new GridLayout(3, 1));

        labelJogadorAtual = new JLabel("Vez de: ", SwingConstants.CENTER);
        labelJogadorAtual.setFont(new Font("Arial", Font.BOLD, 16));

        labelCartaEntupimento = new JLabel("Entupimento: ");
        labelCartaEntupimento.setFont(new Font("Arial", Font.BOLD, 16));
        labelCartaEntupimento.setHorizontalAlignment(SwingConstants.CENTER);

        labelMensagem = new JLabel("", SwingConstants.CENTER);
        labelMensagem.setFont(new Font("Arial", Font.BOLD, 14));
        labelMensagem.setForeground(Color.RED);

        painelTopo.add(labelJogadorAtual);
        painelTopo.add(labelCartaEntupimento);
        painelTopo.add(labelMensagem);
        add(painelTopo, BorderLayout.NORTH);
    }

    public void exibirMaoDoJogador(Jogador jogador) {
        painelMao.removeAll();
        jogador.getMao().forEach(carta -> painelMao.add(criarBotaoCarta(jogador, carta)));
        painelMao.revalidate();
        painelMao.repaint();
    }

    private JButton criarBotaoCarta(Jogador jogador, Carta carta) {
        JButton botaoCarta = new JButton(carta.toString());
        botaoCarta.setOpaque(true);
        botaoCarta.setPreferredSize(new Dimension(80, 100));
        botaoCarta.setBackground(definirCorCarta(carta));
        botaoCarta.addActionListener(e -> tentarJogarCarta(jogador, carta));
        return botaoCarta;
    }

    private Color definirCorCarta(Carta carta) {
        switch (carta.getCor().toLowerCase()) {
            case "amarelo": return Color.YELLOW;
            case "vermelho": return Color.RED;
            case "roxo": return new Color(243, 145, 243);
            case "preto": return Color.GRAY;
            default: return Color.WHITE;
        }
    }

    public void atualizarTela(Jogo jogo, Jogador jogador) {
        painelMao.removeAll();
        exibirMaoDoJogador(jogador);
        atualizarCartaEntupimento(jogo);
        atualizarPilhaPrivada(jogo);
        labelJogadorAtual.setText("Vez de: " + jogo.getJogadorAtual().getNome());
        podeJogar = jogador.equals(jogo.getJogadorAtual());
        labelMensagem.setText(podeJogar ? "Sua vez de jogar!" : "Aguarde sua vez...");
        repaint();
    }

    private void atualizarCartaEntupimento(Jogo jogo) {
        CartaPrivada cartaEntupimento = jogo.getCartaEntupimento();
        labelCartaEntupimento.setText("Entupimento: " + (cartaEntupimento != null ? cartaEntupimento : "Nenhuma carta definida"));
    }

    private void atualizarPilhaPrivada(Jogo jogo) {
        painelPilha.removeAll();
        jogo.getPilhaPrivada().forEach(carta -> painelPilha.add(criarLabelCarta(carta)));
        painelPilha.revalidate();
        painelPilha.repaint();
    }

    private JLabel criarLabelCarta(Carta carta) {
        JLabel lblCarta = new JLabel(carta.toString());
        lblCarta.setPreferredSize(new Dimension(60, 90));
        lblCarta.setOpaque(true);
        lblCarta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lblCarta.setBackground(definirCorCarta(carta));
        return lblCarta;
    }

    private void tentarJogarCarta(Jogador jogador, Carta carta) {
        if (!podeJogar) {
            mostrarMensagem("Não é sua vez! Aguarde o turno do outro jogador.");
            return;
        }
        System.out.println("Jogador " + jogador.getNome() + " está jogando a carta: " + carta);
        processarJogada(jogador, carta);
    }

    private void processarJogada(Jogador jogador, Carta carta) {
        System.out.println("Processando jogada... Carta jogada: " + carta);

        // Remove a carta da mão do jogador
        jogador.getMao().remove(carta);

        // Adiciona a carta à pilha da privada
        jogo.getPilhaPrivada().add(carta);
        System.out.println("Carta " + carta + " adicionada à pilha da privada.");

        if (jogo.verificarVitoria(jogador)) {
            mostrarMensagem(jogador.getNome() + " venceu o jogo!");
            return; // Para evitar passar o turno
        }

        // Aplica efeitos das cartas especiais (Pular, Reverter)
        jogo.aplicarEfeito(carta, jogador);

        // Verifica se há uma descarga
        if (jogo.verificarDescarga(jogador)) {
            mostrarMensagem("Descarga ativada! Todos os jogadores (exceto " + jogador.getNome() + ") compram uma carta.");
        }

        // Verifica se a privada entupiu
        jogo.verificarEntupimento(jogador);

        // Passa o turno para o próximo jogador
        podeJogar = false;
        jogo.proximoJogador();
        System.out.println("Agora é a vez do jogador: " + jogo.getJogadorAtual().getNome());

        // Atualiza a interface
        atualizarTela(jogo, jogo.getJogadorAtual());
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    public static void main(String[] args) {
        Jogo jogoExemplo = new Jogo(new ArrayList<>());

        Jogador jogador1 = new Jogador("Jogador 1");
        Jogador jogador2 = new Jogador("Jogador 2");
        Jogador jogador3 = new Jogador("Jogador 3");

        jogoExemplo.adicionarJogador(jogador1);
        jogoExemplo.adicionarJogador(jogador2);
        jogoExemplo.adicionarJogador(jogador3);

        jogador1.adicionarCarta(new CartaNumerica("Amarelo", 0));
        jogador1.adicionarCarta(new CartaNumerica("Roxo", 4));
        jogador1.adicionarCarta(new CartaNumerica("Vermelho", 3));
        jogador1.adicionarCarta(new CartaEspecial("Roxo", CartaEspecial.Tipo.REVERTER));
        jogador1.adicionarCarta(new CartaNumerica("Amarelo", 2));

        jogador2.adicionarCarta(new CartaEspecial("Vermelho", CartaEspecial.Tipo.PULAR));
        jogador2.adicionarCarta(new CartaNumerica("Roxo", 2));
        jogador2.adicionarCarta(new CartaNumerica("Amarelo", 4));

        jogador3.adicionarCarta(new CartaNumerica("Roxo", 3));
        jogador3.adicionarCarta(new CartaNumerica("Roxo", 0));


        SwingUtilities.invokeLater(() -> {
            TelaJogo tela = new TelaJogo(jogoExemplo);
            tela.setVisible(true);
            tela.exibirMaoDoJogador(jogador1);
            tela.atualizarTela(jogoExemplo, jogador1);
        });
    }
}
