package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TelaJogo extends JFrame {
    private JPanel painelMao;
    private JPanel painelPilha;
    private JLabel labelCartaEntupimento;
    private Jogo jogo;

    public TelaJogo(Jogo jogo) {
        this.jogo = jogo;
        configurarJanela();
        inicializarComponentes();
    }

    /**
     * Configura as propriedades básicas da janela.
     */
    private void configurarJanela() {
        setTitle("Jogo Cocô");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    /**
     * Inicializa os componentes da interface gráfica.
     */
    private void inicializarComponentes() {
        configurarPaineis();
        configurarLabelEntupimento();
    }

    /**
     * Configura os painéis da interface.
     */
    private void configurarPaineis() {
        painelPilha = new JPanel(new FlowLayout());
        add(painelPilha, BorderLayout.CENTER);

        painelMao = new JPanel(new FlowLayout());
        add(painelMao, BorderLayout.SOUTH);
    }

    /**
     * Configura o rótulo da carta de entupimento.
     */
    private void configurarLabelEntupimento() {
        labelCartaEntupimento = new JLabel("Entupimento: ");
        labelCartaEntupimento.setFont(new Font("Arial", Font.BOLD, 16));
        labelCartaEntupimento.setHorizontalAlignment(SwingConstants.CENTER);
        add(labelCartaEntupimento, BorderLayout.NORTH);
    }

    /**
     * Exibe as cartas da mão do jogador.
     */
    public void exibirMaoDoJogador(Jogador jogador) {
        painelMao.removeAll();
        jogador.getMao().forEach(carta -> painelMao.add(criarBotaoCarta(jogador, carta)));
        painelMao.revalidate();
        painelMao.repaint();
    }

    /**
     * Cria um botão representando uma carta.
     */
    private JButton criarBotaoCarta(Jogador jogador, Carta carta) {
        JButton botaoCarta = new JButton(carta.toString());
        botaoCarta.setOpaque(true);
        botaoCarta.setPreferredSize(new Dimension(80, 100));
        botaoCarta.setBackground(definirCorCarta(carta));
        botaoCarta.addActionListener(e -> processarJogada(jogador, carta));
        return botaoCarta;
    }

    /**
     * Define a cor do botão da carta.
     */
    private Color definirCorCarta(Carta carta) {
        switch (carta.getCor().toLowerCase()) {
            case "amarelo": return Color.YELLOW;
            case "vermelho": return Color.RED;
            case "roxo": return new Color(243, 145, 243);
            case "preto": return Color.GRAY;
            default: return Color.WHITE;
        }
    }

    /**
     * Atualiza a interface do jogo após cada jogada.
     */
    public void atualizarTela(Jogo jogo, Jogador jogador) {
        painelMao.removeAll();
        exibirMaoDoJogador(jogador);
        atualizarCartaEntupimento(jogo);
        atualizarPilhaPrivada(jogo);
        repaint();
    }

    /**
     * Atualiza a carta de entupimento.
     */
    private void atualizarCartaEntupimento(Jogo jogo) {
        CartaPrivada cartaEntupimento = jogo.getCartaEntupimento();
        labelCartaEntupimento.setText("Entupimento: " + (cartaEntupimento != null ? cartaEntupimento : "Nenhuma carta definida"));
    }

    /**
     * Atualiza a pilha da privada.
     */
    private void atualizarPilhaPrivada(Jogo jogo) {
        painelPilha.removeAll();
        jogo.getPilhaPrivada().forEach(carta -> painelPilha.add(criarLabelCarta(carta)));
        painelPilha.revalidate();
        painelPilha.repaint();
    }

    /**
     * Cria um rótulo para representar uma carta na pilha.
     */
    private JLabel criarLabelCarta(Carta carta) {
        JLabel lblCarta = new JLabel(carta.toString());
        lblCarta.setPreferredSize(new Dimension(60, 90));
        lblCarta.setOpaque(true);
        lblCarta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lblCarta.setBackground(definirCorCarta(carta));
        return lblCarta;
    }

    /**
     * Processa a jogada ao clicar em uma carta.
     */
    private void processarJogada(Jogador jogador, Carta carta) {
        jogo.processarJogada(jogador, carta);
        atualizarTela(jogo, jogo.getJogadorAtual());
    }

    /**
     * Exibe uma mensagem na tela.
     */
    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    public static void main(String[] args) {

        // Criando um jogo de exemplo para testar a interface
        Jogo jogoExemplo = new Jogo(new ArrayList<>());

        // Adiciona o jogador de Exemplo ao jogo
        Jogador jogadorExemplo = new Jogador("Teste");
        jogoExemplo.adicionarJogador(jogadorExemplo);

        // Distribuir cartas iniciais controladas ao jogador (5 cartas)
        jogadorExemplo.adicionarCarta(new CartaNumerica("Amarelo", 0));
        jogadorExemplo.adicionarCarta(new CartaNumerica("Roxo", 4));
        jogadorExemplo.adicionarCarta(new CartaNumerica("Vermelho", 3));
        jogadorExemplo.adicionarCarta(new CartaEspecial("Roxo", CartaEspecial.Tipo.REVERTER));
        jogadorExemplo.adicionarCarta(new CartaNumerica("Amarelo", 2));

        // Definir uma carta de entupimento para o jogo
        jogoExemplo.definirCartaEntupimento(new CartaPrivada(8));

        // Inicialização da interface gráfica
        SwingUtilities.invokeLater(() -> {
            TelaJogo tela = new TelaJogo(jogoExemplo);
            tela.setVisible(true);
            tela.exibirMaoDoJogador(jogadorExemplo);
            tela.atualizarTela(jogoExemplo, jogadorExemplo);
        });
    }
}
