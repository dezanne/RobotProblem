package damjanrobot;

//Represents a Robot that can rotate and move 1 unit
public class Robot {
	
	//current position of Robot
	private int xpos;
	private int ypos;
	
	//move vector
	private int xmove;
	private int ymove;
	
	//constructor
	public Robot(int x, int y, int xm, int ym) {
		
		xpos = x;
		ypos = y;
		
		xmove = xm;
		ymove = ym;
	}
	
	//copy constructor
	public Robot(Robot r) {
		
		xpos = r.getX();
		ypos = r.getY();
		
		xmove = r.getXDir();
		ymove = r.getYDir();
	}
	
	//apply move vector
	public void move() {
		
		xpos += xmove;
		ypos += ymove;
	}
	
	//rotate 90 degrees to the right
	public void rotateR() {
		
		int tempx = xmove;
		xmove = ymove;
		ymove = (-1)*tempx;
	}
	
	//rotate 90 degrees to the left
	public void rotateL() {
		
		int tempx = xmove;
		xmove = (-1)*ymove;
		ymove = tempx;
	}
	
	//reset Robot to default settings
	public void def() {
		
		xpos = 0;
		ypos = 0;
		
		xmove = 0;
		ymove = 1;
	}
	
	//return x coordinate of current Robot position
	public int getX() {
		
		return xpos;
	}
	
	//return y coordinate of current Robot position
	public int getY() {
		
		return ypos;
	}
	
	//return x component of the Robots current rotation vector
	public int getXDir() {
		
		return xmove;
	}
	
	//return y component of the Robots current rotation vector
	public int getYDir() {
		
		return ymove;
	}
}
