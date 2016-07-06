package gameEngine;

import math.geom2d.Point2D;
import screens.Screen;
import tankAttack.TankAttack;
import tankAttack.Wall;

public class Map {
	
	Point2D topleft, topright, bottomright, bottomleft;
	int width,height;
	int offset = 20;
	int topoffset = 100;
	
	public Map(TankAttack g, Screen screen){
		width = g.getWidth();
		height = g.getHeight();
		topleft = new Point2D(offset, topoffset + offset);
		topright = new Point2D(width - offset, topoffset + offset);
		bottomright = new Point2D(width - offset, height - offset);
		bottomleft = new Point2D(offset, height-offset);
		
		screen.add(new Wall(g, g.graphics(),topleft, topright));
		screen.add(new Wall(g, g.graphics(),topright, bottomright));
		screen.add(new Wall(g, g.graphics(),bottomright, bottomleft));
		screen.add(new Wall(g, g.graphics(),bottomleft, topleft));
	}
	
}
