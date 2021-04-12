package hang_up_game.java.window;

import javax.swing.*;

public class GameFrame extends JFrame {
	
	
	
	public void setPanel(JPanel p, String title, int width, int height) {
		setBounds(Constant.getMiddleWindowRectangle(width, height));
		setTitle(title);
		setContentPane(p);
	}

}
