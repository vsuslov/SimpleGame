package ru.rs.gameobjects;

import ru.rs.interfaces.Game;

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
        int x1=convertX(object.getBounds().lowerLeft.x);
        int x2=convertX(object.getBounds().lowerLeft.x+object.getBounds().width);

        if(x1!=x2) {
            cells[x2].add(object);
        }
        cells[x1].add(object);
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
}
