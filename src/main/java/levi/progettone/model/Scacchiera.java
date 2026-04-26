package levi.progettone.model;

public class Scacchiera {

    private Pedina[][] griglia = new Pedina[8][8];

    public Scacchiera() {
        inizializza();
    }

    private void inizializza() {

        // Pedine nere in alto
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 8; c++) {
                if ((r + c) % 2 == 1) {
                    griglia[r][c] = new Pedina(ColorePedina.NERO);
                }
            }
        }

        // Pedine bianche in basso
        for (int r = 5; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if ((r + c) % 2 == 1) {
                    griglia[r][c] = new Pedina(ColorePedina.BIANCO);
                }
            }
        }
    }

    public Pedina getPedina(int r, int c) {
        return griglia[r][c];
    }

    public void muoviPedina(int daR, int daC, int aR, int aC) {
        griglia[aR][aC] = griglia[daR][daC];
        griglia[daR][daC] = null;
    }

    public void rimuoviPedina(int r, int c) {
        griglia[r][c] = null;
    }
}
