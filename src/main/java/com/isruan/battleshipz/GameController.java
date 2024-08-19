package com.isruan.battleshipz;

import java.io.IOException;
import java.util.Random;

import com.isruan.battleshipz.application.AudioManager;
import com.isruan.battleshipz.application.LanguageManager;
import com.isruan.battleshipz.application.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.isruan.battleshipz.model.Cell;
import com.isruan.battleshipz.model.Ship;
import com.isruan.battleshipz.view.GameBoard;

/**
 * 
 * java --module-path "/Users/thelion/Downloads/javafx-sdk-22.0.1/lib"
 * --add-modules javafx.controls,javafx.fxml -jar Pong.jar
 * 
 * 
 */

public class GameController extends Application {

	private static boolean isEnglish = true;
	private static Stage primaryStage;
	private static BorderPane gameLayout;
	private static Label turnIndicator;
	private static GameBoard enemyBoard;
    private static GameBoard playerBoard;
	private static boolean showingPlayerBoard = true;
	private static AudioManager audioManager;
	private static LanguageManager languageManager;
	private static boolean running = false;
	private static int shipsToPlace = 5;
	private static boolean enemyTurn = false;
	private static Random random = new Random();
	private static Player player;
	private static Label boardIndicator;

	private boolean playerReady = false;
	private boolean opponentReady = false;
	private boolean isHost = false;


	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.audioManager = new AudioManager();
		this.languageManager = new LanguageManager();
		this.player = new Player();
		primaryStage.setTitle("BattleShip");
		showMainPage();
	}
	public static void exit(){
		primaryStage.close();
	}


	public static VBox createTopBox() {
		MenuBar menuBar = createMenuBar();

		ImageView logo = new ImageView(new Image("file:Assets/logo.png"));
		logo.setFitHeight(100);
		logo.setPreserveRatio(true);

		VBox topBox = new VBox(menuBar, logo);
		topBox.setAlignment(Pos.TOP_CENTER);
		topBox.setSpacing(10);

		return topBox;
	}

	public static MenuBar createMenuBar() {
		MenuBar menuBar = new MenuBar();

		// File Menu
		Menu fileMenu = new Menu(languageManager.translate("File"));
		MenuItem exitItem = new MenuItem(languageManager.translate("Exit"));
		exitItem.setOnAction(e -> {
			primaryStage.close();
			// ChatWindow.close();
		});
		fileMenu.getItems().add(exitItem);

		// Options Menu
		Menu optionsMenu = new Menu(languageManager.translate("Mute Options"));
		CheckMenuItem muteMusicItem = new CheckMenuItem(languageManager.translate("Mute Game Music"));
		muteMusicItem.setOnAction(e -> audioManager.setMusicMuted(muteMusicItem.isSelected()));
		CheckMenuItem muteEffectsItem = new CheckMenuItem(languageManager.translate("Mute Sound Effects"));
		muteEffectsItem.setOnAction(e -> audioManager.setEffectsMuted(muteEffectsItem.isSelected()));
		optionsMenu.getItems().addAll(muteMusicItem, muteEffectsItem);

		// Help Menu
		Menu helpMenu = new Menu(languageManager.translate("Help"));
		MenuItem aboutItem = new MenuItem(languageManager.translate("About"));
		aboutItem.setOnAction(e -> showAbout());
		MenuItem helpItem = new MenuItem(languageManager.translate("Help"));
		helpItem.setOnAction(e -> showHelp());
		helpMenu.getItems().addAll(aboutItem, helpItem);

		// Language Menu
		Menu languageMenu = new Menu(languageManager.translate("Language"));
		MenuItem changeLanguageItem = new MenuItem(isEnglish ? "Français" : "English");
		changeLanguageItem.setOnAction(e -> toggleLanguage());
		languageMenu.getItems().add(changeLanguageItem);

		languageMenu.setOnShowing(e -> {
			changeLanguageItem.setText(isEnglish ? "Français" : "English");
		});

		// Network Menu
		Menu networkMenu = new Menu(languageManager.translate("Network"));

		MenuItem openServerPage = new MenuItem(languageManager.translate("Server"));
		openServerPage.setOnAction(e -> openServerPage());

		MenuItem openClientPage = new MenuItem(languageManager.translate("Client"));
		openClientPage.setOnAction(e -> openClientPage());

		networkMenu.getItems().addAll(openServerPage,openClientPage);

		MenuItem backItem = new MenuItem(languageManager.translate("Main Page"));
		backItem.setOnAction(e -> showMainPage());

		menuBar.getMenus().addAll(fileMenu, optionsMenu, helpMenu, languageMenu, networkMenu);
		menuBar.getMenus().add(new Menu(languageManager.translate("Back"), null, backItem));

		return menuBar;
	}

	public static void openServerPage() {
		try {
			FXMLLoader loader = new FXMLLoader(GameController.class.getResource("ServerView.fxml"));
			Parent root = loader.load();
			Stage serverStage = new Stage();
			Scene scene = new Scene(root);
			serverStage.setScene(scene);
			serverStage.setTitle("Server Page");
			serverStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void openClientPage() {
		try {
			FXMLLoader loader = new FXMLLoader(GameController.class.getResource("LoginWindow.fxml"));
			Parent root = loader.load();
			Stage clientStage = new Stage();
			Scene scene = new Scene(root);
			clientStage.setScene(scene);
			clientStage.setTitle("BattleShip");
			clientStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void showMainPage() {
		BorderPane mainPage = new BorderPane();
		mainPage.setPadding(new Insets(20));
		mainPage.setPrefSize(1600, 900);

		VBox topBox = createTopBox();

		VBox centerBox = new VBox(20);
		centerBox.setAlignment(Pos.CENTER);
		centerBox.setPadding(new Insets(20));
		Button vsBotButton = new Button(languageManager.translate("Vs Bot"));
		// Button vsFriend = new Button(languageManager.translate("Vs Friend"));
		vsBotButton.setOnAction(e -> {
			// player.setupGame();
			showGameUI();
		});

		centerBox.getChildren().addAll(vsBotButton);

		ImageView backgroundImageView = new ImageView(new Image("file:Assets/background.png"));
		backgroundImageView.setFitWidth(1600);
		backgroundImageView.setFitHeight(900);

		StackPane stackPane = new StackPane(backgroundImageView, mainPage);
		StackPane.setAlignment(backgroundImageView, Pos.CENTER);
		StackPane.setAlignment(mainPage, Pos.CENTER);

		mainPage.setTop(topBox);
		mainPage.setCenter(centerBox);

		Scene mainScene = new Scene(stackPane, 1400, 800);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	public static void showGameUI() {

		enemyBoard = new GameBoard(true, event -> {
			if (!running) {
				return;
			}

			Cell cell = (Cell) event.getSource();
			if (cell.wasShot) {
				return;
			}

			boolean isHit = cell.shoot();
			if (isHit) {
				enemyBoard.recordHit(cell.x, cell.y, true);
				audioManager.playSoundEffect(
						"Sounds/explosion.wav");

			} else {
				enemyBoard.recordMiss(cell.x, cell.y, true);
				audioManager.playSoundEffect(
						"Sounds/splash.wav");
			}

			if (enemyBoard.ships == 0) {
				running = false;
				showEndGamePopup("YOU WIN!");
				audioManager.playSoundEffect(
						"Sounds/win.wav");
			}

			enemyTurn = !isHit;
			if (enemyTurn) {
				player.enemyMove(playerBoard, random);
				if (playerBoard.ships == 0) {
					running = false;
					showEndGamePopup("YOU LOSE!");
					audioManager.playSoundEffect(
							"Sounds/lose.wav");
				}
			}
		});

		playerBoard = new GameBoard(false, event -> {
			if (running) {
				return;
			}

			Cell cell = (Cell) event.getSource();
			if (playerBoard.placeShip(new Ship(shipsToPlace, event.getButton() == MouseButton.PRIMARY), cell.x,
					cell.y)) {
				if (--shipsToPlace == 0) {
					player.startGame(enemyBoard, random);
					running = true;
				}
			}
		});


		MenuBar menuBar = createMenuBar();

		ImageView logo = new ImageView(new Image("file:Assets/logo.png"));
		logo.setFitHeight(30);
		logo.setPreserveRatio(true);

		HBox menuBarWithLogo = new HBox(10, logo, menuBar);
		menuBarWithLogo.setAlignment(Pos.CENTER_LEFT);

		VBox rightInfo = new VBox(10);
		rightInfo.setPadding(new Insets(10));
		rightInfo.setAlignment(Pos.TOP_LEFT);

		Label shipStatus = new Label();
		Label enemyStatus = new Label();

		shipStatus.textProperty().bind(Bindings.createStringBinding(
				() -> languageManager.translate("Your Ships\nHits: ") + playerBoard.playerHitCountProperty().get()
				+ "\nMisses: " + playerBoard.playerMissCountProperty().get(),
				playerBoard.playerHitCountProperty(), playerBoard.playerMissCountProperty()));

		enemyStatus.textProperty().bind(Bindings.createStringBinding(
				() -> languageManager.translate("Enemy Ships\nHits: ") + enemyBoard.enemyHitCountProperty().get()
				+ "\nMisses: " + enemyBoard.enemyMissCountProperty().get(),
				enemyBoard.enemyHitCountProperty(), enemyBoard.enemyMissCountProperty()));
		boardIndicator = new Label(
				languageManager.translate("Your Board 1\nRound 1 2\nClick on a square\n (left click: place ship vertical,\n right click place horizontal)\n to place a ship"));



		rightInfo.getChildren().addAll(boardIndicator, shipStatus, enemyStatus);

		gameLayout = new BorderPane();
		gameLayout.setTop(menuBarWithLogo);
		gameLayout.setCenter(playerBoard);
		gameLayout.setRight(rightInfo);

		Button toggleButton = new Button("Swap");
		toggleButton.setPrefSize(80, 30);
		toggleButton.setOnAction(e -> swapBoards());

		VBox bottomBox = new VBox(toggleButton);
		bottomBox.setAlignment(Pos.CENTER);
		bottomBox.setPadding(new Insets(10));

		gameLayout.setBottom(bottomBox);

		Scene gameScene = new Scene(gameLayout, 1400, 700);
		primaryStage.setScene(gameScene);
		primaryStage.show();


		audioManager.playBgMusic(
				"Sounds/bgMusic.wav");
	}


	public static void showEndGamePopup(String message) {
		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Game Over");

		VBox popupVBox = new VBox(10);
		popupVBox.setPadding(new Insets(10));
		popupVBox.setAlignment(Pos.CENTER);

		Label messageLabel = new Label(message);
		messageLabel.setStyle("-fx-font-size: 16px;");

		Button exitButton = new Button("Exit");
		exitButton.setOnAction(e -> Platform.exit());

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			popupStage.close();

		});

		Button replayButton = new Button("Replay");
		replayButton.setOnAction(e -> {
			popupStage.close();
			resetGame();
		});

		HBox buttonsHBox = new HBox(10, exitButton, backButton, replayButton);
		buttonsHBox.setAlignment(Pos.CENTER);

		popupVBox.getChildren().addAll(messageLabel, buttonsHBox);

		Scene popupScene = new Scene(popupVBox, 300, 200);
		popupStage.setScene(popupScene);
		popupStage.showAndWait();
	}


	private static void resetGame() {
		shipsToPlace = 5;
		running = false;
		enemyTurn = false;

		enemyBoard = new GameBoard(true, event -> {
			if (!running) {
				return;
			}

			Cell cell = (Cell) event.getSource();
			if (cell.wasShot) {
				return;
			}

			enemyTurn = !cell.shoot();

			if (enemyBoard.ships == 0) {
				running = false;
				showEndGamePopup("YOU WIN!");
				audioManager.playSoundEffect("Sounds/win.wav");

			}

			if (enemyTurn) {
				player.enemyMove(playerBoard, random);
				if (playerBoard.ships == 0) {
					running = false;
					showEndGamePopup("YOU LOSE!");
					audioManager.playSoundEffect("Sounds/lose.wav");
				}
			}
		});

		playerBoard = new GameBoard(false, event -> {
			if (running) {
				return;
			}

			Cell cell = (Cell) event.getSource();
			if (playerBoard.placeShip(new Ship(shipsToPlace, event.getButton() == MouseButton.PRIMARY), cell.x,
					cell.y)) {
				if (--shipsToPlace == 0) {
					player.startGame(enemyBoard, random);
					running = true;
				}
			}
		});

		gameLayout.setCenter(playerBoard);
		turnIndicator.setText(languageManager.translate("Daniel’s Turn\nRound 1\nClick on a square to attack it"));
	}


	public static void showAbout() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(languageManager.translate("About"));
		alert.setHeaderText(languageManager.translate("About this game"));
		alert.setContentText(
				languageManager.translate("Battleship Game\nVersion 1.0\nDeveloped by Ruan Simo F."));
		alert.showAndWait();
	}

	public static void showHelp() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(languageManager.translate("Help"));
		alert.setHeaderText(languageManager.translate("Game Instructions"));
		alert.setContentText(languageManager.translate(
				"How to play Battleship:\n1.Click to Place your ships.\n (left click: place ship vertical,\n right click place horizontal)\n2. Take turns guessing the location of enemy ships.\n3. Sink all enemy ships to win."));
		alert.showAndWait();
	}

	public static void toggleLanguage() {
		isEnglish = !isEnglish;
		languageManager.setLanguage(isEnglish ? "English" : "Français");
		showMainPage();
	}


	private static void swapBoards() {
		audioManager.playSoundEffect(
				"Sounds/swoosh.wav");
		if (showingPlayerBoard) {
			gameLayout.setCenter(enemyBoard);
			boardIndicator
			.setText(languageManager
					.translate("Enemy Board \n1. Round 1 \n2. Click on a square\n to attack it"));
		} else {
			gameLayout.setCenter(playerBoard);
			boardIndicator
			.setText(
					languageManager.translate("Your Board\n1. Round 1 \n2. Click on a square\n to place a ship"));
		}
		showingPlayerBoard = !showingPlayerBoard;
	}

	public static void main(String[] args) {

		launch(args);
	}

}
