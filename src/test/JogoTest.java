package test;

import model.CartaEspecial;
import model.CartaNumerica;
import model.Jogador;
import model.Jogo;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class JogoTest {

    @Test
    public void testVitoriaJogador() {
        // Criamos um jogador fictício e um jogo com ele
        Jogador jogador = new Jogador("Alice");
        List<Jogador> jogadores = new ArrayList<>();
        jogadores.add(jogador);
        Jogo jogo = new Jogo(jogadores);

        // Removemos todas as cartas da mão do jogador (simulando que ele jogou todas)
        jogador.getMao().clear();

        // Verificamos se o jogo detecta que ele venceu
        assertTrue("O jogo deve reconhecer que Alice venceu.", jogo.verificarVitoria(jogador));
    }

    @Test
    public void testDescargaAtivada() {
        Jogador jogador = new Jogador("Bob");
        List<Jogador> jogadores = new ArrayList<>();
        jogadores.add(jogador);
        Jogo jogo = new Jogo(jogadores);

        // Simulamos que o jogador joga três cartas da mesma cor na pilha privada
        jogo.pilhaPrivada.add(new CartaNumerica("Amarelo", 2));
        jogo.pilhaPrivada.add(new CartaNumerica("Amarelo", 3));
        jogo.pilhaPrivada.add(new CartaEspecial("Amarelo", CartaEspecial.Tipo.PULAR));

        // Agora, verificamos se a Descarga será acionada
        assertTrue("A descarga deveria ser ativada.", jogo.verificarDescarga(jogador));
    }
}
