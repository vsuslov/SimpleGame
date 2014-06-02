package ru.rs.gameobjects;

import ru.rs.interfaces.Game;
import ru.rs.objects.math.Rectangle;
import ru.rs.objects.math.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadim Suslov on 30.05.14.
 * 1D Grid for 1D games
 */
public class GeneralGrid {

    private List<SimpleObject> objects;
    private Game game;
    private int cols;
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
       int colSize=game.getGraphics().getWidth()/cols;
       int result=(int) Math.floor(x/colSize);
       return result;
    }

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

}
