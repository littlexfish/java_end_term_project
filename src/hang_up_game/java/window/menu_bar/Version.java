package hang_up_game.java.window.menu_bar;

import hang_up_game.java.window.Constant;

import javax.swing.*;
import java.awt.*;

public class Version extends JDialog {
	
	public Version(JFrame owner) {
		super(owner, true);
		
		setTitle("關於");
		setBounds(Constant.getMiddleWindowRectangle(300, 150));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(5, 5));
		
		JLabel ver = new JLabel(Constant.detail);
		ver.setHorizontalAlignment(SwingConstants.CENTER);
		ver.setFont(new Font("Arial", Font.BOLD, 18));
		getContentPane().add(ver, BorderLayout.CENTER);
		
	}
	
}
