package com.isruan.battleshipz.view;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import com.isruan.battleshipz.model.Cell;
import com.isruan.battleshipz.model.Ship;

/**
 * The GameBoard class represents a game board for the Battleship game. It
 * extends the Region class from JavaFX and manages the placement and display of
 * ships and cells on the board.
 */
public class GameBoard extends Pane {
	private VBox rows = new VBox();
	private boolean enemy;
	// = false;
	public int ships = 5;
	private List<Point2D> enemyHits = new ArrayList<>();
	private List<Point2D> enemyMisses = new ArrayList<>();
	private List<Point2D> playerHits = new ArrayList<>();
	private List<Point2D> playerMisses = new ArrayList<>();

	private IntegerProperty enemyHitCount = new SimpleIntegerProperty(0);
	private IntegerProperty enemyMissCount = new SimpleIntegerProperty(0);
	private IntegerProperty playerHitCount = new SimpleIntegerProperty(0);
	private IntegerProperty playerMissCount = new SimpleIntegerProperty(0);

	/**
	 * Constructs a GameBoard with the specified enemy status and mouse event
	 * handler.
	 *
	 * @param enemy   if true, the board is for the enemy player
	 * @param handler the event handler for mouse click events on the cells
	 */
	public GameBoard(boolean enemy, EventHandler<? super MouseEvent> handler) {
		this.enemy = enemy;
		for (int y = 0; y < 10; y++) {
			HBox row = new HBox();
			for (int x = 0; x < 10; x++) {
				Cell c = new Cell(x, y, this);
				c.setOnMouseClicked(handler);
				row.getChildren().add(c);
			}
			rows.getChildren().add(row);
		}
		rows.setSpacing(0);
		getChildren().add(rows);

	}

	/**
	 * Records a hit on the board (enemy or player).
	 *
	 * @param x       the x-coordinate of the hit
	 * @param y       the y-coordinate of the hit
	 * @param isEnemy true if the hit is on the enemy board, false if on the player
	 *                board
	 */
	public void recordHit(int x, int y, boolean isEnemy) {

		Point2D hit = new Point2D(x, y);
		if (isEnemy) {
			enemyHits.add(hit);
			enemyHitCount.set(enemyHits.size());
		} else {
			playerHits.add(hit);
			playerHitCount.set(playerHits.size());
		}
	}

	/**
	 * Records a miss on the board (enemy or player).
	 *
	 * @param x       the x-coordinate of the miss
	 * @param y       the y-coordinate of the miss
	 * @param isEnemy true if the miss is on the enemy board, false if on the player
	 *                board
	 */
	public void recordMiss(int x, int y, boolean isEnemy) {
		Point2D miss = new Point2D(x, y);

		if (isEnemy) {
			enemyMisses.add(miss);
			enemyMissCount.set(enemyMisses.size());
		} else {
			playerMisses.add(miss);
			playerMissCount.set(playerMisses.size());
		}
	}

	/**
	 * Retrieves the list of hits on the enemy board.
	 *
	 * @return the list of Point2D representing hits on the enemy board
	 */
	public List<Point2D> getEnemyHits() {
		return enemyHits;
	}

	/**
	 * Retrieves the list of misses on the enemy board.
	 *
	 * @return the list of Point2D representing misses on the enemy board
	 */
	public List<Point2D> getEnemyMisses() {
		return enemyMisses;
	}

	/**
	 * Retrieves the list of hits on the player board.
	 *
	 * @return the list of Point2D representing hits on the player board
	 */
	public List<Point2D> getPlayerHits() {
		return playerHits;
	}

	/**
	 * Retrieves the list of misses on the player board.
	 *
	 * @return the list of Point2D representing misses on the player board
	 */
	public List<Point2D> getPlayerMisses() {
		return playerMisses;
	}

	/**
	 * Counts the total number of cells taken on the board (enemy or player).
	 *
	 * @param isEnemy true if counting cells on the enemy board, false if on the
	 *                player board
	 * @return the total number of cells taken
	 */
	public int countCellsTaken(boolean isEnemy) {
		List<Point2D> hits = isEnemy ? enemyHits : playerHits;
		List<Point2D> misses = isEnemy ? enemyMisses : playerMisses;
		return hits.size() + misses.size();
	}

