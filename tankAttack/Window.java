package tankAttack;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends JFrame{

	private static final long serialVersionUID = 1L;
	
	int WIDTH = 1200, HEIGHT = 800;
	String title = "Tank Attack";
	int MAIN_MENU = 0;
	
	public Window(){
		super("Tank Game");
		setSize(WIDTH, HEIGHT);
		add(new TankAttack(this, title));
		requestFocus();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args){
		Window w = new Window();
		w.getContentPane().setPreferredSize(new Dimension(1200, 800));
		w.pack();
	}
}
