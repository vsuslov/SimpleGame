package ru.rs.gameobjects;

import ru.rs.Resources;
import ru.rs.interfaces.Game;

public class Castle extends SimpleObject {

	public Castle(Side side, Game game) {
		super(side, game);
	}

	@Override
	protected void setImage() {
		image = Resources.dom;
	}
}
