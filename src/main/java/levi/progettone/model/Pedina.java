package levi.progettone.model;


public class Pedina {

    private ColorePedina colore;
    private TipoPedina tipo;

    public Pedina(ColorePedina colore) {
        this.colore = colore;
        this.tipo = TipoPedina.NORMALE;
    }

    public TipoPedina getTipo() {
        return tipo;
    }

    public ColorePedina getColore() {
        return colore;
    }

    public void promuoviDama() {
        this.tipo = TipoPedina.DAMA;
    }
}