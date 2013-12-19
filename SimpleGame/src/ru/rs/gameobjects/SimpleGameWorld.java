package ru.rs.gameobjects;

import android.graphics.Color;
import ru.rs.GameObject;
import ru.rs.GameWorld;
import ru.rs.Renderable;
import ru.rs.Updateable;
import ru.rs.interfaces.Game;
import ru.rs.interfaces.Graphics;
import ru.rs.interfaces.Input;
import ru.rs.objects.math.Vector;
import ru.rs.utils.math.FigureUtils;

import java.util.ArrayList;
import java.util.List;
//считаю, нужно объектную модель(человечки, здания) 
//засовывать в грид и оттуда их прорисовывать, а не в гейм ворлде.

public class SimpleGameWorld implements GameWorld {

	private List<Castle> staticObjects;
	private List<DynamicObject> dynamicObjects;
	private Game game;
	private SpatialGrid grid;
	private Graphics graphics;
    private Long lastClicked=0L;

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

    @Override
    public void touchProccess(List<Input.TouchEvent> touchEvents) {
          if(!touchEvents.isEmpty()) {
             for(Input.TouchEvent event:touchEvents) {
                 Castle castle=staticObjects.get(0);

                 if(event.x<(castle.getPosition().x+castle.width) && event.y>(graphics.getHeight()-(castle.getPosition().y+castle.height)) && (System.currentTimeMillis()-lastClicked)>5000) {

                     addAllyUnit();
                     lastClicked=System.currentTimeMillis();
                 }

             }

          }
    }

    // ////////////////////

	private void drawGrid() {
		int cols = grid.getCellsPerRow();
		int rows = grid.getCellsPerCol();
		float dH = graphics.getHeight() / rows;
		float dW = graphics.getWidth() / cols;

		for (int i = 1; i < rows; i++) {
			float lineY = i * dH;
			graphics.drawLine(new Vector(0, lineY),
					new Vector(graphics.getWidth(), lineY), Color.RED);
		}

		for (int i = 0; i < cols; i++) {
			float lineX = i * dW;
			graphics.drawLine(new Vector(lineX, 0),
					new Vector(lineX, graphics.getHeight()), Color.BLUE);
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
