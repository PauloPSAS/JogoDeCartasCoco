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

        setTitle("Jogo Cocô");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel Central: Pilha da Privada
        painelPilha = new JPanel();
        painelPilha.setLayout(new FlowLayout());
        add(painelPilha, BorderLayout.CENTER);

        // Painel Inferior: Mão do Jogador
        painelMao = new JPanel();
        painelMao.setLayout(new FlowLayout());
        add(painelMao, BorderLayout.SOUTH);

        // Painel Superior: Carta de Entupimento
        labelCartaEntupimento = new JLabel("Entupimento: ");
        labelCartaEntupimento.setFont(new Font("Arial", Font.BOLD, 16));
        labelCartaEntupimento.setHorizontalAlignment(SwingConstants.CENTER);
        add(labelCartaEntupimento, BorderLayout.NORTH);
    }

    // Método para exibir as cartas da mão do jogador
    public void exibirMaoDoJogador(Jogador jogador) {
        painelMao.removeAll(); // Limpa cartas antigas

        for (Carta carta : jogador.getMao()) {
            JButton botaoCarta = new JButton(carta.toString());
            botaoCarta.setOpaque(true);
            botaoCarta.setPreferredSize(new Dimension(80, 100));

            // Define cor de fundo baseada na carta
            switch (carta.getCor().toLowerCase()) {
                case "amarelo": botaoCarta.setBackground(Color.YELLOW); break;
                case "vermelho": botaoCarta.setBackground(Color.RED); break;
                case "roxo": botaoCarta.setBackground(new Color(243, 145, 243)); break;
                case "preto": botaoCarta.setBackground(Color.GRAY); botaoCarta.setForeground(Color.WHITE); break;
            }

            // Adiciona evento de clique na carta
            botaoCarta.addActionListener(e -> {
                processarJogada(jogador, carta);
            });

            painelMao.add(botaoCarta);
        }
        painelMao.revalidate();
        painelMao.repaint();
    }

    // Atualiza a interface do jogo após cada jogada
    public void atualizarTela(Jogo jogo, Jogador jogador) {
        painelMao.removeAll();
        exibirMaoDoJogador(jogador);

        // Atualiza a carta de entupimento corretamente
        CartaPrivada cartaEntupimento = jogo.getCartaEntupimento();
        if (cartaEntupimento != null) {
            System.out.println("Atualizando Interface - Carta de Entupimento: " + cartaEntupimento);
            labelCartaEntupimento.setText("Entupimento: " + cartaEntupimento);
        } else {
            labelCartaEntupimento.setText("Entupimento: Nenhuma carta definida");
        }

        // Atualiza a pilha da privada
        painelPilha.removeAll();
        for (Carta carta : jogo.getPilhaPrivada()) {
            JLabel lblCarta = new JLabel(carta.toString());
            lblCarta.setPreferredSize(new Dimension(60, 90));
            lblCarta.setOpaque(true);
            lblCarta.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // Define a cor de fundo da carta na pilha
            switch (carta.getCor().toLowerCase()) {
                case "amarelo": lblCarta.setBackground(Color.YELLOW); break;
                case "vermelho": lblCarta.setBackground(Color.RED); break;
                case "roxo": lblCarta.setBackground(new Color(243, 145, 243)); break;
                case "preto": lblCarta.setBackground(Color.BLACK); lblCarta.setForeground(Color.WHITE); break;
            }

            painelPilha.add(lblCarta);
        }

        painelPilha.revalidate();
        painelPilha.repaint();
        repaint(); // Força a atualização da interface
    }

    // Método para processar a jogada ao clicar em uma carta
    private void processarJogada(Jogador jogador, Carta carta) {
        jogo.processarJogada(jogador, carta);
        atualizarTela(jogo, jogo.getJogadorAtual());
    }

    // Método para exibir mensagens na tela
    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    public static void main(String[] args) {
        // Criando um jogo de exemplo para testar a interface
        Jogo jogoExemplo = new Jogo(new ArrayList<>());
        Jogador jogadorExemplo = new Jogador("Teste");

        // Adiciona o jogador ao jogo
        jogoExemplo.adicionarJogador(jogadorExemplo);

        // Distribuir cartas iniciais ao jogador (5 cartas)
        jogadorExemplo.adicionarCarta(new CartaNumerica("Amarelo", 0));
        jogadorExemplo.adicionarCarta(new CartaNumerica("Roxo", 4));
        jogadorExemplo.adicionarCarta(new CartaNumerica("Vermelho", 3));
        jogadorExemplo.adicionarCarta(new CartaEspecial("Amarelo", CartaEspecial.Tipo.PULAR));
        jogadorExemplo.adicionarCarta(new CartaNumerica("Amarelo", 2));

        // Definir uma carta de entupimento para o jogo
        jogoExemplo.definirCartaEntupimento(new CartaPrivada(10)); // Deve ser a mesma do console

        // Inicialização da interface gráfica
        SwingUtilities.invokeLater(() -> {
            TelaJogo tela = new TelaJogo(jogoExemplo);
            tela.setVisible(true);
            tela.exibirMaoDoJogador(jogadorExemplo);
            tela.atualizarTela(jogoExemplo, jogadorExemplo); // Atualiza a interface para mostrar a carta de entupimento
        });
    }
}
