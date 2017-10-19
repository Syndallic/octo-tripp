package tankAttack;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends JFrame{

	static int WIDTH = 1200;
	static int HEIGHT = 800;
	static String title = "Tank Attack";
	
	public Window(){
		super(title);
		setSize(WIDTH, HEIGHT);
		add(new TankAttack(this, title));
		requestFocus();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args){
		Window w = new Window();
		w.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
		w.pack();
	}
}
