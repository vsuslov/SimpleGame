package ru.rs;

import ru.rs.objects.Screen;
import ru.rs.screens.LoadingScreen;

public class SimpleGame extends MainActivity {

	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}

}
