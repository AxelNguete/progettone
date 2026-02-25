package levi.progettone.model;

import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pedina extends StackPane {
    private int riga, colonna;
    private Color colore;
    private Circle cerchio;
    private boolean selezionata = false;
    private Runnable onClickCallback;

    public Pedina(Color colore, int riga, int colonna, Runnable onClickCallback) {
        this.colore = colore;
        this.riga = riga;
        this.colonna = colonna;
        this.onClickCallback = onClickCallback;

        this.cerchio = new Circle(35);
        cerchio.setFill(colore);
        cerchio.setStroke(Color.GRAY);
        cerchio.setStrokeWidth(2);

        // Effetto ombra
        InnerShadow shadow = new InnerShadow();
        shadow.setColor(Color.DARKGRAY);
        cerchio.setEffect(shadow);

        getChildren().add(cerchio);

        // Gestione click
        setOnMouseClicked(this::handleClick);
    }

    private void handleClick(MouseEvent event) {
        if (onClickCallback != null) {
            onClickCallback.run();
        }
    }

    public void seleziona() {
        selezionata = true;
        cerchio.setStroke(Color.YELLOW);
        cerchio.setStrokeWidth(4);
    }

    public void deseleziona() {
        selezionata = false;
        cerchio.setStroke(Color.GRAY);
        cerchio.setStrokeWidth(2);
    }

    public boolean isSelezionata() {
        return selezionata;
    }

    public int getRiga() { return riga; }
    public int getColonna() { return colonna; }
    public Color getColore() { return colore; }

    public void setPosizione(int riga, int colonna) {
        this.riga = riga;
        this.colonna = colonna;
    }
}
