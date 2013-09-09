package ru.rs.gameobjects;

import ru.rs.Updateable;
import ru.rs.interfaces.Game;
import ru.rs.objects.math.Vector;

public abstract class DynamicObject extends SimpleObject implements Updateable {
	private Vector velocity;
	private float speed;
	private Vector limit;

	public DynamicObject(Side side, Game game) {
		super(side, game);
		speed = 1.2f;
		if (Side.ENEMY.equals(side)) {
			velocity = new Vector(-1, 0);
			limit = new Vector(0, 0);
		} else if (Side.ALLY.equals(side)) {
			velocity = new Vector(1, 0);
			limit = new Vector(
					game.getGraphics().getWidth() - image.getWidth(), 0);
		}
		velocity.mul(speed);
	}

	public void update() {
		if (Math.abs(position.x - limit.x) > speed) {
			this.position.add(velocity);
		}
	}

}
