package com.cyberninjaz.battleship;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class Boat implements Iterable<Coordinate> {
	
	private static final List<String> names = Arrays.asList("Submarine", "Patrol boat", "Destroyer", "Battleship", "Aircraft Carrier");
	
	public static enum Orientation { HORIZONTAL, VERTICAL }
	
	public final int size;
	public final Orientation orientation;
	public final Coordinate location;
	
	public Boat(int size, String location, Orientation orientation) {
		if( size <= 0 )
			throw new IllegalArgumentException("Boat's size must be positive: " + size);
		if( orientation == null )
			throw new IllegalAccessError("Boat's orientation must be non-null" );
		this.size = size;
		this.orientation = orientation;
		this.location = new Coordinate(location);
	}
	
	public String getName() {
		try {
			return names.get(size - 1);
		} catch(IndexOutOfBoundsException e) {
			return size + "-Boat";
		}
	}
	
	public boolean equals(Object obj) {
		if( obj == this )
			return true;
		if( !(obj instanceof Boat) )
			return false;
		Boat that = (Boat) obj;
		return this.size == that.size && this.orientation == that.orientation;
	}
	
	public int hashCode() {
		return orientation.hashCode() + 31 * (location.hashCode() + 31 * size);
	}
	
	public String toString() {
		return getName();
	}

	public Iterator<Coordinate> iterator() {
		return new Iterator<Coordinate>() {
			int offset = 0;
			
			public boolean hasNext() {
				return offset < size;
			}

			public Coordinate next() {
				int i = location.row;
				int j = location.col;
				if( orientation == Boat.Orientation.HORIZONTAL )
					j += offset;
				else if( orientation == Boat.Orientation.VERTICAL )
					i += offset;
				offset++;
				return new Coordinate(i, j);
			}
		};
	}
	
}
