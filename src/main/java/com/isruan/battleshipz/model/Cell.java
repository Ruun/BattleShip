package com.isruan.battleshipz.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import com.isruan.battleshipz.view.GameBoard;

public class Cell extends Rectangle {
	public int x, y;
	public Ship ship = null;
	public boolean wasShot = false;
	private GameBoard board;

	public Cell(int x, int y, GameBoard board) {
		super(60, 60); // 40 X 40
		this.x = x;
		this.y = y;
		this.board = board;
		setFill(Color.LIGHTGRAY);
		setStroke(Color.BLACK);

	}

	public boolean shoot() {
		wasShot = true;
		setFill(Color.BLACK);

		if (ship != null) {
			ship.hit();
			setFill(Color.RED);
			if (!ship.isAlive()) {
				board.ships--;
			}
			return true;
		}

		return false;
	}
}