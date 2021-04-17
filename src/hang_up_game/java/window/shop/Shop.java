package hang_up_game.java.window.shop;

import hang_up_game.java.window.GameFrame;

import javax.swing.*;
import java.awt.*;

public class Shop extends JPanel {
	
	private GameFrame frame;
	
	public Shop(GameFrame gf) {
		
		frame = gf;
		
		setLayout(new BorderLayout(5, 5));
		
		JTabbedPane tabPanel = new JTabbedPane();
		add(tabPanel);
		
		ItemPane ip = new ItemPane(frame);
		tabPanel.addTab("物品", null, ip, "這裡會販售已解鎖的工具");
		
		BlueprintPane bp = new BlueprintPane(frame);
		tabPanel.addTab("藍圖", null, bp, "這裡會販售可解鎖的藍圖");
		
	}

}
