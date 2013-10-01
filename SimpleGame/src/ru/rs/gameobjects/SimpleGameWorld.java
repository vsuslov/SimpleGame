package ru.rs.gameobjects;

import java.util.ArrayList;
import java.util.List;

import ru.rs.GameObject;
import ru.rs.GameWorld;
import ru.rs.Renderable;
import ru.rs.Updateable;
import ru.rs.interfaces.Game;
import ru.rs.interfaces.Graphics;
import ru.rs.objects.math.Vector;
import ru.rs.utils.math.FigureUtils;
import android.graphics.Color;
//считаю, нужно объектную модель(человечки, здания) 
//засовывать в грид и оттуда их прорисовывать, а не в гейм ворлде.

public class SimpleGameWorld implements GameWorld {

	private List<Castle> staticObjects;
	private List<DynamicObject> dynamicObjects;
	private Game game;
	private SpatialGrid grid;
	private Graphics graphics;

	public SimpleGameWorld(Game game) {
		this.game = game;
		graphics = game.getGraphics();
		init();
		Castle allyCastle = new Castle(Side.ALLY, this.game);
		Castle enemyCastle = new Castle(Side.ENEMY, this.game);
		grid.insertObject(allyCastle);
		grid.insertObject(enemyCastle);
		staticObjects.add(allyCastle);
		staticObjects.add(enemyCastle);
		// addAllyUnit();
	}

	// ///////////////////////////////////////////////////////

	@Override
	public void update() {
		if (dynamicObjects.size() > 0) {
			for (DynamicObject unit : dynamicObjects) {
				unit.update();
				List<GameObject> list = grid.getPotentialColliders(unit);
				for (GameObject collider : list) {
					if (FigureUtils.overlap(unit.getBounds(),
							collider.getBounds())) {
						if (collider != null
								&& !unit.side
										.equals(((SimpleObject) collider).side)) {
							removeObject(collider);
							// removeObject(unit);
						}
					}
				}
			}
		}

	}

	@Override
	public void render() {
		drawStatic();
		drawDynamic();
		drawGrid();

	}

	// ///////////////////////////////////////////////////////

	private void drawStatic() {
		for (Renderable object : staticObjects) {
			object.render();
		}
	}

	private void drawDynamic() {
		for (Updateable unit : dynamicObjects) {
			unit.render();
		}
	}

	private void init() {
		staticObjects = new ArrayList<Castle>();
		dynamicObjects = new ArrayList<DynamicObject>();

		float w, h;
		w = game.getGraphics().getWidth();
		h = game.getGraphics().getHeight();
		grid = new SpatialGrid(w, h, w / 3);

	}

	private void addUnit(Side side) {
		Unit unit = new Unit(side, game);
		grid.insertObject(unit);
		dynamicObjects.add(unit);
	}

	@Override
	public void addAllyUnit() {
		addUnit(Side.ALLY);
	}

	@Override
	public void addEnemyUnit() {
		addUnit(Side.ENEMY);
	}

	// ////////////////////

	private void drawGrid() {
		Graphics graphic = game.getGraphics();
		int cols = grid.getCellsPerRow();
		int rows = grid.getCellsPerCol();
		float dH = graphic.getHeight() / rows;
		float dW = graphic.getWidth() / cols;

		for (int i = 1; i < rows; i++) {
			float lineY = i * dH;
			graphic.drawLine(new Vector(0, lineY),
					new Vector(graphic.getWidth(), lineY), Color.RED);
		}

		for (int i = 0; i < cols; i++) {
			float lineX = i * dW;
			graphic.drawLine(new Vector(lineX, 0),
					new Vector(lineX, graphic.getHeight()), Color.BLUE);
		}
	}

	private void removeObject(GameObject object) {
		grid.removeObject(object);
		if (object instanceof DynamicObject) {
			dynamicObjects.remove(object);
		} else {
			staticObjects.remove(object);
		}
	}
}
