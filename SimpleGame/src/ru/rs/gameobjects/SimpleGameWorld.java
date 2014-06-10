package ru.rs.gameobjects;

import ru.rs.GameWorld;
import ru.rs.Renderable;
import ru.rs.Updateable;
import ru.rs.interfaces.Game;
import ru.rs.interfaces.Input;
import ru.rs.utils.math.FigureUtils;

import java.util.ArrayList;
import java.util.List;

public class SimpleGameWorld implements GameWorld {

	private List<Renderable> staticObjects;
	private List<Updateable> dynamicObjects;
	private Game game;
//	private SimpleGrid grid;
    private Long clickedAt=0L;

//    private GeneralGrid grid;

	public SimpleGameWorld(Game game) {
		this.game = game;
		init();
        addCastles();
//		staticObjects.add(new Castle(Side.ALLY, this.game));
//		staticObjects.add(new Castle(Side.ENEMY, this.game));
	}

	// ///////////////////////////////////////////////////////

	@Override
	public void update() {
		for (Updateable unit : dynamicObjects) {
			unit.update();
 		}
        processCollisions();
	}

	@Override
	public void render() {
		drawStatic();
		drawDynamic();
//        grid.render();
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

//		float w, h;
//		w = game.getGraphics().getWidth();
//		h = game.getGraphics().getHeight();
//		grid = new SimpleGrid(w, h, 6);
//        grid=new GeneralGrid(game,3);
//		drawGrid();
	}

	private void addUnit(Side side) {
        if(System.currentTimeMillis()-clickedAt>=1500||clickedAt==0) {
            Unit unit=new Unit(side,game);
            dynamicObjects.add(unit);
//            grid.insertObject(unit);
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
    private void addCastles() {
        Castle ally=new Castle(Side.ALLY,this.game);
        Castle enemy=new Castle(Side.ENEMY,this.game);
        staticObjects.add(ally);
        staticObjects.add(enemy);
//        grid.insertObjects(ally,enemy);
    }
	// ////////////////////

//	private void drawGrid() {
//		Graphics graphic = game.getGraphics();
//		int cols = grid.getCellsPerRow();
//		int rows = grid.getCellsPerCol();
//		float dH = graphic.getHeight() / rows;
//		float dW = graphic.getWidth() / cols;
//
//		for (int i = 1; i < rows; i++) {
//			float lineY = i * dH;
//			graphic.drawLine(new Vector(0, lineY),
//					new Vector(graphic.getWidth(), lineY), Color.RED);
//		}
//
//		for (int i = 0; i < cols; i++) {
//			float lineX = i * dW;
//			graphic.drawLine(new Vector(lineX, 0),
//					new Vector(lineX, graphic.getHeight()), Color.BLUE);
//		}
//	}

    public void touch(List<Input.TouchEvent> touches) {
        if(touches.size()>0) {
            for(Input.TouchEvent event:touches) {
                if(!(event.y<game.getGraphics().getHeight()-32)) {
                    if(event.x<32) {
                        addAllyUnit();
                    } else if(event.x>game.getGraphics().getWidth()-32) {
                        addEnemyUnit();
                    }
                }
            }
        }
    }

    /**
     * Bullky collision processing it will be deprecated!
     */
    public void processCollisions() {
        //Use Iterator because of deletion from collection
     for(Updateable dinam:new ArrayList<Updateable>(dynamicObjects)) {
         SimpleObject ob1=((SimpleObject) dinam);
         for(Updateable opponent:new ArrayList<Updateable>(dynamicObjects)) {
             SimpleObject ob2=((SimpleObject) opponent);
                 if(testOverlap(ob1,ob2)){
                   dynamicObjects.remove(dinam);
                   dynamicObjects.remove(opponent);
                 }
         }
         // go through staticObjects
//         for(Iterator opponent=staticObjects.iterator();opponent.hasNext();) {
//             SimpleObject ob2=(SimpleObject) opponent.next();
//             if(testOverlap(ob1,ob2)){
//                 dinam.remove();
//                 dinam.next();
//                 opponent.remove();
//             }
//         }


     }
    }

    /**
     * Method for testing overlap between two units
     * @param obj1 unit 1
     * @param obj2 unit 2
     * @return true if overlaps, false otherwise
     */
    private boolean testOverlap(SimpleObject obj1, SimpleObject obj2) {
        return !obj1.equals(obj2) && FigureUtils.overlap(obj1.getBounds(),obj2.getBounds()) && !(obj1.side.equals(obj2.side));
    }
}
