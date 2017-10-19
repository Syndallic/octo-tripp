package screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import tankAttack.TankAttack;

public class ControlsMenu extends Screen {

	private Font f1, f2;
	
	public ControlsMenu(TankAttack g, Graphics2D g2d) {
		super(g, g2d);
	}

	public void initiate(){
		f1 = new Font("Times New Roman", Font.ITALIC | Font.BOLD, 40);
		f2 = new Font("Times New Roman", Font.ITALIC | Font.BOLD, 30);
		
	}
	
	public void update(){
		int x = SCREENWIDTH / 4, y = SCREENHEIGHT / 4;

		g2d.setFont(f1);
		g2d.setColor(Color.GRAY);
		printSimpleString("CONTROLS:", SCREENWIDTH, 0, y);

		g2d.setFont(f2);
		printSimpleString("EXIT - Escape", SCREENWIDTH, 0, y + 300);
		g2d.setColor(Color.RED);
		printSimpleString("ROTATE - Left/Right Arrows", SCREENWIDTH, x, y += 50);
		printSimpleString("FORWARDS - Up Arrow", SCREENWIDTH, x, y += 40);
		printSimpleString("REVERSE - Down Arrow", SCREENWIDTH, x, y += 40);
		printSimpleString("FIRE - Enter", SCREENWIDTH, x, y += 40);

		y = SCREENHEIGHT / 4;
		g2d.setColor(Color.BLUE);
		printSimpleString("ROTATE - A/D", SCREENWIDTH, -x, y += 50);
		printSimpleString("FORWARDS - W", SCREENWIDTH, -x, y += 40);
		printSimpleString("REVERSE - S", SCREENWIDTH, -x, y += 40);
		printSimpleString("FIRE - Control", SCREENWIDTH, -x, y += 40);
		printSimpleString("Activate AI - X", SCREENWIDTH, -x, y += 40);
	}
	
	public void keyPressed(int keyCode){
		if(keyCode == KeyEvent.VK_ESCAPE){
			g.main.makeCurrent();
		}
	}
}
