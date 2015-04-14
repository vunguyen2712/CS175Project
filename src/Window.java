import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

import javax.swing.*;

public class Window {

	private JFrame frame;
	private BufferedImage b;
	private Graphics2D g;
	private JPanel panel;
	
	public Window()
	{
		frame = new JFrame();
		frame.setBounds(0, 0, 700, 700);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		b = new BufferedImage(700, 700, BufferedImage.TYPE_INT_ARGB);
		g = b.createGraphics();
		g.setColor(Color.BLACK);
		panel = new JPanel();
		
	}
	
	public void render(Maze maze)
	{
		int x = 50;
		int y = 50;
		int width = maze.getMaze().length;
		int height = maze.getMaze()[0].length;
        
        g.drawLine(x, y, x+50*width, y);
        g.drawLine(x, y, x, y+50*height);
        g.drawLine(x+50*width, y, x+50*width, y+50*height);
        g.drawLine(x, y+50*height, x+50*width, y+50*height);
        for(int i = 0; i < width; i++){
        	for(int j = 0; j < height; j++){
        		ArrayList<Cell> neighbors = maze.getMaze()[i][j].getNeighbors();
        		for(Cell n : neighbors){
        			//if(neighbors.)
        		}
        		
        		
        	}
        }
        panel.paint(g);
        frame.add(panel);
        frame.setVisible(true);
	}
	
}
