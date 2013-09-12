package ru.rs.gameobjects;

import ru.rs.GameObject;
import ru.rs.Renderable;
import ru.rs.interfaces.Game;
import ru.rs.objects.math.Rectangle;
import ru.rs.objects.math.Vector;
import android.graphics.Bitmap;

public abstract class SimpleObject extends GameObject implements Renderable {
	public Bitmap image;
	public Side side;
	public Game game;

	public SimpleObject(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public SimpleObject(Vector position, Bitmap image) {
		super(position.x, position.y, image.getWidth(), image.getHeight());
		this.image = image;
	}

	public SimpleObject(Side side, Game game) {
		this.side = side;
		this.game = game;
		setImage();

		float x = (Side.ENEMY.equals(this.side)) ? this.game.getGraphics()
				.getWidth() - image.getWidth() - 1 : 0;
		float y = 1;

		position = new Vector(x, y);
		float w = image.getWidth();
		float h = image.getHeight();
		this.width = w;
		this.height = h;
		bounds = new Rectangle(x - w / 2, y - h / 2, w, h);
	}

	@Override
	public void render() {
		game.getGraphics()
				.drawBitmap(
						image,
						position.x,
						game.getGraphics().getHeight() - position.y
								- image.getHeight());
	}

	protected abstract void setImage();

	public Vector getPosition() {
		return position.cpy();
	}
}
