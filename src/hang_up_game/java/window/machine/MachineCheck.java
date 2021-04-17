package hang_up_game.java.window.machine;

import hang_up_game.java.game.Background;
import hang_up_game.java.game.Direct;
import hang_up_game.java.game.MachineMiner;
import hang_up_game.java.game.Mineral;
import hang_up_game.java.io.data.storage.Item;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class MachineCheck extends JPanel {
	
	private final JFrame parent;
	private final MachineDetail detail;
	private final MachineMiner MM;
	
	public MachineCheck(JFrame window, MachineDetail detail, MachineMiner mm) {
		parent = window;
		this.detail = detail;
		MM = mm;
		
		
		
		//layout
		JPanel up = new JPanel(new GridLayout(2, 1, 5, 5));
		add(up, BorderLayout.CENTER);
		
		JPanel list = new JPanel(new GridLayout(1, 5, 5, 5));
		up.add(list);
		
		JLabel engine = new JLabel(MM.engine.list());
		list.add(engine);
		
		JLabel head = new JLabel(MM.head.list());
		list.add(head);
		
		JLabel battery = new JLabel(MM.machineBattery.list());
		list.add(battery);
		
		JLabel chest = new JLabel(MM.chest.list());
		list.add(chest);
		
		JLabel plugin = new JLabel(Arrays.toString(MM.plugins).replace("[", "<html>").replace("]", "</html>").replace(",", "<br>"));
		list.add(plugin);
		
		JPanel inChest = new JPanel();
		up.add(inChest);
		
		JLabel chestItems = new JLabel("<html>" + mineralToString(MM.getMineral()) + "," + itemsSet(MM.getItems()) + "</html>");
		inChest.add(chestItems);
		
		JPanel down = new JPanel(new GridLayout(1, 2, 5, 5));
		add(down, BorderLayout.SOUTH);
		
		JComboBox<Direct> dir = new JComboBox<>(Direct.values());
		dir.addItemListener(e -> {
			MM.setDirect((Direct)e.getItem());
		});
		down.add(dir);
		
		JButton returnMachine = new JButton("呼叫挖礦機返回");
		returnMachine.addActionListener(e -> {
			Background.returnMachineMiner(MM);
			detail.changeOnAir(Background.getAllMinerOnline());
		});
		down.add(returnMachine);
		
	}
	
	private String mineralToString(Map<Mineral, Integer> mineral) {
		StringBuilder str = new StringBuilder();
		Set<Mineral> ms = mineral.keySet();
		for(Mineral m : ms) {
			str.append(m.chinese).append(":").append(mineral.get(m)).append(",");
		}
		return str.substring(0, str.length() - 1);
	}
	
	private String itemsSet(Set<Item> items) {
		StringBuilder str = new StringBuilder();
		for(Item item : items) {
			str.append(item.name).append(",");
		}
		return str.substring(0, str.length() - 1);
	}
	
}
