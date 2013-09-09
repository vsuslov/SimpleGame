package ru.rs.gameobjects;

import ru.rs.Resources;
import ru.rs.interfaces.Game;

public class Unit extends DynamicObject {

	public Unit(Side side, Game game) {
		super(side, game);

	}

	@Override
	protected void setImage() {
		this.image = Resources.unit;
	}

}
