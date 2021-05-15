package hang_up_game.java.window.shop;

import hang_up_game.java.window.GameFrame;

import javax.swing.*;
import java.awt.*;

public class Shop extends JPanel {
	
	
	private final GameFrame frame;
	
	public Shop(GameFrame f) {
		frame = f;
		
		JPanel centerButton = new JPanel(new GridLayout(8, 1, 5, 5));
		centerButton.setPreferredSize(new Dimension(400, 300));
		add(centerButton, BorderLayout.CENTER);
		
		JLabel upPadding = new JLabel();
		centerButton.add(upPadding);
		
		JButton buy = new JButton("購買");
		buy.setFocusable(false);
		buy.addActionListener(e -> frame.setPanel(new Buy(frame), "購買"));
		centerButton.add(buy);
		
		JLabel centerPadding1 = new JLabel();
		centerButton.add(centerPadding1);
		
		JButton sell = new JButton("販賣");
		sell.setFocusable(false);
		sell.addActionListener(e -> frame.setPanel(new Sell(frame), "販賣"));
		centerButton.add(sell);
		
	}
	
	
}
