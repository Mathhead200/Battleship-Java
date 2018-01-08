package com.cyberninjaz.battleship;

import java.util.Collection;


public class Board extends Grid {

	private static final long serialVersionUID = 5951631381218216828L;
	
	private static final int BOAT_COUNT = 5;
	private static final int MIN_BOAT_SIZE = 2;
	private static final int MAX_BOAT_SIZE = 5;
	private static final int MIN_TOTAL_SIZE = 17;
	private static final int MAX_TOTAL_SIZE = 20;
	
	private Collection<Boat> boats;
	private int totalSize = 0;
	private int hitCount = 0;
	
	public Board(Collection<Boat> boats) {
		super(Coordinate.ROW_COUNT, Coordinate.COL_COUNT, Battleship.MY_CELL_COLOR);
		
		this.boats = boats;
		
		// validate boats collection
		if( boats.size() != 5 )
			throw new IllegalArgumentException("Must play with " + BOAT_COUNT + " boats!");
		for( Boat boat : boats ) {
			if( boat.size < MIN_BOAT_SIZE )
				throw new IllegalArgumentException("Boat size must be at least " + MIN_BOAT_SIZE + ": " + boat.size);
			if( boat.size > MAX_BOAT_SIZE )
				throw new IllegalArgumentException("Boat size can be at most " + MAX_BOAT_SIZE + ": " + boat.size);
			totalSize += boat.size;
			
			for( Coordinate coord : boat ) {
				if( coord.row < 0 || coord.row >= Coordinate.ROW_COUNT || coord.col < 0 || coord.col > Coordinate.COL_COUNT )
					throw new IllegalArgumentException(boat + " at " + boat.location + " must stay on the board: (" + coord.row + "," + coord.col + ")");
				Cell cell = getCell(coord.row, coord.col);
				if( cell.getBackground().equals(Battleship.SHIP_COLOR) )
					throw new IllegalArgumentException("Boats can not overlap: overlap at " + coord);
				cell.setBackground(Battleship.SHIP_CELL_COLOR);
				cell.setForeground(Battleship.SHIP_COLOR);
				cell.setText( boat.getName().substring(0, 1).toUpperCase() );
			}
		}
		if( totalSize < MIN_TOTAL_SIZE )
			throw new IllegalArgumentException("Total fleet (all boats' combine) size must be at least " + MIN_BOAT_SIZE + ": " + totalSize);
		if( totalSize > MAX_TOTAL_SIZE )
			throw new IllegalArgumentException("Total fleet (all boats' combine) size can be at most " + MAX_BOAT_SIZE + ": " + totalSize);
	}

	public boolean mark(Coordinate coord) {
		for( Boat boat : boats ) {
			for( Coordinate c : boat ) {
				if( coord.equals(c) ) {
					getCell(c.row, c.col).mark(Battleship.HIT_MARK, Battleship.HIT_MARK_COLOR);
					hitCount++;
					return true;
				}
			}
		}
		getCell(coord.row, coord.col).mark(Battleship.MISS_MARK, Battleship.MISS_MARK_COLOR);
		return false;
	}

	public boolean isGameOver() {
		return hitCount >= totalSize;
	}
	
}
