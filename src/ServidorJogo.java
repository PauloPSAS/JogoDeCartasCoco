import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServidorJogo {
    private static final int PORTA = 12345;             // Porta fixa usada para aceitar conexões dos clientes
    private static List<Jogador> jogadores;                    // Lista de jogadores conectados
    private Map<Socket, Jogador> mapaJogadores;        // Mapeia sockets para objeto Jogador
    private ServerSocket servidor;                      // Socket do servidor que aguarda conexões
    private boolean jogoIniciado;                       // Indica se o jogo já foi iniciado
    private GerenciadorComandos gerenciadorComandos;// Responsável por processar comandos dos jogadores
    private  Turno turno;


    public ServidorJogo() {
        this.jogadores = new ArrayList<>();
        this.mapaJogadores = new HashMap<>();
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
     * Quando dois jogadores se conectam, inicia a contagem regressiva para iniciar o jogo.
     * Se cinco jogadores se conectarem antes do tempo acabar, o jogo inicia automaticamente.
     */
    private void aguardarConexoes() {
        System.out.println("Aguardando jogadores");
        new Thread(() -> {
            while (!jogoIniciado) {
                try {
                    Socket cliente = servidor.accept();

                    System.out.println("Novo jogador conectado: " + cliente.getInetAddress().getHostAddress());

                    new Thread(() -> lidarComCliente(cliente)).start();

                    synchronized (mapaJogadores) {
                        if (mapaJogadores.size() == 2) {
                            iniciarContagemJogadores();
                        } else if (mapaJogadores.size() == 5) {
                            iniciarJogo();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Quando dois jogadores se conectam, inicia uma contagem regressiva de 1 minuto.
     * Se mais jogadores entrarem antes do tempo expirar, o jogo os inclui.
     * Se menos de dois jogadores estiverem conectados após 1 minuto, o servidor é encerrado.
     */
    private void iniciarContagemJogadores() {
        System.out.println("Dois jogadores conectados. Iniciando contagem de 1 minuto para mais jogadores...");
        new Thread(() -> {
            try {
                Thread.sleep(60000);    // Espera 1 minuto
                synchronized (mapaJogadores) {
                    if (mapaJogadores.size() >= 2 && mapaJogadores.size() < 5) {
                        iniciarJogo();
                    } else if (mapaJogadores.size() < 2) {
                        System.out.println("Jogadores insuficientes. Encerrando servidor.");
                        encerrarServidor();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Inicia o jogo quando há jogadores suficientes conectados.
     * Exibe a mensagem de início do jogo.
     */
    private void iniciarJogo() {
        jogoIniciado = true;
        System.out.println("Jogo iniciado com " + mapaJogadores.size() + " jogadores!");
    }

    /**
     * Encerra o servidor, fechando todas as conexões e limpando a lista de jogadores conectados.
     */
    private void encerrarServidor() {
        try {
            synchronized (mapaJogadores) {
                for (Socket cliente : mapaJogadores.keySet()) {
                    cliente.close();
                }
                mapaJogadores.clear();
                jogadores.clear();
            }
            servidor.close();
            System.out.println("Servidor encerrado.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lida com um cliente conectado, recebendo e processando mensagens.
     * Mantém a conexão aberta até que o cliente desconecte.
     */
    private void lidarComCliente(Socket cliente) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);

            String mensagemInicial = in.readLine();
            if (mensagemInicial.startsWith("NOME ")) {
                String nome = mensagemInicial.substring(5).trim();
                Jogador jogador = new Jogador(nome);
                synchronized (mapaJogadores) {
                    mapaJogadores.put(cliente, jogador);
                    jogadores.add(jogador);
                }
                enviarMensagemParaTodos("Jogador " + nome + " entrou na partida.");
            }

            String mensagem;
            while ((mensagem = in.readLine()) != null) {
                processarMensagem(cliente, mensagem);
            }
        } catch (IOException e) {
            removerCliente(cliente);
        }
    }

    /**
     * Processa as mensagens recebidas dos clientes.
     * Interpreta comandos e executa ações apropriadas.
     */
    private void processarMensagem(Socket cliente, String mensagem) {
        Jogador jogador = mapaJogadores.get(cliente);
        String nomeJogador = (jogador != null) ? jogador.getNome() : "Desconhecido";
        System.out.println("Mensagem recebida de " + nomeJogador + ": " + mensagem);
        enviarMensagemParaTodos(nomeJogador + ": " + mensagem);

        String[] partes = mensagem.split(" ");
        String comando = partes[0];

        switch (comando) {
            case "JOGAR_CARTA":
                if (partes.length > 1) {
                    int indiceCarta = Integer.parseInt(partes[1]);
                    gerenciadorComandos.jogarCartas(jogador, indiceCarta);
                    enviarMensagemParaTodos("Jogador " + nomeJogador + " jogou uma carta.");
                }
                break;

            case "COMPRAR_CARTA":
                gerenciadorComandos.comprarCarta(jogador);
                enviarMensagemParaTodos("Jogador " + nomeJogador + " comprou uma carta.");
                break;

            case "SAIR":
                gerenciadorComandos.sairDoJogo(jogador);
                removerCliente(cliente);
                enviarMensagemParaTodos("Jogador " + nomeJogador + " saiu do jogo.");
                break;

            default:
                System.out.println("Comando inválido recebido: " + mensagem);
        }
    }

    /**
     * Remove um jogador da lista de conexões e fecha sua conexão.
     * Exibe uma mensagem no console ao removê-lo.
     */
    private void removerCliente(Socket cliente) {
        synchronized (mapaJogadores) {
            Jogador jogadorRemovido = mapaJogadores.remove(cliente);
            if (jogadorRemovido != null) {
                jogadores.remove(jogadorRemovido);
                enviarMensagemParaTodos("Jogador " + jogadorRemovido.getNome() + " foi desconectado.");
                System.out.println("Jogador " + jogadorRemovido.getNome() + " removido do jogo.");
            }
        }
        try {
            cliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envia uma mensagem para todos os clientes conectados.
     * @param mensagem Mensagem a ser enviada
     */
    private void enviarMensagemParaTodos(String mensagem) {
        synchronized (mapaJogadores) {
            for (Socket cliente : mapaJogadores.keySet()) {
                try {
                    PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
                    out.println(mensagem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void processarJogada(ClienteJogo jogador){
        if(turno.getJogadorAtual()==jogador) {
            System.out.println("jogador fez uma jogada!");
            turno.proximoTurno();
            turno.notificarJogadores();
        } else {
            jogador.enviarMensagem("Não é sua vez");
        }
    }

    /**
     * Método principal que inicializa o servidor.
     */
    public static void main(String[] args)  {
        ServidorJogo servidorJogo = new ServidorJogo();
        servidorJogo.iniciarServidor();
    }


}
