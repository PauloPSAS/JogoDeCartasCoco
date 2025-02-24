package model;

// Cartas Cocô numéricas: possuem valor de 0 a 4
public class CartaNumerica extends Carta {
    private int valor;

    public CartaNumerica(String cor, int valor) {
        super(cor);
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
