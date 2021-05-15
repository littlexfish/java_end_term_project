package hang_up_game.java.window.crafting;

import hang_up_game.java.game.Blueprint;
import hang_up_game.java.game.Item;
import hang_up_game.java.game.Mineral;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.storage.Machine;
import hang_up_game.java.io.data.storage.People;
import hang_up_game.java.window.WarningWindow;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class CraftPane extends JPanel {
	
	public CraftPane() {
		setLayout(new BorderLayout(5, 5));
		
		JLabel des = new JLabel();
		add(des, BorderLayout.EAST);
		
		JScrollPane scroll = new JScrollPane();
		add(scroll, BorderLayout.CENTER);
		
		JList<Blueprint> items = new JList<>(FileHolder.getBlueprintUnlock().toArray(new Blueprint[0]));
		items.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		items.addListSelectionListener(e -> {
			if(items.isSelectionEmpty()) return;
			Blueprint item = items.getSelectedValue();
			Set<Mineral> mSet =  item.craftRes.keySet();
			StringBuilder sb = new StringBuilder("<html>製作所需素材: <br>");
			for(Mineral m : mSet) {
				sb.append(m.chinese).append(": ").append(item.craftRes.get(m)).append("<br>");
			}
			sb.append("</html>");
			des.setText(sb.toString());
		});
		scroll.setViewportView(items);
		
		JButton fix = new JButton("製作");
		fix.setFocusable(false);
		fix.addActionListener(e -> {
			if(items.isSelectionEmpty()) return;
			Blueprint item = items.getSelectedValue();
			Set<Mineral> mSet =  item.craftRes.keySet();
			for(Mineral m : mSet) {
				if(FileHolder.mineral.getMineral(m) < item.craftRes.get(m)) {
					new WarningWindow("", "素材\"" + m.chinese + "\"不足", WarningWindow.DialogType_Time, JDialog.DISPOSE_ON_CLOSE, 3000).setVisible(true);
					return;
				}
			}
			for(Mineral m : mSet) {
				FileHolder.mineral.setMineral(m, FileHolder.mineral.getMineral(m) - item.craftRes.get(m));
			}
			Item i = Item.getItemFromId(item.id);
			if(item.id >= 0 && item.id <= 5) {
				FileHolder.people.addPickaxe(new People.PickaxeData(People.PickaxeData.getEmptyId(), i.name, i.getRandomExtra(), i.getRandomValue(), 0));
			}
			if(item.id >= 6 && item.id <= 11) {
				FileHolder.people.addBag(new People.BagData(People.BagData.getEmptyId(), i.name, i.getRandomValue()));
			}
			if(item.id >= 12 && item.id <= 17) {
				FileHolder.machine.addEngine(new Machine.Engine(Machine.Engine.getEmptyId(), i.name, i.getRandomExtra(), i.getRandomValue(), 0));
			}
			if(item.id >= 18 && item.id <= 23) {
				FileHolder.machine.addHead(new Machine.Head(Machine.Head.getEmptyId(), i.name, i.getRandomExtra(), i.getRandomValue(), 0));
			}
			if(item.id >= 24 && item.id <= 29) {
				FileHolder.machine.addBattery(new Machine.Battery(Machine.Battery.getEmptyId(), i.name, i.getRandomValue(), 0));
			}
			if(item.id >= 30 && item.id <= 35) {
				FileHolder.machine.addChest(new Machine.Chest(Machine.Chest.getEmptyId(), i.name, i.getRandomValue()));
			}
			des.setText("");
		});
		add(fix, BorderLayout.SOUTH);
		
	}

}
