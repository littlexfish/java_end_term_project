package hang_up_game.java.window;

import hang_up_game.java.io.data.FileHolder;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameExitDialog extends JDialog {
	
	public GameExitDialog(JFrame owner) {
		super(owner, true);
		setTitle("離開");
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setBounds(Constant.getMiddleWindowRectangle(250, 100));
		getContentPane().setLayout(new BorderLayout(5, 5));
		
		JLabel msg = new JLabel("確定要離開了嗎?");
		msg.setHorizontalAlignment(JLabel.CENTER);
		getContentPane().add(msg, BorderLayout.CENTER);
		
		JPanel button = new JPanel(new GridLayout(1, 2, 5, 5));
		getContentPane().add(button, BorderLayout.SOUTH);
		
		JButton cancel = new JButton("取消");
		cancel.setFocusable(false);
		cancel.addActionListener(e -> {
			backToGame();
		});
		button.add(cancel);
		
		JButton confirm = new JButton("確定");
		confirm.setFocusable(false);
		confirm.addActionListener(e -> {
			saveAllFile();
		});
		button.add(confirm);
		
	}
	
	private void saveAllFile() {
		try {
			FileHolder.saveFile();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	private void backToGame() {
		dispose();
	}
	
}
