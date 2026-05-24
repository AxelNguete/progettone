package levi.progettone.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.animation.PathTransition;
import javafx.util.Duration;

import levi.progettone.model.ColorePedina;
import levi.progettone.model.Pedina;
import levi.progettone.model.Scacchiera;
import levi.progettone.model.TipoPedina;

/**
Si occupa di:
- disegnare la scacchiera e le pedine (aggiornaGrafica)
- gestire i click del giocatore (gestisciClick)
- validare e applicare le mosse (provaMuovi)
- animare il movimento delle pedine (animaMovimento)
- promuovere una pedina a damone quando raggiunge l'ultima riga avversaria
*/

public class MainController {
    @FXML
    private GridPane griglia;

    private Scacchiera scacchiera = new Scacchiera();

    private ColorePedina turnoCorrente = ColorePedina.BIANCO;

    private static final int DIM_TAVOLA = 8;

    private static final int DIM_CASELLA = 60;

    private int selRiga = -1;

    private int selColonna = -1;

    @FXML
    public void initialize() {
        aggiornaGrafica();
    }

    @FXML
    public void nuovaPartita() {
        scacchiera = new Scacchiera();
        turnoCorrente = ColorePedina.BIANCO;
        selRiga = -1;
        selColonna = -1;
        System.out.println("Nuova partita iniziata. Turno: Bianco");
        griglia.setDisable(false);
        aggiornaGrafica();
    }

//  Ridisegna completamente la scacchiera.
    public void aggiornaGrafica() {
        griglia.getChildren().clear();

        for (int r = 0; r < DIM_TAVOLA; r++) {
            for (int c = 0; c < DIM_TAVOLA; c++) {

                StackPane casella = new StackPane();
                casella.setMinSize(DIM_CASELLA, DIM_CASELLA);
                casella.setAlignment(Pos.CENTER);

                // Caselle chiare e scure alternate
                if ((r + c) % 2 == 0)
                    casella.setStyle("-fx-background-color: #f0d9b5;");
                else
                    casella.setStyle("-fx-background-color: #b58863;");

                // Ogni casella reagisce al click del mouse
                final int rr = r;
                final int cc = c;
                casella.setOnMouseClicked(e -> gestisciClick(rr, cc));

                Pedina p = scacchiera.getPedina(r, c);
                if (p != null) {
                    boolean selezionata = (r == selRiga && c == selColonna);
                    Node graficaPedina = creaGraficaPedina(p, selezionata);
                    casella.getChildren().add(graficaPedina);
                }

                griglia.add(casella, c, r);
            }
        }
    }

//  Crea la pedina e il damone
    public Node creaGraficaPedina(Pedina p, boolean selezionata) {
        Color riempimento;
        Color bordo;
        if (p.getColore() == ColorePedina.BIANCO) {
            riempimento = Color.WHITE;
            bordo = Color.LIGHTGRAY;
        } else {
            riempimento = Color.BLACK;
            bordo = Color.DARKGRAY;
        }
        Color bordoSel = Color.YELLOW;
        double raggio  = DIM_CASELLA * 0.38;

        if (p.getTipo() == TipoPedina.DAMA) {
            // Cerchio normale
            Circle sotto = new Circle(raggio);
            sotto.setFill(riempimento);
            sotto.setTranslateY(5);
            if (selezionata) {
                sotto.setStroke(bordoSel);
                sotto.setStrokeWidth(4);
            } else {
                sotto.setStroke(bordo);
                sotto.setStrokeWidth(2);
            }

            // Cerchio più piccolo, spostato in alto
            Circle sopra = new Circle(raggio * 0.78);
            sopra.setFill(riempimento);
            sopra.setTranslateY(-5);
            if (selezionata) {
                sopra.setStroke(bordoSel);
                sopra.setStrokeWidth(3);
            } else {
                sopra.setStroke(bordo);
                sopra.setStrokeWidth(1.5);
            }

            return new Group(sotto, sopra);
        }

        // Pedina normale: cerchio singolo
        Circle c = new Circle(raggio);
        c.setFill(riempimento);
        if (selezionata) {
            c.setStroke(bordoSel);
            c.setStrokeWidth(4);
        } else {
            c.setStroke(bordo);
            c.setStrokeWidth(2);
        }
        return c;
    }

//  Gestisce il click su una casella della scacchiera.
    public void gestisciClick(int r, int c) {

        Pedina p = scacchiera.getPedina(r, c);

        // Caso 1: nessuna pedina selezionata → seleziono la pedina cliccata (solo se è del giocatore di turno)
        if (selRiga == -1 && p != null && p.getColore() == turnoCorrente) {
            selRiga = r;
            selColonna = c;
            aggiornaGrafica();
            return;
        }

        // Caso 2: clicco sulla stessa pedina già selezionata → la deseleziono
        if (selRiga == r && selColonna == c) {
            selRiga = selColonna = -1;
            aggiornaGrafica();
            return;
        }

        // Caso 3: c'è una pedina selezionata → provo a spostarla nella casella cliccata
        if (selRiga != -1) {
            provaMuovi(selRiga, selColonna, r, c);
            selRiga = selColonna = -1;
            aggiornaGrafica();
        }
    }

