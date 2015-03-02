package tankAttack;

import javax.swing.JFrame;

public class Window extends JFrame{
	
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
		
	}
}