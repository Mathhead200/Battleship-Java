package com.cyberninjaz.battleship;

public class Coordinate {

	public static final int ROW_COUNT = 10;
	public static final int COL_COUNT = 10;
	
	public final int row;
	public final int col;
	
	private final String str;
	
	public Coordinate(String str) {
		str = str.toUpperCase();
		
		if( !str.matches("[A-J]([1-9]|10)") )
			throw new IllegalArgumentException("Invalid coordinate: " + str);
		
		this.str = str;
		
		col = str.charAt(0) - 'A';
		row = Integer.parseInt(str.substring(1)) - 1;
	}
	
	public Coordinate(int row, int col) {
		this.row = row;
		this.col = col;
		str = "" + (char) ('A' + col) + (row + 1);
	}
	
	public String toString() {
		return str;
	}
	
	public boolean equals(Object obj) {
		if( this == obj )
			return true;
		if( !(obj instanceof Coordinate) )
			return false;
		Coordinate that = (Coordinate) obj;
		return this.row == that.row && this.col == that.col;
	}
	
	public int hashCode() {
		return row + 31 * col;
	}
	
}
