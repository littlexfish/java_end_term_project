package hang_up_game.java.window;

import hang_up_game.java.window.crafting.Crafting;
import hang_up_game.java.window.shop.Buy;
import hang_up_game.java.window.shop.Shop;

import javax.swing.*;
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
			frame.setPanel(new Shop(frame), "商店");
		});
		centerButton.add(shop);
		
		JLabel centerPadding2 = new JLabel();
		centerButton.add(centerPadding2);
		
		JButton crafting = new JButton("工作坊");
		crafting.setFocusable(false);
		crafting.addActionListener(e -> {
			frame.setPanel(new Crafting(frame), "工作坊");
		});
		centerButton.add(crafting);
		
	}
	
	
}
