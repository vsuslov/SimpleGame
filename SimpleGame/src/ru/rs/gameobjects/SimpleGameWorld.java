package ru.rs.gameobjects;

import java.util.ArrayList;
import java.util.List;

import ru.rs.DynamicGameObject;
import ru.rs.GameObject;
import ru.rs.GameWorld;
import ru.rs.Renderable;
import ru.rs.SpatialGrid;
import ru.rs.Updateable;
import ru.rs.interfaces.Game;
import ru.rs.interfaces.Graphics;
import ru.rs.objects.math.Vector;
import ru.rs.utils.math.FigureUtils;
import android.graphics.Color;
//считаю, нужно объектную модель(человечки, здания) 
//засовывать в грид и оттуда их прорисовывать, а не в гейм ворлде.

public class SimpleGameWorld implements GameWorld {

	private List<Renderable> staticObjects;
	private List<Updateable> dynamicObjects;
	private Game game;
	private SpatialGrid grid;
	private Graphics graphics;

	// Test
	List<GameObject> list;

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
		addAllyUnit();
	}

	// ///////////////////////////////////////////////////////

	@Override
	public void update() {
		for (Updateable unit : dynamicObjects) {
			unit.update();
		}
		if (dynamicObjects.size() > 0) {
			for (Updateable object : dynamicObjects) {
				GameObject obj = (SimpleObject) object;
				list = grid.getPotentialColliders(obj);
				for (GameObject collider : list) {
					if (FigureUtils.overlap(obj.getBounds(),
							collider.getBounds())) {
						if (collider != null
								&& !((SimpleObject) collider).side
										.equals(((SimpleObject) obj).side)) {
							grid.removeObject(collider);
							if (collider instanceof DynamicGameObject)
								dynamicObjects.remove(collider);
							else
								staticObjects.remove(collider);
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
		if (dynamicObjects.size() > 0) {
			list = grid.getPotentialColliders((GameObject) dynamicObjects
					.get(0));

			game.getGraphics().drawText(String.valueOf(list.size()),
					new Vector(100, 20), Color.RED);
		}

	}

	// ///////////////////////////////////////////////////////

	private void drawStatic() {
		for (Renderable object : staticObjects) {
			object.render();
			// Graphics g = game.getGraphics();
			// SimpleObject simple = (SimpleObject) object;

			// g.drawText(String.valueOf(grid.getCellIds(simple)[0]), new
			// Vector(
			// simple.getPosition().x, 20), Color.YELLOW);
		}
	}

	private void drawDynamic() {
		for (Updateable unit : dynamicObjects) {
			//
			SimpleObject simple = (SimpleObject) unit;
			int[] imas = grid.getCellIds(simple);
			String stat = String.valueOf(imas[0]);
			Vector pos = simple.getPosition();
			pos.y = graphics.getHeight() - (pos.y + simple.height + 2);
			graphics.drawText(stat, pos, Color.GREEN);
			stat = String.valueOf(imas[1]);
			pos.x = pos.x + simple.width;
			graphics.drawText(stat, pos, Color.YELLOW);
			//

			unit.render();
		}
	}

	private void init() {
		staticObjects = new ArrayList<Renderable>();
		dynamicObjects = new ArrayList<Updateable>();

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
}
