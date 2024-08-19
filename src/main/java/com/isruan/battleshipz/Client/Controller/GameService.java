package com.isruan.battleshipz.Client.Controller;

import com.isruan.battleshipz.Client.Model.ClientSocket;
import com.isruan.battleshipz.Utils.Command;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

public class GameService extends Service<Void> {
    public ClientViewController viewController;
    private ClientSocket clientSocket;
    private boolean isConnected = false;

    public GameService() {
        super();
        clientSocket = new ClientSocket();
    }

    public void setViewController(ClientViewController viewController) {
        this.viewController = viewController;
    }

    public boolean tryConnect(String address, int port) {
        isConnected = this.clientSocket.connect(address, port);
        return isConnected;
    }

    public ClientSocket getClientSocket() {
        return this.clientSocket;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    loop:
                    while (true) {
                        String received = clientSocket.receiveMessage();
                        if (received != null) {
                            String tmp[] = received.split("#");
                            String command = tmp[0];

                            if (command.equals(Command.LOGIN_SUCCEED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Successfully logged in");
                                    viewController.afterLoginButtons();
                                });
                            } else if (command.equals(Command.NAME_NOT_AVAILABLE.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Another player uses this nickname. Enter another");
                                });
                            } else if (command.equals(Command.GAME_NAME_NOT_AVAILABLE.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("That game name is already taken. Try to come up with a different name");
                                });
                            } else if (command.equals(Command.WAIT_FOR_OPPONENT.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Your game has been created. Now wait for your opponent");
                                    viewController.setMyGame(tmp[1]);
                                    viewController.changeDeleteButtonStatus(false);
                                    viewController.changeExitButtonStatus(false);
                                    viewController.changeJoinButtonStatus(true);
                                    viewController.changeCreateButtonStatus(true);
                                    viewController.changeGamesComboStatus(true);
                                });
                            } else if (command.equals(Command.HOST_DELETED_THIS_GAME.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("The host removed this game. Try looking for another game on the list.");
                                    viewController.clearMyGame();
                                    viewController.disableChat();
                                    viewController.afterLoginButtons();
                                    viewController.reset();
                                    viewController.clearChatArea();
                                });
                            } else if (command.equals(Command.GAME_DELETED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Your game has been removed from the server");
                                    viewController.clearMyGame();
                                    viewController.disableChat();
                                    viewController.clearChatArea();
                                    viewController.afterLoginButtons();
                                    viewController.reset();
                                });
                            } else if (command.equals(Command.ABANDON_OK.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("You have disconnected from the game");
                                    viewController.clearMyGame();
                                    viewController.reset();
                                    viewController.afterLoginButtons();
                                    viewController.disableChat();
                                    viewController.clearChatArea();
                                });
                            } else if (command.equals(Command.GAME_ABANDON_AND_DELETED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("You have left the game. It was removed because there was no other player");
                                    viewController.clearMyGame();
                                    viewController.afterLoginButtons();
                                    viewController.reset();
                                    viewController.disableChat();
                                    viewController.clearChatArea();
                                });
                            } else if (command.equals(Command.HOST_CHANGE.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("The host has left the game, now you are the new host");
                                    viewController.disableChat();
                                    viewController.clearChatArea();
                                    viewController.reset();
                                    viewController.changeDeleteButtonStatus(false);
                                    viewController.changeOfferButtonStatus(true);
                                });
                            } else if (command.equals(Command.OPPONENT_EXIT.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("The other player has left the game. You have to wait for another opponent");
                                    viewController.disableChat();
                                    viewController.clearChatArea();
                                    viewController.reset();
                                    viewController.changeOfferButtonStatus(true);
                                });
                            } else if (command.equals(Command.GAME_HAS_ALREADY_2_PLAYERS.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("This game is already taken, try joining another game");
                                });
                            } else if (command.equals(Command.JOIN_TO_GAME_FAILED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Failed to join this game, please try selecting a different game");
                                });
                            } else if (command.equals(Command.JOINED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Successfully joined the game");
                                    viewController.setMyGame(tmp[1]);
                                    viewController.enableChat();
                                    viewController.changeGamesComboStatus(true);
                                    viewController.changeOfferButtonStatus(false);
                                    viewController.changeExitButtonStatus(false);
                                    viewController.changeCreateButtonStatus(true);
                                    viewController.changeJoinButtonStatus(true);
                                });
                            } else if (command.equals(Command.AVAILABLE_GAME.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("A new game has appeared");
                                    viewController.addGameToList(tmp[1]);
                                });
                            } else if (command.equals(Command.REMOVE_GAME.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.removeGameFromList(tmp[1]);
                                });
                            } else if (command.equals(Command.OPPONENT_JOINED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("The opponent joined the game.");
                                    viewController.enableChat();
                                    viewController.changeOfferButtonStatus(false);
                                });
                            } else if (command.equals(Command.PLACE_YOUR_SHIPS.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("You can start the game. Start Setting Up Your Ships");
                                    viewController.changeShipPlacement(true);
                                    viewController.changeSizeBoxStatus(false);
                                    viewController.radioSizeActivate();
                                    viewController.changeGiveUpButtonStatus(false);
                                    viewController.changeRemoveButtonStatus(false);
                                    viewController.changeOfferButtonStatus(true);
                                });
                            } else if (command.equals(Command.PLACEMENT_FAILED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Ship cannot be placed in this location");
                                    viewController.setPlacementValidation(false);
                                    viewController.changeSizeBoxStatus(false);
                                });
                            } else if (command.equals(Command.PLACEMENT_SUCCEED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Ship placed successfully");
                                    viewController.setPlacementValidation(false);
                                    viewController.changeSizeBoxStatus(false);
                                    viewController.myBoard.placeCurrentShip();
                                });
                            } else if (command.equals(Command.ALL_SHIPS_PLACED.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("All ships are already set. If you're ready, click READY!");
                                    viewController.setPlacementValidation(false);
                                });
                            } else if (command.equals(Command.REMOVE_OK.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Ship has been removed");
                                    viewController.setPlacementValidation(false);
                                    viewController.myBoard.removeShip(Boolean.parseBoolean(tmp[1]), Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]), Integer.parseInt(tmp[4]));
                                });
                            } else if (command.equals(Command.OPPONENT_IS_READY.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Your opponent is ready");
                                });
                            } else if (command.equals(Command.OPPONENT_GIVE_UP.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Your opponent has given up - you win!");
                                    viewController.reset();
                                    viewController.changeOfferButtonStatus(false);
                                });
                            } else if (command.equals(Command.INVITATION.toString())) {
                                Platform.runLater(() -> {
                                    viewController.offerReceived();
                                });
                            } else if (command.equals(Command.OFFER_REJECT.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Your opponent rejected your offer");
                                });
                            } else if (command.equals(Command.YOUR_TURN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Your move. Click on a cell on the opponent's board to shoot !");
                                    viewController.setPlacementValidation(false);
                                    viewController.setShipPlacement(false);
                                    viewController.setShooting(true);
                                    viewController.setMyTurn(true);
                                    viewController.radioDeactivate();
                                });
                            } else if (command.equals(Command.NOT_YOUR_TURN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.LIGHTGREY);
                                    viewController.putInfo("Your opponent will start the game. Wait for your turn");
                                    viewController.setShooting(true);
                                    viewController.setMyTurn(false);
                                    viewController.radioDeactivate();
                                });
                            } else if (command.equals(Command.MISSED_NOT_YOUR_TURN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("Miss!. You have to wait for your turn");
                                    viewController.setMyTurn(false);
                                    viewController.enemyBoard.repaintOnMissed(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                                });
                            } else if (command.equals(Command.OPPONENT_MISSED_YOUR_TURN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("The opponent missed. Now it's your turn");
                                    viewController.setMyTurn(true);
                                    viewController.myBoard.repaintOnMissed(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                                });
                            } else if (command.equals(Command.HIT_SHOOT_AGAIN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("You've hit the mark, but the ship is still on the surface. Shoot again !!");
                                    viewController.setMyTurn(true);
                                    viewController.enemyBoard.repaintOnHit(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                                });
                            } else if (command.equals(Command.OPPONENT_HIT.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("The enemy turn! You have to wait for your turn");
                                    viewController.setMyTurn(false);
                                    viewController.myBoard.repaintOnHit(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                                });
                            } else if (command.equals(Command.HIT_AND_SINK.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Hit - sunk! The enemy still has ships to sink - keep shooting!");
                                    viewController.setMyTurn(true);
                                    viewController.enemyBoard.repaintOnHit(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                                });
                            } else if (command.equals(Command.OPPONENT_HIT_AND_SINK.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("The enemy sunk your ship! Wait for your turn");
                                    viewController.setMyTurn(false);
                                    viewController.myBoard.repaintOnHit(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
                                });
                            } else if (command.equals(Command.PLAYER_HINT.toString())) {
                                Platform.runLater(() -> {
                                    viewController.enemyBoard.paintHintAfterKill(Integer.parseInt(tmp[2]), Integer.parseInt(tmp[3]), Integer.parseInt(tmp[4]), Boolean.parseBoolean(tmp[1]));
                                });
                            } else if (command.equals(Command.YOU_WIN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("You WON! - Game Restarted!");
                                    viewController.reset();
                                    viewController.changeOfferButtonStatus(false);
                                });
                            } else if (command.equals(Command.YOU_LOSE.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.INDIANRED);
                                    viewController.putInfo("YOU LOST! - Game Restarted!");
                                    viewController.reset();
                                    viewController.changeOfferButtonStatus(false);
                                });
                            } else if (command.equals(Command.CHAT_MESSAGE.toString())) {
                                Platform.runLater(() -> {
                                    viewController.setInfoColor(Color.GREENYELLOW);
                                    viewController.putInfo("Message Received");
                                    viewController.chatReceived(tmp[1]);
                                });
                            } else if (command.equals(Command.SERVER_SHUTDOWN.toString())) {
                                Platform.runLater(() -> {
                                    viewController.serverShutdown();
                                });
                                break loop;
                            }
                        }
                    }
                } catch (Exception e) {
                } finally {
                    return null;
                }
            }
        };
    }
}
