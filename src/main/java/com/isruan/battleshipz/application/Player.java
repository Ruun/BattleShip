package com.isruan.battleshipz.application;

import java.util.Random;

import com.isruan.battleshipz.model.Ship;
import com.isruan.battleshipz.view.GameBoard;

public class Player {

	public void startGame(GameBoard enemyBoard, Random random) {
		int type = 5;

		while (type > 0) {
			int x = random.nextInt(10);
			int y = random.nextInt(10);

			if (enemyBoard.placeShip(new Ship(type, Math.random() < 0.5), x, y)) {
				type--;
			}
		}
	}

	public void enemyMove(GameBoard playerBoard, Random random) {
		int x, y;

		do {
			x = random.nextInt(10);
			y = random.nextInt(10);
		} while (playerBoard.getCell(x, y).wasShot);

		playerBoard.getCell(x, y).shoot();

	}
}