package hang_up_game.java.window.shop;

import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.window.Constant;
import hang_up_game.java.window.WarningWindow;

import javax.swing.*;
import java.awt.*;

public class ConfirmDialog extends JDialog {
	
	private int status = -1;
	
	public ConfirmDialog(JFrame owner, int price) {
		super(owner, true);
		setBounds(Constant.getMiddleWindowRectangle(100, 100));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JLabel msg = new JLabel("<html>確定要購買這些嗎?<br>金額:" + price + "</html>");
		msg.setHorizontalAlignment(JLabel.CENTER);
		getContentPane().add(msg, BorderLayout.CENTER);
		
		JPanel button = new JPanel(new GridLayout(1, 2, 5, 5));
		getContentPane().add(button, BorderLayout.SOUTH);
		
		JButton cancel = new JButton("取消");
		cancel.addActionListener(e -> {
			status = 0;
			dispose();
		});
		button.add(cancel);
		
		JButton confirm = new JButton("確認");
		confirm.addActionListener(e -> {
			status = 1;
		});
		button.add(confirm);
	}
	
	public int getStatus() {
		return status;
	}
	
	public static boolean waitAndGetStatus(JFrame owner, int price) {
		ConfirmDialog cd = new ConfirmDialog(owner, price);
		cd.setVisible(true);
		//noinspection StatementWithEmptyBody
		while(cd.status == -1);
		return cd.status == 1;
	}
	
}