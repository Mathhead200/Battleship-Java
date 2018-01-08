package com.cyberninjaz.battleship;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import static com.cyberninjaz.battleship.Boat.Orientation.HORIZONTAL;
import static com.cyberninjaz.battleship.Boat.Orientation.VERTICAL;


public class Battleship {
	
	// You might need to mess with this stuff.
	
	static final Role MY_ROLE = Role.CLIENT;
	static final String HOST = "localhost";
	static final int PORT = "Cyberninjaz".hashCode() & 0xFFFF;
	
	// Must have exactly 5 boats in your fleet, each with size 2, 3, 4, or 5,
	// non-overlapping, and with a combine fleet size of 17 to 20 (inclusive)
	static final Collection<Boat> MY_BOATS = Arrays.asList( new Boat[]{
			new Boat(5, "J4", VERTICAL),
			new Boat(4, "G2", HORIZONTAL),
			new Boat(3, "B5", HORIZONTAL),
			new Boat(3, "D7", HORIZONTAL),
			new Boat(2, "C1", VERTICAL)
	} );
	
	
	// You can mess with this stuff if you want.
	
	static final int WINDOW_SIZE = 480;
	
	static final Color SHIP_CELL_COLOR = Color.GRAY.darker();
	static final Color SHIP_COLOR = Color.GRAY.brighter();
	
	static final Color MY_CELL_COLOR = Color.GRAY;
	static final Color THEIR_CELL_COLOR = new Color(145, 200, 255);
	
	static final String HIT_MARK = "X";
	static final Color HIT_MARK_COLOR = Color.RED;
	static final String MISS_MARK = "O";
	static final Color MISS_MARK_COLOR = Color.WHITE;
	
	static final Font CELL_FONT = new Font(Font.MONOSPACED, Font.BOLD, 32);
	static final Border CELL_BORDER = BorderFactory.createRaisedBevelBorder();
	static final Border SELECTED_CELL_BORDER = BorderFactory.createLoweredBevelBorder();
	
	static final Role FIRST_PLAYER = Math.random() < 0.50 ? Role.CLIENT : Role.SERVER;
	
	
	// --------------------------------------------------------------------------------
	
	// Don't mess with this stuff though!
	
	
	private static final String YOU_WIN_MESSAGE = "You win.";
	private static final String GAME_CONT_MESSAGE = "Game continues...";
	static final String HIT_MESSAGE = "Hit.";
	static final String MISS_MESSAGE = "Miss.";
	
	private enum Role { CLIENT, SERVER }
	
	static void send(PrintWriter out, String message) {
		System.out.println("<< " + message);
		out.println(message);
		out.flush();
	}
	
	static String receive(BufferedReader in) throws IOException {
		String response = in.readLine();
		System.out.println(">> " + response);
		return response;
	}
	
	public static void main(String[] args) {
		
		try {
			
			// Make network connection
			Socket socket;
			
			if( MY_ROLE == Role.SERVER ) {
				ServerSocket server = new ServerSocket(PORT);
				System.out.println( "Server at " + server.getInetAddress() + " on port " + server.getLocalPort() );
				System.out.println( "Waiting for connection from Client..." );
				socket = server.accept();
				server.close();
			} else if( MY_ROLE == Role.CLIENT ) {
				System.out.println( "Trying to connect to Server at " + HOST + " on port " + PORT + "..." );
				socket = new Socket(HOST, PORT);
			} else {
				System.err.println("Illegal run mode: " + MY_ROLE);
				return;
			}
			System.out.println( "Connected. Game starting..." );
			System.out.println("----------------------------------------");
			
			// Set up game
			PrintWriter out = new PrintWriter( socket.getOutputStream() );
			BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()) );
			boolean myTurn;
			
			if( MY_ROLE == Role.SERVER ) {
				send( out, FIRST_PLAYER.name() );
				myTurn = ( FIRST_PLAYER == Role.SERVER );
			} else {
				myTurn = ( MY_ROLE == Role.valueOf(in.readLine()) );
			}
			
			// Set up game and GUI
			TargetingScreen targetingScreen = new TargetingScreen(out);
			Board friendlyWaters = new Board(MY_BOATS);
			
			JFrame tsFrame, fwFrame;
			
			{	String titlePrefix ="Cyberninjaz Battleship (" + MY_ROLE.toString().toUpperCase() + ") - ";
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				
				tsFrame = new JFrame(titlePrefix + "Targeting Screen");
				tsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				tsFrame.add(targetingScreen);
				tsFrame.pack();
				tsFrame.setLocation( (int) (screenSize.width * 0.1), (int) (screenSize.height * 0.1) );
				
				fwFrame = new JFrame(titlePrefix + "My Boats");
				fwFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				fwFrame.add(friendlyWaters);
				fwFrame.pack();
				fwFrame.setLocation( (int) (screenSize.width * 0.9) - fwFrame.getWidth(), (int) (screenSize.height * 0.1) );
			}
			
			fwFrame.setVisible(true);
			tsFrame.setVisible(true);
			
			// Start playing
			Boolean weWinYouLose = null;
			while( weWinYouLose == null ) {
				if( myTurn ) {
					targetingScreen.primeUI();
					if( !targetingScreen.processResponse(receive(in)) )
						myTurn = false; // I missed, your turn
					if( receive(in).equals(YOU_WIN_MESSAGE) )
						weWinYouLose = true;
				} else {
					if( friendlyWaters.mark(new Coordinate(receive(in))) ) {
						send(out, HIT_MESSAGE);
						// myTurn = false; // their turn again
					} else {
						send(out, MISS_MESSAGE);
						myTurn = true; // my turn now
					}
					send( out, friendlyWaters.isGameOver() ? YOU_WIN_MESSAGE : GAME_CONT_MESSAGE );
				}
			}
			
			// Game over
			socket.close();
			
			System.out.println("----------------------------------------");
			if( weWinYouLose ) {
				System.out.println("You Win!");
				JOptionPane.showMessageDialog(null, "You Win!");
			} else {
				System.out.println("You Lose!");
				JOptionPane.showMessageDialog(null, "You Lose!");
			}
			
			tsFrame.dispose();
			fwFrame.dispose();
			
		} catch(IOException e) {
			System.err.println(e);
		}
		
	}
}
