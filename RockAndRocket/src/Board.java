import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JFrame;


import javax.sound.sampled.*;
import java.io.IOException;
import java.net.*;
import javax.swing.*;

public class Board extends JFrame{
	
	public static int boardWidth = 1000;
	public static int boardHeight = 1000;
	
	public static boolean keyHeld = false;
	
	public static int keyHeldCode;
	
	public static ArrayList<PhotonTorpedo>torpedos = new ArrayList<PhotonTorpedo>();
	
	
	
	String thrustFile = "file:./src/thrust.au";
	String laserFile = "file:./src/laser.aiff";
	
	
	public static void main(String[] args) {
		
		new Board();
		
	}
	
	public Board() {
		this.setSize(boardWidth,boardHeight);
		this.setTitle("Rock and Rocket");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				

				if(e.getKeyCode()==87) {
					System.out.println("Forward");
					Board.playSoundEffect(thrustFile);
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				}
				else if(e.getKeyCode()==83) {
					System.out.println("Backward");
					keyHeldCode = e.getKeyCode();
					keyHeld = true;

				}
				else if(e.getKeyCode()==68) {
					System.out.println("Roatate Right");
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				}
				else if(e.getKeyCode()==65) {
					System.out.println("Roatate Left");
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				}
				else if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					Board.playSoundEffect(laserFile);
					torpedos.add(new PhotonTorpedo(GameDrawingPanel.theShip.getShipNoseX(),
							GameDrawingPanel.theShip.getShipNoseY(),
							GameDrawingPanel.theShip.getRotationAngle()));
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
				keyHeld = false;
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
				
			}
			
		});
		
		GameDrawingPanel gamePanel = new GameDrawingPanel();
		
		this.add(gamePanel,BorderLayout.CENTER);
		
		ScheduledThreadPoolExecutor excecutor = new ScheduledThreadPoolExecutor(1);
		
		excecutor.scheduleAtFixedRate(new RepaintTheBoard(this), 0L, 15L, TimeUnit.MILLISECONDS);
		
		this.setVisible(true);
		
	}
	
public static void playSoundEffect(String soundToPlay){
    	
    	// Pointer towards the resource to play
		URL soundLocation;
		try {
			soundLocation = new URL(soundToPlay);
		 
        	    // Stores a predefined audio clip
        	    Clip clip = null;
				
				// Convert audio data to different playable formats
				clip = AudioSystem.getClip();
				
				// Holds a stream of a definite length
        	    AudioInputStream inputStream;

				inputStream = AudioSystem.getAudioInputStream(soundLocation);

				// Make audio clip available for play
				clip.open( inputStream );
					
				// Define how many times to loop
				clip.loop(0);
				
				// Play the clip
	        	clip.start();
				} 
				
				catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	    
        	    catch (UnsupportedAudioFileException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	    
        	    catch (LineUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    	
    }
		
	
}


class RepaintTheBoard implements Runnable {
	Board theBoard;
	
	public RepaintTheBoard(Board theBoard) {
		this.theBoard = theBoard;
	}
	
	public void run() {
		theBoard.repaint();
		
	}
}


class GameDrawingPanel extends JComponent{
	public ArrayList<Rock> rocks= new ArrayList<Rock>();
	int[] PolyXArray = Rock.sPolyXArray;
	int[] poluYArray = Rock.sPolyYArray;
	
	static SpaceShip theShip = new SpaceShip();
	
	int width = Board.boardWidth;
	int height = Board.boardHeight;
	
	
	public GameDrawingPanel() {
		for(int i=0;i<15;i++) {
			int randomStartXPos = (int)(Math.random()*(Board.boardWidth-40)+1);
			int randomStartYPos = (int)(Math.random()*(Board.boardHeight-40)+1);
			
			rocks.add(new Rock(Rock.getPolyXArray(randomStartXPos),Rock.getPolyYArray(randomStartYPos),13,randomStartXPos,randomStartYPos));
			Rock.rocks = rocks;
		}
	}
	
	
	public void paint(Graphics g) {
		
		Graphics2D graphicSettings = (Graphics2D)g;
		
		AffineTransform identity = new AffineTransform();
		
		
		graphicSettings.setColor(Color.BLACK);
		
		graphicSettings.fillRect(0, 0, getWidth(), getHeight());
		
		graphicSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
	
		for(Rock rock:rocks) {
			
			if(rock.onScreen) {
				
				rock.move(theShip,Board.torpedos);
				graphicSettings.setPaint(Color.WHITE);
				graphicSettings.draw(rock);
				graphicSettings.setColor(Color.CYAN);
				graphicSettings.fill(rock);
			}
			
		}
		
		if(Board.keyHeld==true&& Board.keyHeldCode == 68) {
			theShip.increaseRotationAngle();
			System.out.println("Ship Angle: " + theShip.getRotationAngle());
		}
		else if(Board.keyHeld==true&& Board.keyHeldCode == 65) {
			theShip.decreaseRotationAngle();
			System.out.println("Ship Angle: " + theShip.getRotationAngle());
		}
		else if(Board.keyHeld==true&& Board.keyHeldCode == 87) {
			theShip.setMovingAngle(theShip.getRotationAngle());

			theShip.increaseXVelocity(theShip.shipXMoveAngle(theShip.getMovingAngle())*0.1);
			theShip.increaseYVelocity(theShip.shipYMoveAngle(theShip.getMovingAngle())*0.1);
	
		}
		/*else if(Board.keyHeld==true&& Board.keyHeldCode == 83) {
			theShip.setMovingAngle(theShip.getRotationAngle());
			theShip.decreaseXVelocity(theShip.shipXMoveAngle(theShip.getMovingAngle())*-0.1);
			theShip.decreaseYVelocity(theShip.shipYMoveAngle(theShip.getMovingAngle())*-0.1);
		}*/
		
		
		theShip.move();
		
		
		graphicSettings.setTransform(identity);
		graphicSettings.translate(theShip.getXCenter(),theShip.getYCenter());
		graphicSettings.rotate(Math.toRadians(theShip.getRotationAngle()));
		 
		
		graphicSettings.setColor(Color.RED);
		graphicSettings.draw(theShip);
		graphicSettings.setColor(Color.ORANGE);
		graphicSettings.fill(theShip);
		
		
		for(PhotonTorpedo torpedo : Board.torpedos) {
			torpedo.move();
			
			if(torpedo.onScreen) {
				graphicSettings.setTransform(identity);
				
				graphicSettings.translate(torpedo.getXCenter(), torpedo.getYCenter());
				graphicSettings.setColor(Color.RED);
				graphicSettings.draw(torpedo);
				graphicSettings.fill(torpedo);
			}
			
		}
		
	}
	
}
