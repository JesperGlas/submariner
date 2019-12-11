package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML
    private Button startBtn;
    @FXML
    private Button exitBtn;

    public void onButtonClicked(ActionEvent e) {
        System.out.println("Button pressed: " + e.getSource().toString());
    }
}
