package model;

// Cartas Cocô especiais: Pular (P) e Reverter (R)
public class CartaEspecial extends Carta {
    public enum Tipo {
        PULAR,
        REVERTER
    }
    private Tipo tipo;

    public CartaEspecial(String cor, Tipo tipo) {
        super(cor);
        this.tipo = tipo;
    }

    public Tipo getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return (tipo == Tipo.PULAR ? "P" : "R");
    }
}
