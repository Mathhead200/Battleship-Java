package com.cyberninjaz.battleship;

import java.awt.Color;

import javax.swing.JLabel;


public class Cell extends JLabel {

	private static final long serialVersionUID = -380588606118443889L;

	private boolean marked = false;
	
	public Cell(Color bgColor) {
		setBackground(bgColor);
		setOpaque(true);
		setBorder(Battleship.CELL_BORDER);
		setFont(Battleship.CELL_FONT);
	}
	
	public boolean isMarked() {
		return marked;
	}
	
	public void mark(String mark, Color color) {
		if( isMarked() )
			throw new IllegalStateException("Cannot mark a cell more then once");
		
		marked = true;
		setForeground(color);
		setText( "<html><center>" + mark );
		
		repaint();
	}
	
}
