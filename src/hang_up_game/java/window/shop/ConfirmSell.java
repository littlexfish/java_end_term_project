package hang_up_game.java.window.shop;

import hang_up_game.java.io.Log;
import hang_up_game.java.window.Constant;

import javax.swing.*;
import java.awt.*;

public class ConfirmSell extends JDialog {
	
	private int status = -1;
	
	public ConfirmSell(JFrame owner, int price) {
		super(owner, true);
		Log.d("confirmSell", "init with price:" + price);
		setBounds(Constant.getMiddleWindowRectangle(100, 100));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		JLabel msg = new JLabel("<html>確定要販賣這些嗎?<br>金額:" + price + "</html>");
		msg.setHorizontalAlignment(JLabel.CENTER);
		getContentPane().add(msg, BorderLayout.CENTER);
		
		JPanel button = new JPanel(new GridLayout(1, 2, 5, 5));
		getContentPane().add(button, BorderLayout.SOUTH);
		
		JButton cancel = new JButton("取消");
		cancel.setFocusable(false);
		cancel.addActionListener(e -> {
			status = 0;
			dispose();
		});
		button.add(cancel);
		
		JButton confirm = new JButton("確認");
		confirm.setFocusable(false);
		confirm.addActionListener(e -> {
			status = 1;
			dispose();
		});
		button.add(confirm);
	}
	
	public int getStatus() {
		return status;
	}
	
	public static boolean waitAndGetStatus(JFrame owner, int price) {
		ConfirmSell cd = new ConfirmSell(owner, price);
		cd.setVisible(true);
		//noinspection StatementWithEmptyBody
		while(cd.status == -1);
		return cd.status == 1;
	}
	
}
