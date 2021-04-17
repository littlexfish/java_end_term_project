package hang_up_game.java.window.crafting;

import javax.swing.*;
import java.awt.*;

public class Crafting extends JPanel {
	
	public Crafting() {
		
		setLayout(new BorderLayout(5, 5));
		
		JTabbedPane tabPane = new JTabbedPane();
		add(tabPane, BorderLayout.CENTER);
		
		FixPanel fp = new FixPanel();
		tabPane.addTab("修理", null, fp, "這裡可以修理壞掉的裝備");
		
		CraftPane cp = new CraftPane();
		tabPane.addTab("製作", null, cp, "這裡可以製作用藍圖解鎖的工具");
		
	}
	
}
