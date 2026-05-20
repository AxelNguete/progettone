package levi.progettone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
TODO - funzionalità ancora da implementare:
Catena di mangiate: dopo una mangiata, se la stessa pedina può mangiarne
un'altra il turno deve continuare invece di passare all'avversario.

Evidenziare le mosse valide: quando si seleziona una pedina, colorare in
verde le caselle raggiungibili così il giocatore sa subito dove può muoversi.
*/


public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/main-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 560);
        stage.setTitle("Dama");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}