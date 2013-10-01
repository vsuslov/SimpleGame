package ru.rs.gameobjects;

import java.util.ArrayList;
import java.util.List;

import ru.rs.GameObject;
import ru.rs.SimpleGrid;
import ru.rs.objects.math.Rectangle;
import ru.rs.objects.math.Vector;

public class SpatialGrid extends SimpleGrid {

	List<GameObject>[] dynamicCells;

	public SpatialGrid(float worldWidth, float worldHeight, float cellSize) {
		super(worldWidth, worldHeight, cellSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initCells() {
		int numCells = cellsPerRow * cellsPerCol;

		dynamicCells = new List[numCells];
		staticCells = new List[numCells];

		for (int i = 0; i < numCells; i++) {
			dynamicCells[i] = new ArrayList<GameObject>(10);
			staticCells[i] = new ArrayList<GameObject>(10);
		}
		foundObjects = new ArrayList<GameObject>(10);
	}

	@Override
	public void insertObject(GameObject object) {

		int[] cellIds = getCellIds(object);
		int i = 0;
		int cellId = -1;
		while (i <= 3 && (cellId = cellIds[i++]) != -1) {
			if (object instanceof DynamicObject) {
				dynamicCells[cellId].add(object);
			} else {
				staticCells[cellId].add(object);
			}
		}
	}

	@Override
	public void removeObject(GameObject obj) {

		int[] cellIds = getCellIds(obj);
		int i = 0;
		int cellId = -1;
		while (i <= 3 && (cellId = cellIds[i++]) != -1) {
			if (obj instanceof DynamicObject) {
				dynamicCells[cellId].remove(obj);
			} else {
				staticCells[cellId].remove(obj);
			}
		}
	}

	public void clearDynamicCells() {

		int len = dynamicCells.length;
		for (int i = 0; i < len; i++) {
			dynamicCells[i].clear();
		}
	}

	public List<GameObject> getPotentialColliders(GameObject obj) {

		foundObjects.clear();
		int[] cellIds = getCellIds(obj);
		int i = 0;
		int cellId = -1;
		while (i <= 3 && (cellId = cellIds[i++]) != -1) {

			// TODO: оптимизировать циклы
			for (GameObject collider : dynamicCells[cellId]) {
				if (!obj.equals(collider) && !foundObjects.contains(collider)) {
					foundObjects.add(collider);
				}
			}

			for (GameObject collider : staticCells[cellId]) {
				if (!obj.equals(collider) && !foundObjects.contains(collider)) {
					foundObjects.add(collider);
				}
			}
		}
		return foundObjects;
	}

	@Override
	public int[] getCellIds(GameObject object) {

		Rectangle bounds = object.getBounds();
		Vector lowerLeft = bounds.lowerLeft;
		int x1 = (int) Math.floor(lowerLeft.x / cellSize);
		int y1 = (int) Math.floor(lowerLeft.y / cellSize);
		int x2 = (int) Math.floor((lowerLeft.x + bounds.width) / cellSize);
		int y2 = (int) Math.floor((lowerLeft.y + bounds.height) / cellSize);

		if (x1 == x2 && y1 == y2) {
			if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
				cellIds[0] = x1 + y1 * cellsPerRow;
			} else {
				cellIds[0] = -1;
			}
			cellIds[1] = -1;
			cellIds[2] = -1;
			cellIds[3] = -1;
		} else if (x1 == x2) {
			int i = 0;
			if (x1 >= 0 && x1 < cellsPerRow) {
				if (y1 >= 0 && y1 < cellsPerCol) {
					cellIds[i++] = x1 + y1 * cellsPerRow;
				}

				if (y2 >= 0 && y2 < cellsPerCol) {
					cellIds[i++] = x1 + y2 * cellsPerRow;
				}
			}
			while (i <= 3) {
				cellIds[i++] = -1;
			}
		} else if (y1 == y2) {
			int i = 0;
			if (y1 >= 0 && y1 < cellsPerCol) {
				if (x1 >= 0 && x1 < cellsPerRow) {
					cellIds[i++] = x1 + y1 * cellsPerRow;
				}
				if (x2 >= 0 && x2 < cellsPerRow) {
					cellIds[i++] = x2 + y1 * cellsPerRow;
				}
			}
			while (i <= 3) {
				cellIds[i++] = -1;
			}
		} else {
			int i = 0;
			int y1CellsPerRow = y1 * cellsPerRow;
			int y2CellsPerRow = y2 * cellsPerRow;

			if (x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
				cellIds[i++] = x1 + y1CellsPerRow;
			}
			if (x2 >= 0 && x2 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol) {
				cellIds[i++] = x2 + y1CellsPerRow;
			}
			if (x2 >= 0 && x2 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol) {
				cellIds[i++] = x2 + y2CellsPerRow;
			}
			if (x1 >= 0 && x1 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol) {
				cellIds[i++] = x1 + y2CellsPerRow;
			}
			while (i <= 3) {
				cellIds[i++] = -1;
			}
		}
		return cellIds;
	}

	// ////////////////////////////////
	/**
	 * @return the cellsPerRow
	 */
	@Override
	public int getCellsPerRow() {

		return cellsPerRow;
	}

	/**
	 * @return the cellsPerCol
	 */
	@Override
	public int getCellsPerCol() {

		return cellsPerCol;
	}

	/**
	 * @param cellsPerRow
	 *            the cellsPerRow to set
	 */
	@Override
	public void setCellsPerRow(int cellsPerRow) {

		this.cellsPerRow = cellsPerRow;
	}

	/**
	 * @param cellsPerCol
	 *            the cellsPerCol to set
	 */
	@Override
	public void setCellsPerCol(int cellsPerCol) {

		this.cellsPerCol = cellsPerCol;
	}

}
