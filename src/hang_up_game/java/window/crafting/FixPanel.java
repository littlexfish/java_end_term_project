package hang_up_game.java.window.crafting;

import hang_up_game.java.game.Blueprint;
import hang_up_game.java.game.Blueprints;
import hang_up_game.java.game.Mineral;
import hang_up_game.java.io.Log;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.storage.Item;
import hang_up_game.java.io.data.storage.Machine;
import hang_up_game.java.io.data.storage.People;
import hang_up_game.java.window.WarningWindow;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class FixPanel extends JPanel {
	
	public FixPanel() {
		Log.d("fix panel", "init");
		setLayout(new BorderLayout(5, 5));
		
		JLabel des = new JLabel();
		add(des, BorderLayout.EAST);
		
		JScrollPane scroll = new JScrollPane();
		add(scroll, BorderLayout.CENTER);
		
		JList<Item> items = new JList<>(FileHolder.getNeedFix().toArray(new Item[0]));
		items.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		items.addListSelectionListener(e -> {
			if(items.isSelectionEmpty()) return;
			Item item = items.getSelectedValue();
			Blueprint b = Blueprints.findBluePrintFromName(item.name);
			float percent = 1f;
			if(item instanceof People.PickaxeData) {
				percent = (float)((People.PickaxeData)item).damage / ((People.PickaxeData)item).maxDamage;
			}
			if(item instanceof Machine.Engine) {
				percent = (float)((Machine.Engine)item).getDamage() / ((Machine.Engine)item).maxDamage;
			}
			if(item instanceof Machine.Head) {
				percent = (float)((Machine.Head)item).getDamage() / ((Machine.Head)item).maxDamage;
			}
			assert b != null;
			Set<Mineral> mSet =  b.craftRes.keySet();
			StringBuilder sb = new StringBuilder("<html>修復所需素材: <br>");
			for(Mineral m : mSet) {
				sb.append(m.chinese).append(": ").append((int)(b.craftRes.get(m) / 2.0 * percent) + 1).append("<br>");
			}
			sb.append("</html>");
			des.setText(sb.toString());
		});
		scroll.setViewportView(items);
		
		JButton fix = new JButton("修理");
		fix.setFocusable(false);
		fix.addActionListener(e -> {
			if(items.isSelectionEmpty()) return;
			Item item = items.getSelectedValue();
			Blueprint b = Blueprints.findBluePrintFromName(item.name);
			float percent = 1f;
			if(item instanceof People.PickaxeData) {
				percent = (float)((People.PickaxeData)item).damage / ((People.PickaxeData)item).maxDamage;
			}
			if(item instanceof Machine.Engine) {
				percent = (float)((Machine.Engine)item).getDamage() / ((Machine.Engine)item).maxDamage;
			}
			if(item instanceof Machine.Head) {
				percent = (float)((Machine.Head)item).getDamage() / ((Machine.Head)item).maxDamage;
			}
			assert b != null;
			Set<Mineral> mSet =  b.craftRes.keySet();
			for(Mineral m : mSet) {
				int need = (int)(b.craftRes.get(m) / 2.0 * percent) + 1;
				if(FileHolder.mineral.getMineral(m) < need) {
					new WarningWindow("", "素材\"" + m.chinese + "\"不足", WarningWindow.DialogType_Time, JDialog.DISPOSE_ON_CLOSE, 3000).setVisible(true);
					return;
				}
			}
			for(Mineral m : mSet) {
				int need = (int)(b.craftRes.get(m) / 2.0 * percent) + 1;
				FileHolder.mineral.setMineral(m, FileHolder.mineral.getMineral(m) - need);
			}
			if(item instanceof People.PickaxeData) {
				FileHolder.people.setPickaxe(People.PickaxeData.quickData((People.PickaxeData)item, 0));
			}
			if(item instanceof Machine.Engine) {
				FileHolder.machine.setEngine(Machine.Engine.quickData((Machine.Engine)item, 0));
			}
			if(item instanceof Machine.Head) {
				FileHolder.machine.setHead(Machine.Head.quickData((Machine.Head)item, 0));
			}
			items.setListData(FileHolder.getNeedFix().toArray(new Item[0]));
			des.setText("");
		});
		add(fix, BorderLayout.SOUTH);
		
	}
	
}
