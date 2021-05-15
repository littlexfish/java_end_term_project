package hang_up_game.java.window.shop;

import hang_up_game.java.game.Mineral;
import hang_up_game.java.io.data.FileHolder;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;

public class MaterialSell extends JPanel {
	
	private final JFrame frame;
	
	public MaterialSell(JFrame f) {
		frame = f;
		EnumMap<Mineral, Integer> minerals = FileHolder.mineral.getAllMineral();
		setLayout(new BorderLayout(5, 5));
		
		Mineral[] ms = Mineral.values();
		
		int size = ms.length;
		
		JLabel moneyT = new JLabel("剩餘金額: $" + FileHolder.shop.getMoney());
		add(moneyT, BorderLayout.NORTH);
		
		JPanel spinners = new JPanel(new GridLayout(size, 4, 5, 5));
		add(spinners, BorderLayout.CENTER);
		
		JLabel[] texts = new JLabel[size];
		JButton[] reset = new JButton[size];
		JSpinner[] mineralSpinner = new JSpinner[size];
		JButton[] maxButton = new JButton[size];
		
		for(int i = 0;i < size;i++) {
			int finalI = i;
			//text
			texts[i] = new JLabel(ms[i].chinese + " $" + ms[i].price);
			spinners.add(texts[i]);
			
			//spinners reset button
			reset[i] = new JButton("重設");
			reset[i].setFocusable(false);
			reset[i].addActionListener(e -> mineralSpinner[finalI].setValue(0));
			spinners.add(reset[i]);
			
			//spinners
			int max = minerals.get(ms[i]), min = 0;
			SpinnerNumberModel snm = new SpinnerNumberModel(0, min, max, 1);
			mineralSpinner[i] = new JSpinner(snm);
			((JSpinner.NumberEditor)mineralSpinner[i].getEditor()).getTextField().setEnabled(false);
			spinners.add(mineralSpinner[i]);
			
			//spinners max button
			maxButton[i] = new JButton("最大");
			maxButton[i].setFocusable(false);
			maxButton[i].addActionListener(e -> mineralSpinner[finalI].setValue(((SpinnerNumberModel)mineralSpinner[finalI].getModel()).getMaximum()));
			spinners.add(maxButton[i]);
			
		}
		
		JButton sell = new JButton("販賣 $0");
		sell.setFocusable(false);
		sell.addActionListener(e -> {
			int[] values = new int[size];
			int price = 0;
			for(int i = 0;i < size;i++) {
				int value = (int)mineralSpinner[i].getValue();
				price += value * ms[i].price;
				values[i] = value;
			}
			boolean isSell = ConfirmSell.waitAndGetStatus(frame, price);
			if(isSell) {
				FileHolder.shop.addMoney(price);
				decodeMineral(values);
				refreshSpinner(mineralSpinner);
				moneyT.setText("剩餘金額: $" + FileHolder.shop.getMoney());
			}
		});
		add(sell, BorderLayout.SOUTH);
		
		for(int i = 0;i < size;i++) {
			mineralSpinner[i].addChangeListener(e -> {
				int price = 0;
				for(int a = 0;a < size;a++) {
					int value = (int)mineralSpinner[a].getValue();
					price += value * ms[a].price;
				}
				sell.setText("販賣 $" + price);
			});
		}
		
	}
	
	private void decodeMineral(int[] values) {
		Mineral[] ms = Mineral.values();
		for(int i = 0;i < values.length;i++) {
			FileHolder.mineral.setMineral(ms[i], FileHolder.mineral.getMineral(ms[i]) - values[i]);
		}
	}
	
	private void refreshSpinner(JSpinner[] mineralSpinner) {
		Mineral[] ms = Mineral.values();
		for(int i = 0;i < ms.length;i++) {
			int max = FileHolder.mineral.getMineral(ms[i]), min = 0;
			SpinnerNumberModel snm = new SpinnerNumberModel(0, min, max, 1);
			mineralSpinner[i].setModel(snm);
			((JSpinner.NumberEditor)mineralSpinner[i].getEditor()).getTextField().setEnabled(false);
		}
	}
	
}