	/**
	 * Counts the number of red cells (hits) on the board (enemy or player).
	 *
	 * @param isEnemy true if counting hits on the enemy board, false if on the
	 *                player board
	 * @return the number of red cells (hits)
	 */
	public int countRedCells(boolean isEnemy) {
		List<Point2D> hits = isEnemy ? enemyHits : playerHits;
		return hits.size();
	}

	/**
	 * Places a ship on the game board at the specified coordinates.
	 *
	 * @param ship the ship to place on the board
	 * @param x    the x-coordinate to place the ship
	 * @param y    the y-coordinate to place the ship
	 * @return true if the ship was placed successfully, false otherwise
	 */
	public boolean placeShip(Ship ship, int x, int y) {
		if (canPlaceShip(ship, x, y)) {

			int length = ship.type;

			if (ship.vertical) {
				for (int i = y; i < y + length; i++) {
					Cell cell = getCell(x, i);
					cell.ship = ship;
					if (!enemy) {
						cell.setFill(Color.WHITE);
						cell.setStroke(Color.GREEN);
					}
				}
			} else {
				for (int i = x; i < x + length; i++) {
					Cell cell = getCell(i, y);
					cell.ship = ship;
					if (!enemy) {
						cell.setFill(Color.WHITE);
						cell.setStroke(Color.GREEN);
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Gets the cell at the specified coordinates.
	 *
	 * @param x the x-coordinate of the cell
	 * @param y the y-coordinate of the cell
	 * @return the cell at the specified coordinates
	 */
	public Cell getCell(int x, int y) {
		return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
	}

	/**
	 * Gets the neighboring cells of the specified cell coordinates.
	 *
	 * @param x the x-coordinate of the cell
	 * @param y the y-coordinate of the cell
	 * @return an array of neighboring cells
	 */
	private Cell[] getNeighbors(int x, int y) {
		Point2D[] points = new Point2D[] { new Point2D(x - 1, y), new Point2D(x + 1, y), new Point2D(x, y - 1),
				new Point2D(x, y + 1) };

		List<Cell> neighbors = new ArrayList<>();

		for (Point2D p : points) {
			if (isValidPoint(p)) {
				neighbors.add(getCell((int) p.getX(), (int) p.getY()));
			}
		}
		return neighbors.toArray(new Cell[0]);
	}

	/**
	 * Checks if a ship can be placed at the specified coordinates.
	 *
	 * @param ship the ship to be placed
	 * @param x    the x-coordinate to place the ship
	 * @param y    the y-coordinate to place the ship
	 * @return true if the ship can be placed, false otherwise
	 */
	private boolean canPlaceShip(Ship ship, int x, int y) {
		int length = ship.type;

		if (ship.vertical) {
			for (int i = y; i < y + length; i++) {
				if (!isValidPoint(x, i)) {
					return false;
				}
				Cell cell = getCell(x, i);
				if (cell.ship != null) {
					return false;
				}
				for (Cell neighbor : getNeighbors(x, i)) {
					if (!isValidPoint(x, i) || neighbor.ship != null) {
						return false;
					}
				}
			}
		} else {
			for (int i = x; i < x + length; i++) {
				if (!isValidPoint(i, y)) {
					return false;
				}
				Cell cell = getCell(i, y);
				if (cell.ship != null) {
					return false;
				}
				for (Cell neighbor : getNeighbors(i, y)) {
					if (!isValidPoint(i, y) || neighbor.ship != null) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the specified point is within the bounds of the board.
	 *
	 * @param point the point to check
	 * @return true if the point is valid, false otherwise
	 */
	private boolean isValidPoint(Point2D point) {
		return isValidPoint(point.getX(), point.getY());
	}

	public IntegerProperty enemyHitCountProperty() {
		return enemyHitCount;
	}

	public IntegerProperty enemyMissCountProperty() {
		return enemyMissCount;
	}

	public IntegerProperty playerHitCountProperty() {
		return playerHitCount;
	}

	public IntegerProperty playerMissCountProperty() {
		return playerMissCount;
	}

	/**
	 * Checks if the specified coordinates are within the bounds of the board.
	 *
	 * @param x the x-coordinate to check
	 * @param y the y-coordinate to check
	 * @return true if the coordinates are valid, false otherwise
	 */
	private boolean isValidPoint(double x, double y) {
		return x >= 0 && x < 10 && y >= 0 && y < 10;
	}


}
