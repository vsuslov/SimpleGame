package ru.rs.gameobjects;

import android.graphics.Color;
import ru.rs.Renderable;
import ru.rs.interfaces.Game;
import ru.rs.interfaces.Graphics;
import ru.rs.objects.math.Rectangle;
import ru.rs.objects.math.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadim Suslov on 30.05.14.
 * 1D Grid for 1D games
 */
public class GeneralGrid implements Renderable{
    /**
     * List potential colliders
     */
    private List<SimpleObject> foundObjects;
    /**
     * Game interface
     */
    private Game game;
    /**
     * Number of columns
     */
    private int cols;
    /**
     * objects containing in the cells
     */
    private List<SimpleObject>[] cells;


    /**
     * Constructor for grid
     * @param game game interface
     * @param i number of cells per row;
     */
    public GeneralGrid(Game game, int i) {
        this.game=game;
        cols=i;
        cells=new List[cols];
        for(int j=0;j<cols;j++) {
            cells[j]=new ArrayList<SimpleObject>();
        }
    }

    /**
     * insert object into Grid
     * @param object object to insert
     */
    public void insertObject(SimpleObject object) {
        int[] cls=getCellIds(object);

        for(Integer i:cls) {
           cells[i].add(object);
        }
    }

     public void insertObjects(SimpleObject... objects) {
       for(SimpleObject obj:objects) {
            insertObject(obj);
       }
     }
    /**
     * Method for remove object from grid;
     * @param object object to delete
     */
    public void removeObject(SimpleObject object) {
         int[] cls=getCellIds(object);

        for(Integer i:cls) {
            cells[i].remove(object);
        }
    }

    /**
     * Method for clear particular cell
     * @param cellId id of cell
     */
    public void clearCell(int cellId) {
        cells[cellId].clear();
    }
    /**
     * Getting X coordinate as integer cell-coordinate
     * @param x double original coordinate
     * @return integer cell-coordinate
     */
    private int convertX(double x) {
       double colSize=game.getGraphics().getWidth()/cols;
       int result=(int) Math.floor(x/colSize);
       return result;
    }

    /**
     * method for determining cell ids of object
     * @param object given object
     * @return ids of cells containing object
     */
    private int[] getCellIds(SimpleObject object) {
        Rectangle bounds = object.getBounds();
        Vector lowerLeft=bounds.lowerLeft;
        int x1=convertX(lowerLeft.x);
        int x2=convertX(lowerLeft.x+bounds.width);

        if(x1!=x2) {
            int[] result={x1,x2};
            return result;
        } else { int[] result={x1};return result;}
    }

    /**
     * Method for determine potential colliders
     * in the same cell as given object in
     * @param object given object
     * @return potential colliders
     */
    public List<SimpleObject> getPotentialColliders(SimpleObject object) {
      int[] cls=getCellIds(object);

        for(int i:cls) {
           for(SimpleObject colider:cells[i]) {
               if(!object.equals(colider) && !object.side.equals(colider.side) && !foundObjects.contains(colider)) {
                   foundObjects.add(object);
               }
           }
        }
        return foundObjects;
    }

    /**
     * Method for render the grid
     */
    @Override
    public void render() {
        Graphics graphic=game.getGraphics();
        float h=graphic.getHeight(),w=graphic.getWidth()/cols;
        float colSize = graphic.getWidth()/cols;
        for(int i=0;i<cols;i++) {
            Vector start=new Vector((i+1)*colSize,0);
            Vector end=new Vector(start);
            end.setY(h);
            graphic.drawLine(start,end, Color.BLUE);
        }
    }
}
