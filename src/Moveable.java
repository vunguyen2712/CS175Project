/*
 * CS 175 Group 1
 * 
 * Moveable is an abstract class that represents that the object can move throughout the maze
 * 
 * All children of this class must be able to figure out where it will move next, and it needs to know how 
 * 		to move
 * 
 * The two children of moveable are Agent.java and Monster.java
 */
public abstract class Moveable {
	
	public abstract void move();
	
	public abstract Cell calculateNextMove();
	
//	public abstract void calculateNextMove();
	
}
