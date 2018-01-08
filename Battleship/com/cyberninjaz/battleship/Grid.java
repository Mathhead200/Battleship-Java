package com.cyberninjaz.battleship;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;


public class Grid extends JPanel {

	private static final long serialVersionUID = -7866744694926595476L;
	
	private Cell[][] cells;
	
	public Grid(int rows, int cols, Color background) {
		super( new GridLayout(rows, cols) );
		setPreferredSize( new Dimension(Battleship.WINDOW_SIZE, Battleship.WINDOW_SIZE) );
		
		cells = new Cell[rows][cols];
		for( int i = 0; i < rows; i++ )
			for( int j = 0; j < cols; j++ )
				add( cells[i][j] = new Cell(background) );
	}
	
	public Cell getCell(int row, int col) {
		return cells[row][col];
	}
	
}
