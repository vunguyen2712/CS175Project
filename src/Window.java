import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

import javax.swing.*;

public class Window {

	private JFrame frame;
	private BufferedImage b;
	private Graphics2D g;
	private JPanel panel;
	
	private Maze maze;
	private int width;
	private int height;
	
	private final int windowSize = 800;
	private int cellSize;
	private int x;
	private int y;
	
	private boolean inbetween;
	
	public Window(Maze maze)
	{
		this.maze = maze;
		width = maze.getMaze().length;
		height = maze.getMaze()[0].length;
		cellSize = windowSize/(Math.max(width, height)+2);
		x = cellSize;
		y = cellSize;
		
		frame = new JFrame("Maze Solver");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		frame.setSize(windowSize+cellSize/2, windowSize+2*cellSize);
		b = new BufferedImage(windowSize+cellSize/2, windowSize+2*cellSize, BufferedImage.TYPE_INT_ARGB);
		
		panel = new Panel();
	}
	
	public void render(boolean inbetween)
	{
		this.inbetween = inbetween;
		g = b.createGraphics();
		g.setColor(Color.BLACK);
		
		
        frame.add(panel);
        frame.setVisible(true);
	}
	
	private class Panel extends JPanel{
		
		@Override
		public void paint(Graphics g){
			
			Graphics2D g2d = (Graphics2D) g;
			for(int i = 0; i < height; i++){
	        	for(int j = 0; j < width; j++){
	        		Cell c = maze.getMaze()[j][i];
	        		
	        		if(c == maze.getEntrance()){
	        			g2d.setColor(Color.GREEN);
	        			g2d.fillRect(x, y, cellSize, cellSize);
	        		}
	        		else if(c == maze.getExit()){
	        			g2d.setColor(Color.RED);
	        			g2d.fillRect(x, y, cellSize, cellSize);
	        		}
	        		if(c.isOccupied()){//brown if monster, orange if agent 
	        			if(c.getCreature() instanceof Agent){
	        				g2d.setColor(Color.ORANGE);
	        			}
	        			else{
	        				g2d.setColor(new Color(0x7E6335));
	        			}
	        			g2d.fillOval(x+cellSize/8, y+cellSize/8, 3*cellSize/4, 3*cellSize/4);
	        		}
	        		if(c.isImportant() == 3){//yellow circle outline if reward
	        			g2d.setColor(Color.YELLOW);
	        			g2d.drawOval(x, y, cellSize, cellSize);
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
	        		x += cellSize;
	        	}
	        	x = cellSize;
	        	y += cellSize;
	        }
			x = cellSize;
			y = cellSize;
		}
	}
	
}
