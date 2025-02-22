public class Carta {
    private int numero;     // Número da carta (para cartas numéricas)
    private String cor;     // Cor da carta (Amarelo, Vermelho, Roxo, Preto)
    private String tipo;    // Tipo da carta (Normal, Pular, Reverter, Privada)

    // Construtor para cartas Cocô normais
    public Carta(int numero, String cor) {
        this.numero = numero;
        this.cor = cor;
        this.tipo = "Normal";
    }

    // Construtor para cartas especiais (Pular e Reverter)
    public Carta(String tipo, String cor) {
        this.tipo = tipo;
        this.cor = cor;
        this.numero = 0;    // Especiais possuem valor 0 para entupimento, mas não são reveladas
    }

    // Construtor para cartas Privadas
    public static Carta criarCartaPrivada(int numero) {
        return new Carta(numero, "Preto");
    }

    //Getters
    public int getNumero() {
        return numero;
    }

    public String getCor() {
        return cor;
    }

    public String getTipo() {
        return tipo;
    }

    // Verifica se a carta é especial
    public boolean isEspecial() {
        return tipo.equals("Pular") || tipo.equals("Reverter");
    }

    @Override
    public String toString() {
        if (tipo.equals("Normal")) {
            return "Carta Cocô: " + numero + " - Cor: " + cor;
        } else if (tipo.equals("Privada")) {
            return "Carta Privada: " + numero;
        } else {
            return "Carta especial: " + tipo + " - Cor: " + cor;
        }
    }
}
