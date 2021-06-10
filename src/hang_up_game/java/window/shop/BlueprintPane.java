package hang_up_game.java.window.shop;

import hang_up_game.java.game.Blueprint;
import hang_up_game.java.game.Blueprints;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.window.WarningWindow;
import org.lf.logger.Log;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BlueprintPane extends JPanel {
	
	private final int priceEach = 50;
	private final JFrame frame;
	
	public BlueprintPane(JFrame owner) {
		Log.d("blueprintPane", "init");
		frame = owner;
		
		setLayout(new BorderLayout(5, 5));
		
		JScrollPane scroll = new JScrollPane();
		add(scroll, BorderLayout.CENTER);
		
		JLabel moneyT = new JLabel("剩餘金額: $" + FileHolder.shop.getMoney());
		add(moneyT, BorderLayout.NORTH);
		
		JList<Blueprint> blueprint = new JList<>(Blueprints.getBlueprintCanBuy().toArray(new Blueprint[0]));
		blueprint.setDragEnabled(true);
		blueprint.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		blueprint.setFocusable(false);
		scroll.setViewportView(blueprint);
		
		JButton buy = new JButton("購買 $0");
		buy.setFocusable(false);
		buy.addActionListener(e -> {
			if(blueprint.isSelectionEmpty()) return;
			List<Blueprint> bs = blueprint.getSelectedValuesList();
			int price = bs.size() * priceEach;
			int money = FileHolder.shop.getMoney();
			if(money < price) {
				new WarningWindow("", "金額不足", WarningWindow.DialogType_Time, JDialog.DISPOSE_ON_CLOSE, 3000).setVisible(true);
				return;
			}
			boolean isBuy = ConfirmDialog.waitAndGetStatus(frame, price);
			if(isBuy) {
				FileHolder.shop.addMoney(-price);
				for(Blueprint b : bs) {
					FileHolder.shop.addBlueprint(b.id);
				}
				blueprint.clearSelection();
				blueprint.setListData(Blueprints.getBlueprintCanBuy().toArray(new Blueprint[0]));
				moneyT.setText("剩餘金額: $" + FileHolder.shop.getMoney());
			}
		});
		add(buy, BorderLayout.SOUTH);
		
		blueprint.addListSelectionListener(e -> {
			if(blueprint.isSelectionEmpty()) return;
			List<Blueprint> bs = blueprint.getSelectedValuesList();
			int price = bs.size() * priceEach;
			buy.setText("購買 $" + price);
		});
		
	}

}
