package hang_up_game.java.window.shop;

import javax.swing.*;
import java.awt.*;

public class Buy extends JPanel {
	
	public Buy(JFrame gf) {
		
		setLayout(new BorderLayout(5, 5));
		
		JTabbedPane tabPanel = new JTabbedPane();
		add(tabPanel);
		
		ItemPane ip = new ItemPane(gf);
		tabPanel.addTab("物品", null, ip, "這裡會販售已解鎖的工具");
		
		BlueprintPane bp = new BlueprintPane(gf);
		tabPanel.addTab("藍圖", null, bp, "這裡會販售可解鎖的藍圖");
		
	}

}
