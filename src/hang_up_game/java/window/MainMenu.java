package hang_up_game.java.window;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;

public class MainMenu extends JPanel {

	private final GameFrame frame;
	
	public MainMenu(GameFrame g) {
		frame = g;
		
		JPanel centerButton = new JPanel(new GridLayout(7, 1, 5, 5));
		centerButton.setPreferredSize(new Dimension(300, 200));
		add(centerButton, BorderLayout.CENTER);
		
		JLabel upPadding = new JLabel();
		centerButton.add(upPadding);
		
		JButton mining = new JButton("開始挖礦");
		mining.setFocusable(false);
		mining.addActionListener(e -> {
			frame.setPanel(new Mining(frame), "開始挖礦");
		});
		centerButton.add(mining);
		
		JLabel centerPadding1 = new JLabel();
		centerButton.add(centerPadding1);
		
		JButton shop = new JButton("商店");
		shop.setFocusable(false);
		shop.addActionListener(e -> {
		
		});
		centerButton.add(shop);
		
		JLabel centerPadding2 = new JLabel();
		centerButton.add(centerPadding2);
		
		JButton crafting = new JButton("製作");
		crafting.setFocusable(false);
		crafting.addActionListener(e -> {
		
		});
		centerButton.add(crafting);
		
	}
	
	
}
