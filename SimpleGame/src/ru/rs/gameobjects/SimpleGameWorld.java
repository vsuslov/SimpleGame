package ru.rs.gameobjects;

import android.graphics.Color;
import ru.rs.*;
import ru.rs.interfaces.Game;
import ru.rs.interfaces.Graphics;
import ru.rs.interfaces.Input;
import ru.rs.objects.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class SimpleGameWorld implements GameWorld {

	private List<Renderable> staticObjects;
	private List<Updateable> dynamicObjects;
	private Game game;
	private SimpleGrid grid;
    private Long clickedAt=0L;

	public SimpleGameWorld(Game game) {
		this.game = game;
		init();
		staticObjects.add(new Castle(Side.ALLY, this.game));
		staticObjects.add(new Castle(Side.ENEMY, this.game));
	}

	// ///////////////////////////////////////////////////////

	@Override
	public void update() {
		for (Updateable unit : dynamicObjects) {
			unit.update();
		}
	}

	@Override
	public void render() {
		drawStatic();
		drawDynamic();
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
		staticObjects = new ArrayList<Renderable>();
		dynamicObjects = new ArrayList<Updateable>();

		float w, h;
		w = game.getGraphics().getWidth();
		h = game.getGraphics().getHeight();
		grid = new SimpleGrid(w, h, 6);

		drawGrid();
	}

	private void addUnit(Side side) {
        if(System.currentTimeMillis()-clickedAt>=1500||clickedAt==0) {
		    dynamicObjects.add(new Unit(side, game));
            clickedAt=System.currentTimeMillis();
        }
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

    public void touch(List<Input.TouchEvent> touches) {
        if(touches.size()>0) {
            for(Input.TouchEvent event:touches) {
                if(event.x<32&&!(event.y<game.getGraphics().getHeight()-32)) {
                    addAllyUnit();
                }
            }
        }
    }
}
