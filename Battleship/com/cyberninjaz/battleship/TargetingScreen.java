package com.cyberninjaz.battleship;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;


public class TargetingScreen extends Grid {
	
	private static final long serialVersionUID = -2622526623573271190L;
	
	private boolean uiPrimed = false;
	private Cell selectedCell = null;
	
	public TargetingScreen(PrintWriter out) {
		super(Coordinate.ROW_COUNT, Coordinate.COL_COUNT, Battleship.THEIR_CELL_COLOR);
		for( int i = 0; i < Coordinate.ROW_COUNT; i++ ) {
			for( int j = 0; j < Coordinate.COL_COUNT; j++ ) {
				Cell cell = getCell(i, j);
				Coordinate coord = new Coordinate(i, j);
				cell.addMouseListener( new MouseListener() {
					public void mouseEntered(MouseEvent e) {
						if( uiPrimed && !cell.isMarked() )
							cell.setBorder(Battleship.SELECTED_CELL_BORDER);
					}
					
					public void mouseExited(MouseEvent e) {
						cell.setBorder(Battleship.CELL_BORDER);
					}
					
					public void mouseClicked(MouseEvent e) {
						if( uiPrimed && !cell.isMarked() ) {
							uiPrimed = false;
							selectedCell = cell;
							Battleship.send( out, coord.toString() );
						}
					}
					
					public void mousePressed(MouseEvent e) {}
					public void mouseReleased(MouseEvent e) {}
				});
			}
		}
	}

	public void primeUI() {
		uiPrimed = true;
	}

	public boolean processResponse(String response) {
		if( selectedCell == null )
			throw new IllegalStateException("Application protocol synchronization issuse! TargetingScreen.selectedCell == null");
		
		if( response.equals(Battleship.HIT_MESSAGE) ) {
			selectedCell.mark(Battleship.HIT_MARK, Battleship.HIT_MARK_COLOR);
			selectedCell = null;
			return true;
		} else if( response.equals(Battleship.MISS_MESSAGE) ) {
			selectedCell.mark(Battleship.MISS_MARK, Battleship.MISS_MARK_COLOR);
			selectedCell = null;
			return false;
		} else
			throw new IllegalArgumentException("Unrecognized response message: " + response);
	}
	
}
