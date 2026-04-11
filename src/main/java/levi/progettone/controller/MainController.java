package levi.progettone.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import levi.progettone.model.ColorePedina;
import levi.progettone.model.Pedina;
import levi.progettone.model.Scacchiera;

public class MainController {

    @FXML
    private GridPane griglia;

    private Scacchiera scacchiera = new Scacchiera();
    private static final int DIM_TAVOLA = 8;
    private static final int DIM_CASELLA = 60;

    @FXML
    public void initialize() {
        aggiornaGrafica();
    }

    private void aggiornaGrafica() {
        griglia.getChildren().clear();

        griglia.getRowConstraints().clear();
        griglia.getColumnConstraints().clear();

        for (int r = 0; r < DIM_TAVOLA; r++) {
            for (int c = 0; c < DIM_TAVOLA; c++) {
                StackPane casella = new StackPane();


                casella.setMinSize(DIM_CASELLA, DIM_CASELLA);
                casella.setMaxSize(DIM_CASELLA, DIM_CASELLA);
                casella.setPrefSize(DIM_CASELLA, DIM_CASELLA);

                if ((r + c) % 2 == 0) {
                    casella.setStyle("-fx-background-color: #f0d9b5;");
                } else {
                    casella.setStyle("-fx-background-color: #b58863;");
                }

                Pedina p = scacchiera.getPedina(r, c);
                if (p != null) {
                    Circle cerchio = creaCerchioPedina(p);
                    casella.getChildren().add(cerchio);
                }


                griglia.add(casella, c, r);
            }
        }
    }


    private Circle creaCerchioPedina(Pedina p) {
        Circle c = new Circle(DIM_CASELLA * 0.4);

        if (p.getColore() == ColorePedina.BIANCO) {
            c.setFill(Color.WHITE);
            c.setStroke(Color.LIGHTGREY);
        } else {
            c.setFill(Color.BLACK);
            c.setStroke(Color.DARKGREY);
        }

        c.setStrokeWidth(2);
        return c;
    }
}





