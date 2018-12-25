import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Polygon;
import java.awt.Rectangle; 
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

class Rock extends Polygon {
	
	int uLeftXPos, uLeftYPos;
	
	int xDirection = 1;
	int yDirection = 1;
	
	int rockWidth = 26;
	int rockHeight = 31;
	
	
	
	 static ArrayList<Rock> rocks = new ArrayList<Rock>();
	
	int width = Board.boardWidth;
	int height = Board.boardHeight;
	
	int[] polyXArray, polyYArray[];
	public boolean onScreen = true;
	
	public static int[] sPolyXArray = {10,17,26,34,27,36,26,14,8,1,5,1,10};
	public static int[] sPolyYArray = {0,5,1,8,13,20,31,28,31,22,16,7,0};	
	
	
	public Rock(int[] polyXArray,int polyYArray[],int pointsInPoly, int randomStartXPos,int randomStartYPos) {
		super(polyXArray,polyYArray,pointsInPoly);
		
		this.xDirection = (int)(Math.random()*4+1);
		this.yDirection = (int)(Math.random()*4+1);
		this.uLeftXPos = randomStartXPos;
		this.uLeftYPos = randomStartYPos;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(super.xpoints[0],super.ypoints[0],rockWidth,rockHeight);
	}
	

	String explodeFile = "file:./src/explode.wav";
	
public void move(SpaceShip theShip, ArrayList<PhotonTorpedo> torpedos){
		
		// This rectangle surrounds the rock I'll check against
		// all of the other rocks below
		
		Rectangle rockToCheck = this.getBounds();
		
		// Cycle through all the other rocks and check if they
		// cross over the rectangle I created above
		
		for(Rock rock : rocks){
			
			// NEW Is rock viewable
			
			if(rock.onScreen){
						
			Rectangle otherRock = rock.getBounds();
			
			
			if(rock != this && otherRock.intersects(rockToCheck)){

				// Switch the direction the rocks are moving on impact
				
				int tempXDirection = this.xDirection;
				int tempYDirection = this.yDirection;
				
				this.xDirection = rock.xDirection;
				this.yDirection = rock.yDirection;
				
				rock.xDirection = tempXDirection;
				rock.yDirection = tempYDirection;
				
			}
			
			
				Rectangle shipBox = theShip.getBounds();
				
				if(otherRock.intersects(shipBox)){
					
					Board.playSoundEffect(explodeFile);
					
					theShip.setXCenter(theShip.gBWidth/2);
					theShip.setYCenter(theShip.gBHeight/2);
					
					theShip.setXVelocity(0);
					theShip.setYVelocity(0);
					
					
				}
				
				for(PhotonTorpedo torpedo : torpedos){
					
					// Make sure the Torpedo is on the screen
					
					if(torpedo.onScreen){
					
						// NEW Check if a torpedo hits a Rock
						
						if(otherRock.contains(torpedo.getXCenter(),torpedo.getYCenter())){
							
							rock.onScreen = false;
							torpedo.onScreen = false;
							
							System.out.println("HIT");
							
							// NEW play explosion sound if rock is destroyed
							
							Board.playSoundEffect(explodeFile);
						}
					
					}
					
				}
			
			}
			
		} 
		
		int uLeftXPos = super.xpoints[0];
		int uLeftYPos = super.ypoints[0];
		
		if(uLeftXPos<0||(uLeftXPos+25)>width) xDirection = -xDirection; 
		if(uLeftYPos<0||(uLeftYPos+50)>height) yDirection = -yDirection;
		
		
		for(int i=0;i<super.xpoints.length;i++) {
			super.xpoints[i]+=xDirection;
			super.ypoints[i]+=yDirection;
		}
		
	}
	
	
	public static int[] getPolyXArray(int randomStartXPos) {
		int[] tempPolyXArray = (int[])sPolyXArray.clone();
		
		for(int i=0;i<tempPolyXArray.length;i++) {
			tempPolyXArray[i]+=randomStartXPos;
		}
		
		return tempPolyXArray;
		
	}
	
	
	public static int[] getPolyYArray(int randomStartYPos) {
		int[] tempPolyYArray = (int[])sPolyYArray.clone();
		
		for(int i=0;i<tempPolyYArray.length;i++) {
			tempPolyYArray[i]+=randomStartYPos;
		}
		
		return tempPolyYArray;
		
	}
	
}
