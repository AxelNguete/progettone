package levi.progettone.model;

public class Scacchiera {
    private Pedina[][] griglia = new Pedina[8][8];

    public Scacchiera(){
        inizializza();

    }

    private void inizializza(){
        // Pedine nere in alto
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 8; c++){
                if((r + c) % 2 == 1){
                    griglia[r][c] = new Pedina(ColorePedina.BLACK);
                }
            }

        }

        // Pedine bianche in basso
        for(int r = 5; r < 8; r++){
            for(int c = 0; c < 8; c++){
                if((r + c) % 2 == 1){
                    griglia[r][c] = new Pedina(ColorePedina.WHITE);
                }
            }
        }
    }

    public Pedina getPedina(int riga, int colonna){
        return griglia[riga][colonna];
    }

    public void muoviPedina(int daRiga, int daColonna, int aRiga, int aColonna){
        griglia[aRiga][aColonna] = griglia[daRiga][daColonna];
        griglia[daRiga][daColonna] = null;
    }

    public void rimuoviPedina(int riga, int colonna){
        griglia[riga][colonna] = null;
    }
}
