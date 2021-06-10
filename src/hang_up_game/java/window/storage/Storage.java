package hang_up_game.java.window.storage;

import hang_up_game.java.game.Mineral;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.storage.Item;
import org.lf.logger.Log;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;

public class Storage extends JPanel {

	public Storage() {
		Log.d("storage", "init");
		setLayout(new BorderLayout(5, 5));
		
		JPanel center = new JPanel(new GridLayout(1, 2, 5, 5));
		add(center, BorderLayout.CENTER);
		
		JLabel leftPad = new JLabel();
		add(leftPad, BorderLayout.WEST);
		
		JLabel upPad = new JLabel();
		add(upPad, BorderLayout.NORTH);
		
		
		JPanel left = new JPanel(new BorderLayout(5, 5));
		center.add(left);
		
		JLabel mater = new JLabel("材料");
		mater.setHorizontalAlignment(SwingConstants.CENTER);
		left.add(mater, BorderLayout.NORTH);
		
		Mineral[] ms = Mineral.values();
		
		int size = ms.length;
		
		JPanel materialS = new JPanel(new GridLayout(size, 2, 5, 5));
		left.add(materialS, BorderLayout.CENTER);
		
		EnumMap<Mineral, Integer> minerals = FileHolder.mineral.getAllMineral();
		JLabel[][] materV = new JLabel[size][2];
		
		for(int i = 0;i < size;i++) {
			
			materV[i][0] = new JLabel(ms[i].chinese);
			materialS.add(materV[i][0]);
			
			materV[i][1] = new JLabel(minerals.get(ms[i]).toString());
			materV[i][1].setHorizontalAlignment(SwingConstants.RIGHT);
			materialS.add(materV[i][1]);
			
		}
		
		JPanel right = new JPanel(new BorderLayout(5, 5));
		center.add(right);
		
		JLabel item = new JLabel("物品");
		item.setHorizontalAlignment(SwingConstants.CENTER);
		right.add(item, BorderLayout.NORTH);
		
		JScrollPane scroll = new JScrollPane();
		right.add(scroll, BorderLayout.CENTER);
		
		ArrayList<Item> items = FileHolder.getAllStorage();
		
		JList<Item> itemList = new JList<>(items.toArray(new Item[0]));
		scroll.setViewportView(itemList);
		
	}

}
