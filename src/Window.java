package src;
import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class Window {

	private JFrame frame;
	private JPanel panel;
	
	private Maze maze;
	private int width;
	private int height;
	private final int windowSize = 800;
	private int cellSize;
	private int x;
	private int y;
	
	public Window(Maze maze)
	{
		initializeMaze(maze);
		
		frame = new JFrame("Maze Solver");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		frame.setSize(windowSize+cellSize/2, windowSize+2*cellSize);
		panel = new Panel();
	}
	
	public void render()
	{
		frame.repaint();
		
        frame.add(panel);
        frame.setVisible(true);
	}
	
	private class Panel extends JPanel{
		
		@Override
		public void paint(Graphics g){
			Graphics2D g2d = (Graphics2D) g;
			for(int i = 0; i < width; i++){
	        	for(int j = 0; j < height; j++){
	        		Cell c = maze.getMaze()[j][i];
	        		if(c == maze.getEntrance()){
	        			g2d.setColor(Color.GREEN);
	        			g2d.fillRect(x, y, cellSize, cellSize);
	        		}
	        		else if(c == maze.getExit()){
	        			g2d.setColor(Color.RED);
	        			g2d.fillRect(x, y, cellSize, cellSize);
	        		}
	        		if(c.hasReward()){
        				g2d.setColor(Color.ORANGE);
        				g2d.fillOval(x+cellSize/8, y+cellSize/8, 3*cellSize/4, 3*cellSize/4);
	        		}
	        		if(c.isOccupied())
	        		{
	        			if(c.getCreature() instanceof Agent){
	        				g2d.setColor(Color.YELLOW);
	        			}
	        			else if(c.getCreature() instanceof Monster){
	        				g2d.setColor(Color.BLUE);
	        			}
	 
	        			g2d.fillOval(x+cellSize/8, y+cellSize/8, 3*cellSize/4, 3*cellSize/4);
	        		
	        		}
	        		g2d.setColor(Color.BLACK);
	        		if(!c.hasNorthNeighbor())
	        			g2d.drawLine(x, y, x+cellSize, y);
	        		if(!c.hasWestNeighbor())
	        			g2d.drawLine(x, y, x, y+cellSize);
	        		if(!c.hasEastNeighbor())
	        			g2d.drawLine(x+cellSize, y, x+cellSize, y+cellSize);
	        		if(!c.hasSouthNeighbor())
	        			g2d.drawLine(x, y+cellSize, x+cellSize, y+cellSize);
	        		
	        		g2d.drawString(c.printCoords(), x, y);
	        		x += cellSize;
	        		

	        	}
	        	x = cellSize;
	        	y += cellSize;
	        }
			x = cellSize;
			y = cellSize;
		}
	}
	
	private void initializeMaze(Maze maze){
		this.maze = maze;
		width = maze.getMaze().length;
		height = maze.getMaze()[0].length;
		cellSize = windowSize/(Math.max(width, height)+2);
		x = cellSize;
		y = cellSize;
	}
}
