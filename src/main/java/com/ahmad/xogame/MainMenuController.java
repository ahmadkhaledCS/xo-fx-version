package com.ahmad.xogame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class MainMenuController {
    @FXML
    Button startButton;
    @FXML
    protected void startGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainMenu.class.getResource("Game.fxml"));
        Stage stage = (Stage) startButton.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setScene(scene);
        stage.show();
    }
}