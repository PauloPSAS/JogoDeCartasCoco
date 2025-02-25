import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorJogo {
    private static final int PORTA = 12345; // Porta fixa usada para aceitar conexões dos clientes
    private List<Jogador> jogadores; // Lista de jogadores conectados
    private ServerSocket servidor; // Socket do servidor que aguarda conexões
    private boolean jogoIniciado; // Indica se o jogo já foi iniciado
    private GerenciadorComandos gerenciadorComandos;// Responsável por processar comandos dos jogadores
    private Baralho baralho;

    public ServidorJogo() {
        this.jogadores = new ArrayList<>();
        this.jogoIniciado = false;
        this.gerenciadorComandos = new GerenciadorComandos();


    }

    /**
     * Inicia o servidor, criando um socket na porta definida.
     * Aguarda conexões de clientes e começa a escutar jogadores.
     */
    public void iniciarServidor() {
        try {
            servidor = new ServerSocket(PORTA);
            System.out.println("Servidor iniciado na porta " + PORTA);
            aguardarConexoes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aguarda conexões de novos jogadores e os adiciona à lista.
     * Quando dois jogadores se conectam, inicia a contagem regressiva para iniciar
     * o jogo.
     * Se cinco jogadores se conectarem antes do tempo acabar, o jogo inicia
     * automaticamente.
     */
    private void aguardarConexoes() {
        System.out.println("Aguardando jogadores...");

        while (!jogoIniciado) {
            try {
                Socket cliente = servidor.accept();
                System.out.println("Novo jogador conectado: " + cliente.getInetAddress().getHostAddress());
                addCliente(cliente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inicia o jogo quando há jogadores suficientes conectados.
     * Exibe a mensagem de início do jogo.
     */
    private void iniciarJogo() {
        jogoIniciado = true;
        enviarMensagemParaTodos("Jogo iniciado com " + jogadores.size() + " jogadores!");
        new Jogo(jogadores);
    }


    private void iniciarContagemJogadores() {
        new Thread(() -> {
            try {
                for (int segundos = 60; segundos > 0; segundos -= 10) {
                    synchronized (jogadores) {
                        enviarMensagemParaTodos(
                                String.format(
                                        "%d jogadores conectados. Faltam %d segundos para começar o jogo...",
                                        jogadores.size(), segundos));
                    }
                    Thread.sleep(10000); // Espera 10 segundos
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (jogadores) {
                if (jogadores.size() >= 2 && jogadores.size() <= 5) {
                    iniciarJogo();
                }
            }
        }).start();
    }

    /**
     * Encerra o servidor, fechando todas as conexões e limpando a lista de
     * jogadores conectados.
     */
    private void encerrarServidor() {
        try {
            synchronized (jogadores) {
                for (Jogador jogador : jogadores) {
                    jogador.getSocket().close();
                }
                jogadores.clear();
            }
            servidor.close();
            System.out.println("Servidor encerrado.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCliente(Socket cliente) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            String mensagemInicial = in.readLine();

            if (mensagemInicial.startsWith("NOME ")) {
                String nome = mensagemInicial.substring(5).trim();
                Jogador jogador = new Jogador(nome, cliente);

                enviarMensagemParaTodos("Jogador " + nome + " entrou na partida.");

                synchronized (jogadores) {
                    jogadores.add(jogador);
                    if (jogadores.size() == 5) {
                        enviarMensagemParaTodos("Número máximo de jogadores conectados...");
                        iniciarJogo();
                    } else {
                        if (jogadores.size() == 2) {
                            iniciarContagemJogadores();
                        }
                        enviarMensagemParaTodos(String.format("Aguardando jogadores... Número de Jogadores: %d",
                                jogadores.size()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lida com um cliente conectado, recebendo e processando mensagens.
     * Mantém a conexão aberta até que o cliente desconecte.
     */
    private void lidarComClientes() {

        // try {
        // // String mensagem;
        // // while ((mensagem = in.readLine()) != null) {
        // // processarMensagem(cliente, mensagem);
        // // }
        // } catch (IOException e) {
        // removerCliente(cliente);
        // }
    }

    /**
     * Processa as mensagens recebidas dos clientes.
     * Interpreta comandos e executa ações apropriadas.
     */
    private void processarMensagem(Socket cliente, String mensagem) {
        // Jogador jogador = mapaJogadores.get(cliente);
        // String nomeJogador = (jogador != null) ? jogador.getNome() : "Desconhecido";
        // System.out.println("Mensagem recebida de " + nomeJogador + ": " + mensagem);
        // enviarMensagemParaTodos(nomeJogador + ": " + mensagem);

        // String[] partes = mensagem.split(" ");
        // String comando = partes[0];

        // switch (comando) {
        // case "JOGAR_CARTA":
        // if (partes.length > 1) {
        // int indiceCarta = Integer.parseInt(partes[1]);
        // gerenciadorComandos.jogarCartas(jogador, indiceCarta);
        // enviarMensagemParaTodos("Jogador " + nomeJogador + " jogou uma carta.");
        // }
        // break;

        // case "COMPRAR_CARTA":
        // gerenciadorComandos.comprarCarta(jogador);
        // enviarMensagemParaTodos("Jogador " + nomeJogador + " comprou uma carta.");
        // break;

        // case "SAIR":
        // gerenciadorComandos.sairDoJogo(jogador);
        // removerCliente(cliente);
        // enviarMensagemParaTodos("Jogador " + nomeJogador + " saiu do jogo.");
        // break;

        // default:
        // System.out.println("Comando inválido recebido: " + mensagem);
        // }
    }

    private void removerCliente(Jogador jogador) {
        enviarMensagemParaTodos("Jogador " + jogador.getNome() + " foi desconectado.");
        synchronized (jogadores) {
            jogadores.remove(jogador);
        }

        try {
            Socket cliente = jogador.getSocket();
            if (cliente.isConnected()) {
                cliente.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove um jogador da lista de conexões e fecha sua conexão.
     * Exibe uma mensagem no console ao removê-lo.
     */
    private void removerCliente(Socket cliente) {
        synchronized (jogadores) {
            for (Jogador jogador : jogadores) {
                if (jogador.getSocket().equals(cliente)) {
                    enviarMensagemParaTodos("Jogador " + jogador.getNome() + " foi desconectado.");
                    jogadores.remove(jogador);

                }
            }

            if (cliente.isConnected()) {
                try {
                    cliente.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Envia uma mensagem para todos os clientes conectados.
     * 
     * @param mensagem Mensagem a ser enviada
     */
    private void enviarMensagemParaTodos(String mensagem) {
        System.out.println(mensagem);
        synchronized (jogadores) {
            for (Jogador jogador : jogadores) {
                jogador.enviarMensagem(mensagem);
            }
        }
    }


    /**
     * Método principal que inicializa o servidor.
     */
    public static void main(String[] args) {
        ServidorJogo servidorJogo = new ServidorJogo();
        servidorJogo.iniciarServidor();




    }

}
