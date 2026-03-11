package levi.progettone.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import levi.progettone.model.ColorePedina;
import levi.progettone.model.Pedina;
import levi.progettone.model.Scacchiera;


public class MainController {

    private static final int DIM_CASELLA = 100;
    private static final int DIM_TAVOLA = 8;

    private Scacchiera scacchiera = new Scacchiera();

    @FXML
    private ImageView immagineScacchiera;

    @FXML
    private Pane livelloPedine;

    @FXML
    public void initialize() {
        immagineScacchiera.setImage(
                new Image(getClass().getResourceAsStream("/levi/progettone/views/images/scacchiera.jpg"))
        );

        disegnaPedine();
    }

    private void disegnaPedine() {
        livelloPedine.getChildren().clear();

        for (int r = 0; r < DIM_TAVOLA; r++) {
            for (int c = 0; c < DIM_TAVOLA; c++) {

                Pedina p = scacchiera.getPedina(r, c);
                if (p != null) {
                    livelloPedine.getChildren().add(creaNodoPedina(p, r, c));
                }
            }
        }
    }

    private Circle creaNodoPedina(Pedina pedina, int riga, int colonna) {

        double x = colonna * DIM_CASELLA + DIM_CASELLA / 2.0;
        double y = riga * DIM_CASELLA + DIM_CASELLA / 2.0;

        Circle cerchio = new Circle(DIM_CASELLA * 0.35);

        if (pedina.getColore() == ColorePedina.BIANCO) {
            cerchio.setFill(Color.BEIGE);
        } else {
            cerchio.setFill(Color.DARKRED);
        }

        cerchio.setStroke(Color.BLACK);
        cerchio.setStrokeWidth(2);

        cerchio.setTranslateX(x);
        cerchio.setTranslateY(y);

        return cerchio;
    }
}





