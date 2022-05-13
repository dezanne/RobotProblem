package damjanrobot;

//represents the path of a robot beginning at origin and initially facing north
//and then moving to some final destination, the destination Robot only contains
//the final coordinates and the direction the Robot finally faces
public class RobotPath {
	
	//2 robots to represent the initial and final positions
	private Robot beginRobot;
	private Robot destRobot;
	
	//constructor
	public RobotPath(int x, int y, int xm, int ym) {
		
		beginRobot = new Robot(x, y, xm, ym);
		destRobot  = new Robot(x, y, xm, ym);
	}
	
	//copy constructor
	public RobotPath(RobotPath rp) {
		
		beginRobot = new Robot(rp.getBeginRobot());
		destRobot  = new Robot(rp.getDestRobot());
	}
	
	//get the robot at the initial position
	public Robot getBeginRobot() {
		
		return beginRobot;
	}
	
	//get the robot at the final position
	public Robot getDestRobot() {
		
		return destRobot;
	}
	
	//combines RobotPaths by setting the destination Robot of the initial path
	//to the destination Robot of the path to be appended
	public void combineRobotPath(RobotPath rp) {
		
		destRobot = new Robot(rp.getDestRobot());
	}
	
	//move the destination Robot 1 unit in its current direction or perform a rotation
	public void performAction(char robotAction) {
		
		if (robotAction == 'P') {
			destRobot.move();
		}
		
		else if (robotAction == 'L') {
			destRobot.rotateL();
		}
		
		else {
			destRobot.rotateR();
		}
	}
	

}
