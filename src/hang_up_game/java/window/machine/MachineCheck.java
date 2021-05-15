package hang_up_game.java.window.machine;

import hang_up_game.java.game.Background;
import hang_up_game.java.game.Direct;
import hang_up_game.java.game.MachineMiner;
import hang_up_game.java.game.Mineral;
import hang_up_game.java.io.data.storage.Item;
import hang_up_game.java.window.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class MachineCheck extends JPanel {
	
	private final GameFrame parent;
	private final MachineMiner MM;
	
	public MachineCheck(GameFrame window, MachineDetail detail, MachineMiner mm) {
		parent = window;
		MM = mm;
		
		setLayout(new BorderLayout(5, 5));
		
		//layout
		JPanel up = new JPanel(new GridLayout(2, 1, 5, 5));
		add(up, BorderLayout.CENTER);
		
		JPanel list = new JPanel(new GridLayout(1, 5, 5, 5));
		up.add(list);
		
		JLabel engine = new JLabel(toHtml(MM.engine.list()));
		list.add(engine);
		
		JLabel head = new JLabel(toHtml(MM.head.list()));
		list.add(head);
		
		JLabel battery = new JLabel(toHtml(MM.machineBattery.list()));
		list.add(battery);
		
		JLabel chest = new JLabel(toHtml(MM.chest.list()));
		list.add(chest);
		
		JLabel plugin = new JLabel(Arrays.toString(MM.plugins).replace("[", "<html>").replace("]", "</html>").replace(",", "<br>"));
		list.add(plugin);
		
		JPanel inChest = new JPanel();
		up.add(inChest);
		
		JLabel chestItems = new JLabel();
		String getM = mineralToString(MM.getMineral());
		String getI = itemsSet(MM.getItems());
		if(!("".equals(getM) && "".equals(getI))) {
			chestItems.setText("<html>" + getM + "," + getI + "</html>");
		}
		else {
			chestItems.setText("<html>" + getM + getI + "</html>");
		}
		inChest.add(chestItems);
		
		JPanel down = new JPanel(new GridLayout(1, 2, 5, 5));
		add(down, BorderLayout.SOUTH);
		
		JComboBox<Direct> dir = new JComboBox<>(Direct.values());
		dir.setSelectedItem(MM.getDirect());
		dir.addItemListener(e -> MM.setDirect((Direct)e.getItem()));
		down.add(dir);
		
		JButton returnMachine = new JButton("呼叫挖礦機返回");
		returnMachine.addActionListener(e -> {
			Background.returnMachineMiner(MM);
			detail.changeOnAir(Background.getAllMinerOnline());
			parent.goBack();
		});
		down.add(returnMachine);
		
	}
	
	private String mineralToString(Map<Mineral, Integer> mineral) {
		StringBuilder str = new StringBuilder();
		Set<Mineral> ms = mineral.keySet();
		if(ms.size() <= 0) {
			return "";
		}
		for(Mineral m : ms) {
			str.append(m.chinese).append(":").append(mineral.get(m)).append(",");
		}
		return str.substring(0, str.length() - 1);
	}
	
	private String itemsSet(Set<Item> items) {
		if(items.size() <= 0) {
			return "";
		}
		StringBuilder str = new StringBuilder();
		for(Item item : items) {
			str.append(item.name).append(",");
		}
		return str.substring(0, str.length() - 1);
	}
	
	public String toHtml(String old) {
		return "<html>".concat(old).replace("\n", "<br>").concat("</html>");
	}
	
}
