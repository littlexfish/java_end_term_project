package hang_up_game.java.window.shop;

import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.storage.Item;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ItemSell extends JPanel {
	
	private final JLabel itemLabel;
	private final JFrame frame;
	
	public ItemSell(JFrame f) {
		frame = f;
		
		setLayout(new BorderLayout(5, 5));
		
		ArrayList<Item> allItems = FileHolder.getAllStorage();
		
		JScrollPane scroll = new JScrollPane();
		add(scroll, BorderLayout.CENTER);
		
		JList<Item> itemList = new JList<>(allItems.toArray(new Item[0]));
		itemList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scroll.setViewportView(itemList);
		
		itemLabel = new JLabel("剩餘金額: $" + FileHolder.shop.getMoney());
		add(itemLabel, BorderLayout.NORTH);
		
		JButton sell = new JButton("販賣 $0");
		sell.addActionListener(e -> {
			if(itemList.isSelectionEmpty()) return;
			List<Item> selects = itemList.getSelectedValuesList();
			hang_up_game.java.game.Item[] items = new hang_up_game.java.game.Item[selects.size()];
			for(int i = 0;i < selects.size();i++) {
				items[i] = hang_up_game.java.game.Item.getItemFromName(selects.get(i).name);
			}
			int price = 0;
			for(hang_up_game.java.game.Item i : items) {
				price += i.price;
			}
			boolean isSell = ConfirmSell.waitAndGetStatus(frame, price);
			if(isSell) {
				FileHolder.shop.addMoney(price);
				for(Item i : selects) {
					decodeItem(i);
					refreshList(itemList);
				}
			}
		});
		add(sell, BorderLayout.SOUTH);
		
		itemList.addListSelectionListener(e -> {
			if(itemList.isSelectionEmpty()) return;
			List<Item> selects = itemList.getSelectedValuesList();
			hang_up_game.java.game.Item[] items = new hang_up_game.java.game.Item[selects.size()];
			for(int i = 0;i < selects.size();i++) {
				items[i] = hang_up_game.java.game.Item.getItemFromName(selects.get(i).name);
			}
			int price = 0;
			for(hang_up_game.java.game.Item i : items) {
				price += i.price;
			}
			sell.setText("販賣 $" + price);
		});
		
	}
	
	private void decodeItem(Item i) {
		if(i.name.endsWith("稿子")) {
			FileHolder.people.removePickaxe(i.id);
		}
		else if(i.name.endsWith("包包")) {
			FileHolder.people.removeBag(i.id);
		}
		else if(i.name.endsWith("引擎")) {
			FileHolder.machine.removeEngine(i.id);
		}
		else if(i.name.endsWith("鑽頭")) {
			FileHolder.machine.removeHead(i.id);
		}
		else if(i.name.endsWith("電池")) {
			FileHolder.machine.removeBattery(i.id);
		}
		else { //箱子
			FileHolder.machine.removeChest(i.id);
		}
	}
	private void refreshList(JList<Item> itemList) {
		ArrayList<Item> allItems = FileHolder.getAllStorage();
		itemList.setListData(allItems.toArray(new Item[0]));
		itemLabel.setText("剩餘金額: $" + FileHolder.shop.getMoney());
	}
	
}
