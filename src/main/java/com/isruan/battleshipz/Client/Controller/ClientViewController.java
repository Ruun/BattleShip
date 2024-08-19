package com.isruan.battleshipz.Client.Controller;

import com.isruan.battleshipz.GameController;
import com.isruan.battleshipz.application.AudioManager;
import com.isruan.battleshipz.Utils.Package;

import com.isruan.battleshipz.Client.Model.ClientCell;
import com.isruan.battleshipz.Client.Model.ClientSocket;
import com.isruan.battleshipz.Client.Model.EnemyBoard;
import com.isruan.battleshipz.Client.Model.PlayerBoard;
import com.isruan.battleshipz.Utils.Command;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientViewController implements Initializable {
    @FXML
    public Button swapButton;
    @FXML
    private TextField nickField;
    @FXML
    private Button loginButton;
    @FXML
    private TextField yourgameField;
    @FXML
    private ComboBox<String> gamesCombo;
    @FXML
    private Button joinButton;
    @FXML
    private Button createButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button buttonDeleteGame;
    @FXML
    private Button buttonGiveUp;
    @FXML
    private Button buttonOffer;
    //@FXML
    //private RadioButton radioPlacement;
    @FXML
    private RadioButton radioReady;
    @FXML
    private RadioButton radio1;
    @FXML
    private RadioButton radio2;
    @FXML
    private RadioButton radio3;
    @FXML
    private RadioButton radio4;
    @FXML
    private ToggleButton removeShip;
    @FXML
    private VBox VBoxMy;
    @FXML
    private VBox VBoxEnemy;
    @FXML
    private VBox sizeBox;
    @FXML
    private TextArea infoArea;
    @FXML
    private ListView<String> chatArea;
    @FXML
    private TextField chatField;
    @FXML
    private Button sendButton;

    boolean isMyBoardVisible =  true;

    private ToggleGroup radioGroup;
    private ToggleGroup sizeGroup;

    public PlayerBoard myBoard;
    public EnemyBoard enemyBoard;
    private String myName;
    private String myGame;
    private GameService gameService;
    private ClientSocket clientSocket;
    private Stage stage;

    private boolean shipPlacement = false;
    private boolean placementValidation = false;
    private boolean shooting = false;
    private boolean myTurn = false;
    private boolean removing = false;


    private ObservableList<String> gameList;
    private ObservableList<String> messageList;

    AudioManager audioManager = new AudioManager();

    //Initialize methods

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        VBoxMy.setVisible(true);
        VBoxEnemy.setVisible(false);

        GameController.exit();

        audioManager.playSoundEffect(
                "Sounds/bgMusic.wav");

        this.buttonDeleteGame.setDisable(true);
        this.createButton.setDisable(true);
        this.exitButton.setDisable(true);
        this.joinButton.setDisable(true);
        this.sendButton.setDisable(true);
        this.buttonOffer.setDisable(true);
        this.removeShip.setDisable(true);
        this.gameList = FXCollections.observableArrayList();
        this.messageList = FXCollections.observableArrayList();
        this.gamesCombo.setItems(this.gameList);
        this.chatArea.setItems(this.messageList);
        this.gamesCombo.setDisable(true);
        this.buttonGiveUp.setDisable(true);
        this.chatField.setDisable(true);
        this.radioGroup = new ToggleGroup();
        //this.radioPlacement.setToggleGroup(radioGroup);
       // this.radioPlacement.setSelected(true);
        this.radioReady.setToggleGroup(radioGroup);

        this.radioReady.setOnMouseClicked(event -> {
            this.clientSocket.sendMessage(Command.READY.toString());
            this.radioDeactivate();
            this.changeRemoveButtonStatus(true);
            this.shipPlacement = false;
        });
       // this.radioPlacement.setSelected(true);
        this.radio4.setUserData(4);
        this.radio3.setUserData(3);
        this.radio2.setUserData(2);
        this.radio1.setUserData(1);
        this.sizeGroup = new ToggleGroup();
        this.radio1.setToggleGroup(sizeGroup);
        this.radio2.setToggleGroup(sizeGroup);
        this.radio3.setToggleGroup(sizeGroup);
        this.radio4.setToggleGroup(sizeGroup);
        this.radio4.setSelected(true);
        this.radioDeactivate();
        this.radioSizesListener();
        this.radioSizeDeactivate();
        this.sizeBox.setDisable(true);
        initBoards();
        this.removeToggleAction();
    }

    private void initBoards(){


        myBoard = new PlayerBoard(event -> {
            if(!shipPlacement)
                return;
            if(placementValidation){
                setInfoColor(Color.INDIANRED);
                putInfo("Wait for the server's response before placing another ship");
                return;
            }
            if((myBoard.getShipsLeft() == 0) && !removing){
                setInfoColor(Color.INDIANRED);
                putInfo("You have placed the maximum number of ships!");
                return;
            }
            ClientCell cell = (ClientCell)event.getSource();
            if(removing){
                if(!cell.wasUsed()){
                    setInfoColor(Color.INDIANRED);
                    putInfo("There is no ship in this field. Click on the box that contains the ship!");
                    return;
                }
                Package pack = new Package(Command.REMOVE_SHIP.toString(),cell.getXCoordinate(),cell.getYCoordinate());
                this.clientSocket.sendMessage(pack.toString());
                setPlacementValidation(true);
                return;
            }
            if(cell.wasUsed()){
                return;
            }
            myBoard.setCurrentCell(cell);
            boolean vertical = (event.getButton() == MouseButton.PRIMARY);
            myBoard.setCurrentVertical(vertical);
            Package pack = new Package(Command.PLACE_A_SHIP.toString(),Boolean.toString(vertical),cell.getXCoordinate(),cell.getYCoordinate(),myBoard.getCurrentPlacingSize());
            this.clientSocket.sendMessage(pack.toString());
            setPlacementValidation(true);
            this.sizeBox.setDisable(true);
        },this);
        enemyBoard = new EnemyBoard(event -> {
            if(!shooting)
                return;
            if(!myTurn){
                setInfoColor(Color.INDIANRED);
                putInfo("Wait for your turn!");
                return;
            }
            ClientCell cell = (ClientCell)event.getSource();
            if(cell.wasUsed()){
                return;
            }
            Package pack = new Package(Command.SHOOT.toString(),cell.getXCoordinate(),cell.getYCoordinate());
            this.clientSocket.sendMessage(pack.toString());
        });
        this.VBoxMy.getChildren().add(myBoard);
        Label mylabel = new Label();
        mylabel.setText("Your board");
        mylabel.setAlignment(Pos.CENTER);
        Label enemyLabel = new Label();
        enemyLabel.setText("Opponent board");
        enemyLabel.setAlignment(Pos.CENTER);
        this.VBoxMy.getChildren().add(mylabel);
        this.VBoxEnemy.getChildren().add(enemyBoard);
        this.VBoxEnemy.getChildren().add(enemyLabel);
    }

    private void radioSizesListener(){
        sizeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (sizeGroup.getSelectedToggle() != null) {
                    int size = (int)sizeGroup.getSelectedToggle().getUserData();
                    myBoard.setCurrentPlacingSize(size);
                }
            }
        });
    }

    private void removeToggleAction(){
        this.removeShip.setOnMouseClicked(event -> {
            if(this.removeShip.isSelected()) {
                this.removing = true;
            }
            else {
                removing = false;
            }
        });
    }

    //End of initialize methods

    //Resets boards
    public void reset(){
        this.shooting = false;
        this.shipPlacement = false;
        this.myTurn = false;
        this.placementValidation = false;
        this.myBoard.resetBoard();
        this.enemyBoard.resetBoard();
        this.radioSizeActivate();
        this.sizeBox.setDisable(true);
        this.radioDeactivate();
        this.buttonGiveUp.setDisable(true);
        this.removeShip.setDisable(true);
        //this.radioPlacement.setSelected(true);
    }

    public void setNewSizeRadio(){
        this.sizeBox.setDisable(false);
        for (Toggle t: this.sizeGroup.getToggles()){
            if(((RadioButton)t).isDisabled())
                continue;
            t.setSelected(true);
            ((RadioButton) t).requestFocus();
        }
    }

    public ToggleGroup getSizeGroup() {
        return sizeGroup;
    }

    // FXML ActionEvent handlers
    @FXML
    private void logIn(ActionEvent event){
        if(!this.nickField.getText().isEmpty()) {
            Package pack = new Package(Command.LOGIN.toString(), this.nickField.getText());
            this.clientSocket.sendMessage(pack.toString());
            this.myName = this.nickField.getText();
        }

        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong login");
            alert.setContentText("The username field cannot be left empty!");
            alert.showAndWait();
        }

    }

    @FXML
    private void swapBoards() {
        audioManager.playSoundEffect(
                "Sounds/swoosh.wav");
        if (isMyBoardVisible) {
            VBoxMy.setVisible(false);
            VBoxEnemy.setVisible(true);
        } else {
            VBoxMy.setVisible(true);
            VBoxEnemy.setVisible(false);
        }
        isMyBoardVisible = !isMyBoardVisible;
    }

    @FXML
    private void giveUp(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation of surrender");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to give up?");
        Optional<ButtonType> action = alert.showAndWait();
        if(action.get() == ButtonType.OK){
            this.clientSocket.sendMessage(Command.GIVE_UP.toString());
        }
        else
            return;
    }

    @FXML
    private void sendChatMessage(ActionEvent event){
        if(!this.chatField.getText().isEmpty()){
            String message = this.myName + " : " + this.chatField.getText();
            Package pack = new Package(Command.CHAT_MESSAGE.toString(),message);
            this.chatReceived(message);
            this.clientSocket.sendMessage(pack.toString());
            this.chatField.clear();
        }
        else{
            setInfoColor(Color.INDIANRED);
            putInfo("You didn't enter any message!");
        }
    }

    @FXML
    private void joinGame(ActionEvent event){
        if(this.gamesCombo.getSelectionModel().isEmpty()){
            this.setInfoColor(Color.INDIANRED);
            this.putInfo("No game selected from the list!");
        }

        else{
            String tmp = this.gamesCombo.getSelectionModel().getSelectedItem();
            Package pack = new Package(Command.JOIN_TO_GAME.toString(),tmp);
            this.clientSocket.sendMessage(pack.toString());
        }
    }

    @FXML
    private void createGame(ActionEvent event){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Game Name");
        dialog.setHeaderText("Enter a name");
        dialog.setContentText("Enter a name for the new game:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            String res = result.get();
            if(res.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Misnamed");
                alert.setContentText("The name field can't be left empty!");
                alert.showAndWait();
            }
            else {
                Package pack = new Package(Command.CREATE_GAME.toString(), res);
                this.clientSocket.sendMessage(pack.toString());
            }
        }
    }

    @FXML
    private void deleteGame(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion confirmation");
        alert.setHeaderText(null);
        alert.setContentText("You are trying to delete a game. Are you sure you want to do that?");
        Optional<ButtonType> action = alert.showAndWait();
        if(action.get() == ButtonType.OK){
            this.clientSocket.sendMessage(Command.DELETE_GAME.toString());
        }
        else
            return;
    }

    @FXML
    private void abandonGame(ActionEvent event){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Acknowledgment of abandonment");
            alert.setHeaderText(null);
            alert.setContentText("You are trying to leave the game. Are you sure you want to leave?");
            Optional<ButtonType> action = alert.showAndWait();
            if(action.get() == ButtonType.OK){
                this.clientSocket.sendMessage(Command.ABANDON_GAME.toString());
            }
            else
                return;
    }

    @FXML
    private void handleExit() {
        // Handle exiting the application
        Platform.exit();
    }

    @FXML
    private void handleMuteMusic(ActionEvent event) {
        CheckMenuItem menuItem = (CheckMenuItem) event.getSource();
        audioManager.setMusicMuted(menuItem.isSelected());
    }

    @FXML
    private void handleMuteEffects(ActionEvent event) {
        CheckMenuItem menuItem = (CheckMenuItem) event.getSource();
        audioManager.setEffectsMuted(menuItem.isSelected());
    }

    @FXML
    private void showAbout() {
        GameController.showAbout();
        // Show About dialog or information
    }

    @FXML
    private void showHelp() {
        GameController.showHelp();
        // Show Help dialog or information
    }

    @FXML
    private void toggleLanguage() {
        // Handle language toggle
        GameController.toggleLanguage();
    }

    @FXML
    private void openServerPage() {
        GameController.openServerPage();
        // Open the server page
    }

    @FXML
    private void openClientPage() {
        GameController.openClientPage();
        // Open the client page
    }

    @FXML
    private void showMainPage() {
        GameController.showMainPage();
        // Show the main page
    }

    @FXML
    private void sendOffer(ActionEvent event){
        this.clientSocket.sendMessage(Command.INVITATION.toString());
    }

    //End of FXML ActionEvent handlers

    //Activating / Deactivating GUI elements
    public void radioDeactivate() {
        //this.radioPlacement.setDisable(true);
        this.radioReady.setDisable(true);
    }

    public void radioActivate(){
       // this.radioPlacement.setDisable(false);
        this.radioReady.setDisable(false);
    }

    public void radioSizeDeactivate(){
        this.radio4.setDisable(true);
        this.radio3.setDisable(true);
        this.radio2.setDisable(true);
        this.radio1.setDisable(true);
    }

    public void radioSizeActivate(){
        this.radio4.setDisable(false);
        this.radio3.setDisable(false);
        this.radio2.setDisable(false);
        this.radio1.setDisable(false);
    }

    public void changeLoginButtonStatus(Boolean status){
        this.loginButton.setDisable(status);
    }
    public void changeJoinButtonStatus(Boolean status){
        this.joinButton.setDisable(status);
    }
    public void changeCreateButtonStatus(Boolean status){
        this.createButton.setDisable(status);
    }
    public void changeDeleteButtonStatus(Boolean status){
        this.buttonDeleteGame.setDisable(status);
    }
    public void changeGiveUpButtonStatus(Boolean status){
        this.buttonGiveUp.setDisable(status);
    }
    public void changeExitButtonStatus(Boolean status){
        this.exitButton.setDisable(status);
    }
    public void changeOfferButtonStatus(Boolean status) { this.buttonOffer.setDisable(status);}
    public void changeGamesComboStatus(Boolean status) { this.gamesCombo.setDisable(status);}
    public void changeSizeBoxStatus(Boolean status) { this.sizeBox.setDisable(status);}
    public void changeRemoveButtonStatus(Boolean status) { this.removeShip.setDisable(status);}

    public void enableChat(){
        this.chatField.setDisable(false);
        this.sendButton.setDisable(false);
    }

    public void disableChat(){
        this.chatField.setDisable(true);
        this.sendButton.setDisable(true);
    }

    public void afterLoginButtons(){
        changeCreateButtonStatus(false);
        changeJoinButtonStatus(false);
        this.gamesCombo.setDisable(false);
        changeLoginButtonStatus(true);
        this.nickField.setDisable(true);
        this.changeOfferButtonStatus(true);
        this.changeDeleteButtonStatus(true);
        this.changeExitButtonStatus(true);
    }

    // End of GUI Activating/Deactivating methods

    //Observable lists methods

    public void addGameToList(String gameInfo){
        if (this.gameList.contains(gameInfo)){
            this.infoArea.clear();
            return;
        }
        this.gameList.add(gameInfo);
    }
    public void removeGameFromList(String gameInfo){
        this.gameList.remove(gameInfo);
    }

    public void chatReceived(String msg){
        String message = " " + " " + msg;
        if(this.messageList.size() <100){
            this.messageList.add(message);
        }
        else {
            this.messageList.remove(0);
            this.messageList.add(message);
            this.chatArea.refresh();
        }
    }

    public void clearChatArea() { this.chatArea.getItems().clear();}

    //End of Observable lists methods

    //Setters
    public void changeShipPlacement(Boolean status){
        this.shipPlacement = status;
    }
    public void setPlacementValidation(boolean placementValidation) {
        this.placementValidation = placementValidation;
    }
    public void setShipPlacement(boolean shipPlacement) {
        this.shipPlacement = shipPlacement;
    }
    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }
    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public void setServiceAndStage(GameService service, Stage stage) {
        this.gameService = service;
        this.gameService.setViewController(this);
        this.stage = stage;
        this.clientSocket = this.gameService.getClientSocket();
        this.gameService.start();
        this.stage.setOnCloseRequest((event)->{
            this.clientSocket.sendMessage(Command.CLIENT_CLOSE.toString());
            this.gameService.cancel();
            this.clientSocket.close();
            System.exit(0);
        });
    }

    public void setMyGame(String name){
        this.myGame = name;
        this.yourgameField.setText("Game ID : " + this.myGame);
    }

    public void clearMyGame(){
        this.myGame = null;
        this.yourgameField.clear();
    }

    public void setInfoColor(Color paint){
        Region content = (Region) infoArea.lookup(".content");
        content.setStyle("-fx-background-color: " + toRgbString(paint) + ";");
    }

    //End of setters

    /**
     * Puts info after receiving message from server
     */
    public void putInfo(String info){
        infoArea.setText(info);
    }

    public void serverShutdown(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Server shutdown");
        alert.setHeaderText(null);
        alert.setContentText("The server sent a shutdown signal. Application interrupted");
        alert.showAndWait();
        this.clientSocket.sendMessage(Command.SERVER_SHUTDOWN.toString());
        this.gameService.cancel();
        this.clientSocket.close();
        System.exit(0);
    }

    public void offerReceived(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game suggestion");
        alert.setHeaderText(null);
        alert.setContentText("The opponent offers to start the game. What you answer?");
        Optional<ButtonType> action = alert.showAndWait();
        if(action.get() == ButtonType.OK){
            this.clientSocket.sendMessage(Command.OFFER_ACCEPT.toString());
        }
        else
            this.clientSocket.sendMessage(Command.OFFER_REJECT.toString());
    }

    //Additional methods for changing Color into RGB String
    private String toRgbString(Color c) {
        return "rgb("
                + to255Int(c.getRed())
                + "," + to255Int(c.getGreen())
                + "," + to255Int(c.getBlue())
                + ")";
    }

    private int to255Int(double d) {
        return (int) (d * 255);
    }

}

