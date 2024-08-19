package com.isruan.battleshipz.Client.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController implements Initializable{
    @FXML
    private TextField fieldAdres;
    @FXML
    private TextField fieldPort;
    @FXML
    private Button connectButton;

    private GameService service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = new GameService();

    }
    @FXML
    private void connect(ActionEvent event) throws IOException {
//        ClientViewController clientViewController = new ClientViewController();
//        clientViewController.logIn(ActionEvent event);
        String address;
        int port;
        try {
            address = this.fieldAdres.getText();
            port = Integer.parseInt(this.fieldPort.getText());
        }
        catch (NumberFormatException e){
            e.printStackTrace();
            System.err.println(e);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Number format");
            alert.setHeaderText(null);
            alert.setContentText("The port value you enter is not a numeric format.");
            alert.showAndWait();
            return;
        }
        boolean result = this.service.tryConnect(address,port);
        if (result) {
            Stage stage = (Stage) this.connectButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/isruan/battleshipz/ClientView.fxml"));
            Parent root = loader.load();

            ClientViewController clientController = loader.getController();
            clientController.setServiceAndStage(this.service, stage);

            // Set the scene with a preferred size
            Scene scene = new Scene(root, 1200, 800);
            stage.setScene(scene);

            stage.setResizable(true); // Allow resizing if not in full-screen
            stage.show();
        }

        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Connection error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to connect to the server. Please check the data you entered and try again.");
            alert.showAndWait();
        }
    }

}
