import java.util.*;

public class Turno {
    private List<ClienteJogo> jogadores;
    private int indiceAtual;

    public Turno() {
        this.jogadores = jogadores;
        this.indiceAtual = 0;
        Collections.shuffle(this.jogadores); // Sorteia quem começa
    }

    public ClienteJogo getJogadorAtual() {
        return jogadores.get(indiceAtual);
    }

    public void proximoTurno() {
        indiceAtual = (indiceAtual + 1) % jogadores.size();
    }

    public void notificarJogadores() {
        for (int i = 0; i < jogadores.size(); i++) {
            if (i == indiceAtual) {
                jogadores.get(i).enviarMensagem("SUA_VEZ");
            } else {
                jogadores.get(i).enviarMensagem("AGUARDE");
            }
        }
    }
}








/**
public enum Turno {
    J1(0), J2(1), J3(2), J4(3), J5(4);

    private final int idJogador;

    Turno(int idJogador) {
        this.idJogador = idJogador;
    }

    public int getIdJogador() {
        return this.idJogador;
    }

    public static Turno proximoTurno(Turno turnoAtual) {
        int novoTurno = (turnoAtual.ordinal() + 1) % Turno.values().length;
        return Turno.values()[novoTurno];
    }

    public String printTurno() {
        return "JOGADOR " + (idJogador + 1);
    }
}*/
