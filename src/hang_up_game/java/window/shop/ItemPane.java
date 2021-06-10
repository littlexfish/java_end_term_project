package hang_up_game.java.window.shop;

import hang_up_game.java.game.Item;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.storage.Machine;
import hang_up_game.java.io.data.storage.People;
import hang_up_game.java.window.WarningWindow;
import org.lf.logger.Log;

import javax.swing.*;
import java.awt.*;

public class ItemPane extends JPanel {
	
	private final JFrame frame;
	int[] ids = new int[9];
	
	public ItemPane(JFrame owner) {
		Log.d("itemPane", "init");
		frame = owner;
		
		setLayout(new BorderLayout(5, 5));
		
		JLabel moneyT = new JLabel("剩餘金額: $" + FileHolder.shop.getMoney());
		add(moneyT, BorderLayout.NORTH);
		
		JPanel items = new JPanel(new GridLayout(3, 3, 5, 5));
		add(items, BorderLayout.CENTER);
		
		ItemHolder[] item = new ItemHolder[9];
		for(int i = 0;i < 9;i++) {
			int finalI = i;
			int id = Item.getRandomItemFromUnlock();
			item[i] = new ItemHolder(Item.getItemFromId(id));
			ids[i] = id;
			item[i].setFocusable(false);
			item[i].addActionListener(e -> {
				int money = FileHolder.shop.getMoney();
				int price = item[finalI].getItem().getBuyPrice();
				if(money < price) {
					new WarningWindow("", "金額不足", WarningWindow.DialogType_Time, JDialog.DISPOSE_ON_CLOSE, 3000).setVisible(true);
					return;
				}
				boolean isBuy = ConfirmDialog.waitAndGetStatus(frame, price);
				if(isBuy) {
					FileHolder.shop.addMoney(-price);
					decodeItem(item[finalI].getItem());
					item[finalI].setEnabled(false);
					moneyT.setText("剩餘金額: $" + FileHolder.shop.getMoney());
				}
			});
			items.add(item[i]);
		}
		
	}
	
	private void decodeItem(Item i) {
		if(i.name.endsWith("稿子")) {
			People.PickaxeData p = new People.PickaxeData(People.PickaxeData.getEmptyId(), i.name, i.getRandomExtra(), i.getRandomValue(), 0);
			FileHolder.people.addPickaxe(p);
		}
		else if(i.name.endsWith("包包")) {
			People.BagData b = new People.BagData(People.BagData.getEmptyId(), i.name, i.getRandomValue());
			FileHolder.people.addBag(b);
		}
		else if(i.name.endsWith("引擎")) {
			Machine.Engine e = new Machine.Engine(Machine.Engine.getEmptyId(), i.name, i.getRandomExtra(), i.getRandomValue(), 0);
			FileHolder.machine.addEngine(e);
		}
		else if(i.name.endsWith("鑽頭")) {
			Machine.Head h = new Machine.Head(Machine.Head.getEmptyId(), i.name, i.getRandomExtra(), i.getRandomValue(), 0);
			FileHolder.machine.addHead(h);
		}
		else if(i.name.endsWith("電池")) {
			Machine.Battery b = new Machine.Battery(Machine.Battery.getEmptyId(), i.name, i.getRandomValue(), 0);
			FileHolder.machine.addBattery(b);
		}
		else { //箱子
			Machine.Chest c = new Machine.Chest(Machine.Chest.getEmptyId(), i.name, i.getRandomValue());
			FileHolder.machine.addChest(c);
		}
	}
	
}
