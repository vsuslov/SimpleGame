package ru.rs.screens;

import ru.rs.Resources;
import ru.rs.interfaces.Game;
import ru.rs.interfaces.Graphics;
import ru.rs.objects.Screen;
import android.graphics.Color;

public class LoadingScreen extends Screen {

	public LoadingScreen(Game g) {
		super(g);
	}

	@Override
	public void update(float delta) {
		Graphics graphics = game.getGraphics();
		Resources.dom = graphics.newBitmap("rec_y.png");
		Resources.unit = graphics.newBitmap("unit.png");
		Resources.enemyHouse = graphics.newBitmap("rec_dy");
	}

	@Override
	public void render(float delta) {
		Graphics g = game.getGraphics();

		g.drawBitmap(Resources.dom, 0, 50);

		g.drawBitmap(Resources.unit, 100, 50);

		g.drawText("DONE!", g.getWidth() / 2, g.getHeight() / 2, Color.RED);
		game.setScreen(new CollisionScreen(game));
	}

}
