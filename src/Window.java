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
	private final int cellSize = 50;
	private int x = cellSize;
	private int y = cellSize;
	
	public Window()
	{
		frame = new JFrame("Maze Solver");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		
		panel = new Panel();
	}
	
	public void render(Maze maze)
	{
		this.maze = maze;
		width = maze.getMaze().length;
		height = maze.getMaze()[0].length;
		frame.setBounds(0, 0, (width+2)*cellSize, (height+2)*cellSize);
		b = new BufferedImage((width+2)*cellSize, (height+2)*cellSize, BufferedImage.TYPE_INT_ARGB);
		g = b.createGraphics();
		g.setColor(Color.BLACK);
		
		
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
