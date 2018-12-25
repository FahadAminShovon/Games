import java.awt.Polygon;
import java.awt.Rectangle;

public class PhotonTorpedo extends Polygon{
	
	int gBWidth = Board.boardWidth;
	int gBHeight = Board.boardHeight;
	
	private double centerX = 0, centerY = 0;
	
	
	public static int[] polyXArray = {-3,3,3,-3,-3};
	public static int[] polyYArray = {-3,-3,3,3,-3};
	
	private int torpedoWidth = 10, torpedoHeight = 6;
	
	public boolean onScreen = false;
	
	private double movingAngle = 0;
	
	private double xVelocity = 5, yVelocity = 5;

	public PhotonTorpedo(double centerX, double centerY, double movingAngle){
		
		// Creates a Polygon by calling the super or parent class Polygon
		
		super(polyXArray, polyYArray, 5);
		
		// Defines the center based on the vectors of
		// the ships nose. movingAngle is the same as ship
		
		this.centerX = centerX;
		this.centerY = centerY;
		this.movingAngle = movingAngle;
		
		// Tells program to show the ship
		
		this.onScreen = true;
		
		// Sets how quickly the torpedo moves along the path defined
		// by setXVelocity and setYVelocity
		
		
		this.setXVelocity(this.torpedoXMoveAngle(movingAngle)*10);
		this.setYVelocity(this.torpedoYMoveAngle(movingAngle)*10);
			
	}
	
	public double getXCenter() {
		return centerX;
	}
	
	public double getYCenter() {
		return centerY;
	}
	
	public void setXCenter(double cenx) {
		this.centerX = cenx;
	}
	
	public void setYCenter(double ceny) {
		this.centerY = ceny;
	}
	
	
	public void changeXPos(double incAmt) {
		this.centerX+=incAmt;
	}
	public void changeYPos(double incAmt) {
		this.centerY+=incAmt;
	}
	
	public double getXVelocity() {
		return xVelocity;
	}
	
	public double getYVelocity() {
		return yVelocity;
	}
	
	public void setXVelocity(double xVel) {
		this.xVelocity= xVel;
	}
	public void setYVelocity(double yVel) {
		this.yVelocity= yVel;
	}
	
	public int getWidth() {
		return torpedoWidth;
	}
	
	public int getHeight() {
		return torpedoHeight;
	}
	
	public void setMovingAngle(double moveAngle) {
		this.movingAngle=moveAngle;
	}
	
	public double getMovingAngel() {
		return movingAngle;
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)getXCenter()-10,(int)getYCenter()-10,getWidth(),getHeight());
	}
	
	
	public double torpedoXMoveAngle(double xMoveAngle){
		
		return (double) (Math.cos(xMoveAngle * Math.PI / 180));
			
	}
		
	// By taking the sin of the angle I get the opposite value of y
	// Angle * Math.PI / 180 converts degrees into radians 
		
	public double torpedoYMoveAngle(double yMoveAngle){
			
		return (double) (Math.sin(yMoveAngle * Math.PI / 180));
			
	}
	
	public void move() {
		if(this.onScreen) {
			this.changeXPos(this.getXVelocity());
			if(this.getXCenter()<0||this.getXCenter()>gBWidth) {
				this.onScreen=false;
			}
			this.changeYPos(this.getYVelocity());
			if(this.getYCenter()<0||this.getYCenter()>gBHeight) {
				this.onScreen=false;
			}
		}
	}
			
			
	
}
