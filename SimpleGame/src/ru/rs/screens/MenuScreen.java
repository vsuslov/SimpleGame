package ru.rs.screens;

import android.graphics.Color;
import android.graphics.Paint;
import ru.rs.interfaces.Game;
import ru.rs.interfaces.Graphics;
import ru.rs.interfaces.Input;
import ru.rs.interfaces.Input.TouchEvent;
import ru.rs.objects.Screen;

import java.util.List;

public class MenuScreen extends Screen {
	private Input input;
	private List<TouchEvent> events;
	private Graphics graphics;

	public MenuScreen(Game g) {
		super(g);
		input = game.getInput();
		graphics = game.getGraphics();
	}

	@Override
	public void update(float delta) {
		events = input.getTouchEvents();
		for (TouchEvent event : events) {
			if (event.type == TouchEvent.TOUCH_UP) {
				checkTouch(event);
			}
		}

	}

	@Override
	public void render(float delta) {
		Paint paint = new Paint();
		paint.setTextSize(24);
		paint.setColor(Color.GREEN);

		graphics.drawText("Play", graphics.getWidth() / 2 - 5,
				graphics.getHeight() / 2, paint);

		paint.setColor(Color.RED);
		graphics.drawText("Exit", graphics.getWidth() / 2 - 5,
				graphics.getHeight(), paint);
	}

	private void checkTouch(TouchEvent event) {
		if (inBounds(event, graphics.getWidth() / 2 - 10, graphics.getHeight(),
				15, 5)) {
			game.exit();
		} else if (inBounds(event, graphics.getWidth() / 2,
				graphics.getHeight() / 2, 30, 15)) {
			game.setScreen(new CollisionScreen(game));
		}

	}
}
