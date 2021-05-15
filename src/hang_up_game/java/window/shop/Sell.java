package hang_up_game.java.window.shop;

import javax.swing.*;
import java.awt.*;

public class Sell extends JPanel {
	
	public Sell(JFrame gf) {
		
		setLayout(new BorderLayout(5, 5));
		
		JTabbedPane tabPanel = new JTabbedPane();
		add(tabPanel);
		
		ItemSell is = new ItemSell(gf);
		tabPanel.addTab("物品", null, is, "這裡可以賣出獲得的工具");
		
		MaterialSell ms = new MaterialSell(gf);
		tabPanel.addTab("材料", null, ms, "這裡可以賣出挖到的材料");
		
	}
	
}
