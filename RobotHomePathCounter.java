package damjanrobot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.Scanner;
import java.util.LinkedList;

public class RobotHomePathCounter {
	
	//variable to store total number of to-origin cycles
	private static int totalToOriginCycles = 0;
	
	// (x, y) = (p[0], p[1], perform an n number of 90 degree rotations to the left to the vector (x, y)
	private static void performN2DRotations(int[] p, int n) {
		
		int i = 0;
		
		while (i < n) {
			
			int temp = p[0];
			
			p[0] = (-1)*p[1];
			p[1] = temp;
			
			i++;
		}
	}
	
	//given a rotation vector (x, y) return how many 90 degree rotations to are needed from (0,1) to
	//get to (x, y)
	private static int numLeftRotsFromUp(int x, int y) {
		
		int result;
		
		if (x == -1 && y == 0) {
			result = 1;
		}
		else if (x == 0 && y == -1) {
			result = 2;
		}
		else if (x == 1 && y == 0) {
			result = 3;
		}
		else {
			result = 0;
		}
		
		return result;
	}
	
	//returns true if the order of RobotPaths appended together results in a cycle back to origin
	private static boolean returnsToOrigin(LinkedList<RobotPath> rpl) {
		
		LinkedList<RobotPath> temp = new LinkedList<RobotPath>(rpl);
		boolean result = false;
		
		RobotPath path = temp.removeFirst();
		
		int[] destCoord = {path.getDestRobot().getX(), path.getDestRobot().getY()};
		int[] curLRot = {path.getDestRobot().getXDir(), path.getDestRobot().getYDir()};
		
		int lrots = numLeftRotsFromUp(curLRot[0], curLRot[1]);
		
		while (temp.size() > 0) {
			
			int[] nextCoord = {temp.peekFirst().getDestRobot().getX(), 
					           temp.peekFirst().getDestRobot().getY()};
			
			performN2DRotations(nextCoord, lrots);
			
			destCoord[0] += nextCoord[0];
			destCoord[1] += nextCoord[1];
			
			lrots += numLeftRotsFromUp(temp.peekFirst().getDestRobot().getXDir(), 
									   temp.peekFirst().getDestRobot().getYDir());
			
			temp.removeFirst();
			
		}
		
		if (destCoord[0] == 0 && destCoord[1] == 0) {
			result = true;
		}
		
		return result;
	}
	
	//swap the values in RobotPath list rpl at indices i and j
	private static void swapAt(LinkedList<RobotPath> rpl, int i, int j) {
		
		RobotPath temp, tempr;
		
		temp = rpl.get(i);
		tempr = rpl.get(j);
		
		rpl.remove(i);
		rpl.add(i,tempr);
		
		rpl.remove(j);
		rpl.add(j,temp);
	}
	
	//goes through all permutations of a given RobotPath list
	private static void goThroughPerms(LinkedList<RobotPath> rpl, int i, int j) {
		
		if (i != j) {
			for(int k = i; k <= j; k++) {
				swapAt(rpl, i, k);
				goThroughPerms(rpl, i+1, j);
				swapAt(rpl, i, k);
			}
		}
		
		else {
			if (returnsToOrigin(rpl)) { //if there is valid cycle to origin increment total
				totalToOriginCycles++;
			}
		}
	}
	
	//generates all permutations of the RobotPath list and outputs how many of them correspond to
	//a cycle back to the origin
	private static void permuteOriginCount(LinkedList<RobotPath> rpl) {
		
		LinkedList<RobotPath> rplcp = new LinkedList<RobotPath>(rpl);
		
		//if there is only 1 RobotPath in the list and it is at origin return 1
		if (rpl.size() == 1) {
			if (rpl.peek().getDestRobot().getX() == 0 &&
				rpl.peek().getDestRobot().getY() == 0) {
				totalToOriginCycles++;
			}
		}
		
		else {
			
			int i = 0;
			int j = rplcp.size() - 1;
			
			goThroughPerms(rplcp, i, j);
			
		}
	}
	
	//recursively iterate through the string of character commands and generate the
	//appropriate RobotPath accordingly
	private static void recurseGeneratePath(RobotPath path, String commands, int numCommands) {
		
		//numCommands can be either 2 or 1
		if (numCommands <= 2) {
			
			int i = 0;
			
			Robot dest = path.getDestRobot();
			
			//create new temporary path starting from the destination of the path
			RobotPath tempPath = new RobotPath(dest.getX(), dest.getY(), 
											dest.getXDir(), dest.getYDir());
			
			//perform the commands
			while (i < numCommands) {
				tempPath.performAction(commands.charAt(i));
				i++;
			}
			
			//combine the path with the temporary one
			path.combineRobotPath(tempPath);
		}
		
		else { //recurse
			
			int leftSize = (int)Math.floor(numCommands/2);
			
			recurseGeneratePath(path, commands.substring(0, leftSize), leftSize);
			recurseGeneratePath(path, commands.substring(leftSize, numCommands), 
														 numCommands - leftSize);
		
		}
	}
	
	//main thread of execution
	public static void main(String[] args) {
		
		String inputFilename = args[0];
		String delimiter = ".";
		
		String[] outSplit = inputFilename.split("[.]");
		outSplit[2] = "out";
		
		String outputFilename = "";
		
		for (String s : outSplit) {
			outputFilename += s + delimiter;
		}
		
		outputFilename = outputFilename.substring(0, outputFilename.length() - 1);
				
		try {
			
			File robotDataFile = new File(inputFilename);
			@SuppressWarnings("resource")
			Scanner robotDataReader = new Scanner(robotDataFile);
			
			byte k;
			int n;
			String actionData;
			
			//get metadata from file
			String metaData = robotDataReader.nextLine();
			k = (byte)Integer.parseInt(metaData.split(" ")[0]);
			n = Integer.parseInt(metaData.split(" ")[1]);
			byte i = 0;
			
			//create list of RobotPaths 
			LinkedList<RobotPath> robotPathList = new LinkedList<RobotPath>();
			RobotPath path = new RobotPath(0,0,0,1);
			
			while (i < k) {
				
				//get string of character instructions
				actionData = robotDataReader.nextLine();
				
				//create RobotPath from string of character instructions
				recurseGeneratePath(path, actionData, n);
				
				//copy the path and add it to the list
				RobotPath pathCopy = new RobotPath(path);
				robotPathList.add(pathCopy);
				
				//reset original path
				path.getBeginRobot().def();
				path.getDestRobot().def();
				
				i++;

			}
			
			permuteOriginCount(robotPathList);
			
			//write to output file
			File out = new File(outputFilename);
			out.createNewFile();
			
			FileWriter outputWriter = new FileWriter(out);
			outputWriter.write(Integer.toString(totalToOriginCycles));
			outputWriter.close();
			
			
		} catch (FileNotFoundException e) { //if file not found
			System.out.println("File with name " + inputFilename + " could not be found.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
