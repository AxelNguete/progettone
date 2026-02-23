module levi.progettone {
    requires javafx.controls;
    requires javafx.fxml;


    opens levi.progettone to javafx.fxml;
    exports levi.progettone;
    exports levi.progettone.model;
    opens levi.progettone.model to javafx.fxml;
    exports levi.progettone.controller;
    opens levi.progettone.controller to javafx.fxml;
}