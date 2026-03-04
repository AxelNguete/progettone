package levi.progettone.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

    public class MainController {
        @FXML private Label welcomeText;
        @FXML private GridPane Scacchiera;

        @FXML
        protected void onHelloButtonClick() {
            welcomeText.setText("");
            // logica scacchiera
            //Giocatore seleziona pezzo
            //Sistema mostra mosse valide
            //Giocatore clicca destinazione
            //Pezzo si muove
            //Se cattura, nemico scompare
            //Se ne mangia di piu , ha piu dame
            //Turno passa all'altro giocatore
            //Se uno ha 0 pezzi, game over
        }
    }





