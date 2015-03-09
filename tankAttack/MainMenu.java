package tankAttack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MainMenu extends Screen {

	JButton a;
	
	
	public MainMenu(Game g, Graphics2D g2d) {
		super(g, g2d); 
		a = new JButton("Button1");
		initiate();
	}

	public void update() {
		
		a.setSize(200, 200);
		a.setActionCommand("Versus");
		a.addActionListener(new Button());
//		add(a);
		
		g2d.setFont(new Font("Verdana", Font.BOLD, 36));
		g2d.setColor(Color.BLACK);
		printSimpleString("TANK ATTACK", SCREENWIDTH, -2,
				SCREENHEIGHT / 3 - 2);
		g2d.setColor(new Color(200, 30, 30));
		printSimpleString("TANK ATTACK", SCREENWIDTH, 0, SCREENHEIGHT / 3);

		int x = SCREENWIDTH / 4, y = SCREENHEIGHT / 2;
		g2d.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 30));
		g2d.setColor(Color.GRAY);
		printSimpleString("CONTROLS:", SCREENWIDTH, 0, y);
		g2d.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 20));
		printSimpleString("END GAME - Escape", SCREENWIDTH, 0, y + 100);
		g2d.setColor(Color.RED);
		printSimpleString("ROTATE - Left/Right Arrows", SCREENWIDTH, x,
				y += 40);
		printSimpleString("FORWARDS - Up Arrow", SCREENWIDTH, x, y += 30);
		printSimpleString("REVERSE - Down Arrow", SCREENWIDTH, x, y += 30);
		printSimpleString("FIRE - Enter", SCREENWIDTH, x, y += 30);

		y = SCREENHEIGHT / 2;
		g2d.setColor(Color.BLUE);
		printSimpleString("ROTATE - A/D", SCREENWIDTH, -x, y += 40);
		printSimpleString("FORWARDS - W", SCREENWIDTH, -x, y += 30);
		printSimpleString("REVERSE - S", SCREENWIDTH, -x, y += 30);
		printSimpleString("FIRE - Control", SCREENWIDTH, -x, y += 30);
		printSimpleString("Activate AI - X", SCREENWIDTH, -x, y += 30);

		g2d.setFont(new Font("Ariel", Font.BOLD, 24));
		g2d.setColor(Color.WHITE);
		printSimpleString("Press SPACE to start", SCREENWIDTH, 0,
				(int) (0.8 * SCREENHEIGHT));


	}

	private class Button implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Versus")){
				setState(GAME_RUNNING);
			}
		}

	}
}