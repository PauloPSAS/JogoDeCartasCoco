package model;

// Classe base para todas as cartas
public abstract class Carta {
    protected String cor;

    public Carta(String cor) {
        this.cor = cor;
    }

    public String getCor() {
        return cor;
    }

    public abstract String toString();
}
