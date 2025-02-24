package model;

// Cartas Privada: usadas para definir o "Número de Entupimento"
// São todas de cor "Preto" e possuem valores de 8 a 12
public class CartaPrivada extends Carta {
    private int valor;

    public CartaPrivada(int valor) {
        super("Preto");
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return String.valueOf(valor);
    }
}
