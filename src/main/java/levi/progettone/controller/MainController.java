package levi.progettone.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
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

public class MainController {

    @FXML
    private GridPane griglia;

    private Scacchiera scacchiera = new Scacchiera();

    private static final int DIM_TAVOLA = 8;
    private static final int DIM_CASELLA = 60;

    // Coordinate della pedina selezionata
    private int selRiga = -1;
    private int selColonna = -1;

    @FXML
    public void initialize() {
        aggiornaGrafica();
    }

    /**
     * Ridisegna tutta la scacchiera.
     */
    private void aggiornaGrafica() {
        griglia.getChildren().clear();

        for (int r = 0; r < DIM_TAVOLA; r++) {
            for (int c = 0; c < DIM_TAVOLA; c++) {

                StackPane casella = new StackPane();
                casella.setMinSize(DIM_CASELLA, DIM_CASELLA);
                casella.setAlignment(Pos.CENTER);

                // Colori alternati
                if ((r + c) % 2 == 0)
                    casella.setStyle("-fx-background-color: #f0d9b5;");
                else
                    casella.setStyle("-fx-background-color: #b58863;");

                // Rende la casella cliccabile
                final int rr = r;
                final int cc = c;
                casella.setOnMouseClicked(e -> gestisciClick(rr, cc));

                // Disegna pedina se presente
                Pedina p = scacchiera.getPedina(r, c);
                if (p != null) {
                    boolean selezionata = (r == selRiga && c == selColonna);
                    Circle cerchio = creaCerchioPedina(p, selezionata);
                    casella.getChildren().add(cerchio);
                }

                griglia.add(casella, c, r);
            }
        }
    }

    /**
     * Crea il cerchio grafico della pedina.
     */
    private Circle creaCerchioPedina(Pedina p, boolean selezionata) {
        Circle c = new Circle(DIM_CASELLA * 0.4);

        if (p.getColore() == ColorePedina.BIANCO) {
            c.setFill(Color.WHITE);
            c.setStroke(Color.LIGHTGRAY);
        } else {
            c.setFill(Color.BLACK);
            c.setStroke(Color.DARKGRAY);
        }

        // Evidenziazione selezione
        if (selezionata) {
            c.setStroke(Color.YELLOW);
            c.setStrokeWidth(4);
        } else {
            c.setStrokeWidth(2);
        }

        return c;
    }

    /**
     * Gestisce il click su una casella.
     */
    private void gestisciClick(int r, int c) {

        Pedina p = scacchiera.getPedina(r, c);

        // Caso 1: seleziono una pedina
        if (selRiga == -1 && p != null) {
            selRiga = r;
            selColonna = c;
            aggiornaGrafica();
            return;
        }

        // Caso 2: clicco sulla stessa pedina → deseleziono
        if (selRiga == r && selColonna == c) {
            selRiga = selColonna = -1;
            aggiornaGrafica();
            return;
        }

        // Caso 3: provo a muovere
        if (selRiga != -1) {
            provaMuovi(selRiga, selColonna, r, c);
            selRiga = selColonna = -1;
            aggiornaGrafica();
        }
    }

    /**
     * Logica base del movimento (senza mangiate).
     */
    private void provaMuovi(int daR, int daC, int aR, int aC) {

        Pedina p = scacchiera.getPedina(daR, daC);
        if (p == null) return;

        int dir = (p.getColore() == ColorePedina.BIANCO) ? -1 : 1;

        // Movimento semplice diagonale
        if (aR == daR + dir && Math.abs(aC - daC) == 1 && scacchiera.getPedina(aR, aC) == null) {

            animaMovimento(daR, daC, aR, aC);
            scacchiera.muoviPedina(daR, daC, aR, aC);
        }
    }

    private void animaMovimento(int daR, int daC, int aR, int aC) {

        Node nodo = getNode(daR, daC);
        if (nodo == null) return;

        Circle pedinaGrafica = (Circle) ((StackPane) nodo).getChildren().get(0);

        double startX = daC * DIM_CASELLA + DIM_CASELLA / 2.0;
        double startY = daR * DIM_CASELLA + DIM_CASELLA / 2.0;
        double endX = aC * DIM_CASELLA + DIM_CASELLA / 2.0;
        double endY = aR * DIM_CASELLA + DIM_CASELLA / 2.0;

        Path path = new Path();
        path.getElements().add(new MoveTo(startX, startY));
        path.getElements().add(new LineTo(endX, endY));

        PathTransition pt = new PathTransition();
        pt.setDuration(Duration.millis(300));
        pt.setNode(pedinaGrafica);
        pt.setPath(path);
        pt.setOnFinished(e -> aggiornaGrafica());
        pt.play();
    }

    /**
     * Recupera il nodo grafico in una cella della GridPane.
     */
    private Node getNode(int r, int c) {
        for (Node n : griglia.getChildren()) {
            if (GridPane.getRowIndex(n) == r && GridPane.getColumnIndex(n) == c) {
                return n;
            }
        }
        return null;
    }
}
