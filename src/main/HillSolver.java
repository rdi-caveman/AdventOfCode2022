package main;

import advent_code_common.Cell;
import advent_code_common.MazeSolver;

public class HillSolver extends MazeSolver {
	char[][] map;
	
	public int solve(char[][] map, int startRow, 
			int startCol, int endRow, int endCol) {
		this.map = map;
		return super.solve(map.length, map[0].length, startRow, startCol, endRow, endCol);
	}
	
	@Override
	public boolean isNavigable(Cell minCell, int row, int col) {
		if (map[minCell.row][minCell.col] == 'S') return true; // can always navigate away from start
		if (map[row][col] == 'E') return true; // can always navigate to end
		return map[row][col] - map[minCell.row][minCell.col] <=1;  // otherwise can go up one or down zero to many
	}
	
	@Override
	public int moveCost (int row, int col) {
		return 1;
	}	
}