    //  Valida e applica la mossa dalla casella (daR, daC) alla casella (aR, aC).
    public void provaMuovi(int daR, int daC, int aR, int aC) {

        Pedina p = scacchiera.getPedina(daR, daC);
        if (p == null) return;

        boolean moved  = false;
        boolean isDama = (p.getTipo() == TipoPedina.DAMA);

        int dir;
        if (p.getColore() == ColorePedina.BIANCO) {
            dir = -1;
        } else {
            dir = 1;
        }

        // Una mossa semplice è valida se si sposta di 1 diagonale in una direzione consentita
        boolean mossaSempliceValida;
        if (isDama) {
            // Il damone può muoversi in tutte e 4 le direzioni diagonali
            mossaSempliceValida = Math.abs(aR - daR) == 1 && Math.abs(aC - daC) == 1;
        } else {
            // La pedina normale può muoversi solo in avanti (nella direzione di dir)
            mossaSempliceValida = aR == daR + dir && Math.abs(aC - daC) == 1;
        }

        if (mossaSempliceValida && scacchiera.getPedina(aR, aC) == null) {
            animaMovimento(daR, daC, aR, aC);
            scacchiera.muoviPedina(daR, daC, aR, aC);
            moved = true;

        } else if (Math.abs(aR - daR) == 2 && Math.abs(aC - daC) == 2 && scacchiera.getPedina(aR, aC) == null) {
            // Salto di 2 caselle: mangiata
            int midR = (daR + aR) / 2; // riga della pedina in mezzo
            int midC = (daC + aC) / 2; // colonna della pedina in mezzo
            Pedina pedinaInMezzo = scacchiera.getPedina(midR, midC);

            // La pedina normale non può mangiare un damone
            boolean saltoValido = false;
            if (pedinaInMezzo != null && pedinaInMezzo.getColore() != p.getColore()) {
                if (isDama || pedinaInMezzo.getTipo() != TipoPedina.DAMA) {
                    saltoValido = true;
                }
            }

            if (saltoValido) {
                animaMovimento(daR, daC, aR, aC);
                scacchiera.muoviPedina(daR, daC, aR, aC);
                scacchiera.rimuoviPedina(midR, midC);
                moved = true;
            }
        }

        if (moved) {
            // Controlla se la pedina appena mossa deve essere promossa a damone
            Pedina pedinaArrivata = scacchiera.getPedina(aR, aC);
            if (pedinaArrivata != null && pedinaArrivata.getTipo() == TipoPedina.NORMALE) {
                if (pedinaArrivata.getColore() == ColorePedina.BIANCO && aR == 0) {
                    pedinaArrivata.promuoviDama();
                } else if (pedinaArrivata.getColore() == ColorePedina.NERO && aR == 7) {
                    pedinaArrivata.promuoviDama();
                }
            }

            // Passa il turno all'altro giocatore
            if (turnoCorrente == ColorePedina.BIANCO) {
                turnoCorrente = ColorePedina.NERO;
            } else {
                turnoCorrente = ColorePedina.BIANCO;
            }

            // Controllo condizione di vittoria: se un giocatore ha 0 pedine
            int bCount = scacchiera.contaPedine(ColorePedina.BIANCO);
            int nCount = scacchiera.contaPedine(ColorePedina.NERO);
            if (bCount == 0 || nCount == 0) {
                if (bCount == 0) {
                    log("Il Bianco ha esaurito le pedine. Il Nero ha vinto!");
                } else {
                    log("Il Nero ha esaurito le pedine. Il Bianco ha vinto!");
                }
                // Disabilita la scacchiera per evitare ulteriori mosse
                griglia.setDisable(true);
            }
        }
    }


//  Anima lo spostamento di una pedina da una casella a un'altra.
    public void animaMovimento(int daR, int daC, int aR, int aC) {

        Node nodo = getNode(daR, daC);
        if (nodo == null) return;

        Node pedinaGrafica = ((StackPane) nodo).getChildren().get(0);

        // Calcola le coordinate in pixel del centro delle caselle di partenza e arrivo
        double startX = daC * DIM_CASELLA + DIM_CASELLA / 2.0;
        double startY = daR * DIM_CASELLA + DIM_CASELLA / 2.0;
        double endX   = aC  * DIM_CASELLA + DIM_CASELLA / 2.0;
        double endY   = aR  * DIM_CASELLA + DIM_CASELLA / 2.0;

        Path path = new Path();
        path.getElements().add(new MoveTo(startX, startY));
        path.getElements().add(new LineTo(endX, endY));

        PathTransition pt = new PathTransition();
        pt.setDuration(Duration.millis(300));
        pt.setNode(pedinaGrafica);
        pt.setPath(path);
        pt.setOnFinished(e -> aggiornaGrafica()); // ridisegna tutto a fine animazione
        pt.play();
    }

    public void log(String msg) {
        System.out.println(msg);
    }


    public Node getNode(int r, int c) {
        for (Node n : griglia.getChildren()) {
            if (GridPane.getRowIndex(n) == r && GridPane.getColumnIndex(n) == c) {
                return n;
            }
        }
        return null;
    }
}
