package com.isruan.battleshipz.Server.Controller;

import com.isruan.battleshipz.Utils.Command;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerViewController implements Initializable {
    @FXML
    private TextField portField;
    @FXML
    private TextField playersField;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button closeButton;

    private Server server;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.closeButton.setDisable(true);
        this.stopButton.setDisable(true);
    }

    @FXML
    private void start(ActionEvent event){

        int port;
        if(this.portField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong port");
            alert.setContentText("This field cannot be blank!!");
            alert.showAndWait();
            return;
        }
        else {
            try {
                port = Integer.parseInt(this.portField.getText());
            }
            catch (NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Wrong port");
                alert.setContentText("The port value you enter is not recognized as a numeric value!!");
                alert.showAndWait();
                return;
            }

            if(port <= 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Wrong port");
                alert.setContentText("The port cannot be specified by a negative number!!");
                alert.showAndWait();
                return;
            }

            server = Server.getInstance();
            server.setPort(port);
            server.setController(this);
            server.start();
            this.portField.setDisable(true);
            this.startButton.setDisable(true);
            this.stopButton.setDisable(false);
        }
    }

    @FXML
    private void stop(ActionEvent event){
        server.sendBroadcast(Command.SERVER_SHUTDOWN.toString());
        this.stopButton.setDisable(true);
        this.closeButton.setDisable(false);
    }

    @FXML
    private void close(ActionEvent event){
        server.kill();
        System.exit(0);
    }

    public void updatePlayers(int value){
        this.playersField.setText(Integer.toString(value));
    }
}
