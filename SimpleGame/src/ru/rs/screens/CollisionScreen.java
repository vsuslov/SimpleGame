package ru.rs.screens;

import android.graphics.Color;
import ru.rs.GameWorld;
import ru.rs.gameobjects.SimpleGameWorld;
import ru.rs.interfaces.Game;
import ru.rs.interfaces.Graphics;
import ru.rs.interfaces.Input;
import ru.rs.interfaces.Input.TouchEvent;
import ru.rs.objects.Screen;

import java.util.List;

public class CollisionScreen extends Screen {

	private GameWorld gameWorld;
	private Input input;
	private Graphics graphics;

	private List<TouchEvent> touches;

	public CollisionScreen(Game g) {
		super(g);
		gameWorld = new SimpleGameWorld(game);
		input = game.getInput();
		graphics = game.getGraphics();
	}

	@Override
	public void update(float delta) {
		touches = input.getTouchEvents();
		if (touches.size() > 0) {
//			for (TouchEvent event : touches) {
//				if (event.x < 32 && event.y < 32) {
//					gameWorld.addAllyUnit();
//					gameWorld.addEnemyUnit();
//				}

            gameWorld.touch(touches);
//			}
		}
		gameWorld.update();

	}

	@Override
	public void render(float delta) {
		graphics.drawText(Float.toString(delta), graphics.getWidth() - 15, 15,
				Color.BLUE);
		graphics.clear(Color.BLACK);
		gameWorld.render();
	}

}
