package hang_up_game.java.window;

import hang_up_game.java.game.Background;
import hang_up_game.java.game.MachineMiner;
import hang_up_game.java.io.Log;
import hang_up_game.java.io.data.FileHolder;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GameExitDialog extends JDialog {
	
	public GameExitDialog(JFrame owner) {
		super(owner, true);
		setTitle("離開");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(Constant.getMiddleWindowRectangle(250, 150));
		getContentPane().setLayout(new BorderLayout(5, 5));
		
		JLabel msg = new JLabel("<html>確定要離開了嗎?<br>離開後會讓所有挖礦機強制返回</html>");
		msg.setHorizontalAlignment(JLabel.CENTER);
		getContentPane().add(msg, BorderLayout.CENTER);
		
		JPanel button = new JPanel(new GridLayout(1, 2, 5, 5));
		getContentPane().add(button, BorderLayout.SOUTH);
		
		JButton cancel = new JButton("取消");
		cancel.setFocusable(false);
		cancel.addActionListener(e -> backToGame());
		button.add(cancel);
		
		JButton confirm = new JButton("確定");
		confirm.setFocusable(false);
		confirm.addActionListener(e -> saveAllFile());
		button.add(confirm);
		
	}
	
	private void saveAllFile() {
		Log.i("exit", "start exit progress");
		Background.throwMsg("挖礦機", "返回挖礦機...");
		Log.i("miner", "return machine...");
		ArrayList<MachineMiner> mm = new ArrayList<>(Background.getAllMinerOnline());
		for(MachineMiner m : mm) {
			Background.returnMachineMiner(m);
		}
		Log.i("miner", "return machine success");
		Log.i("file", "saving...");
//		Background.throwMsg("檔案", "儲存檔案中...");
		try {
			FileHolder.saveFile();
			Background.throwMsg("檔案", "儲存完成");
			Log.i("file", "success");
		}
		catch(IOException e) {
			Background.throwMsg("檔案", "儲存失敗");
			Log.i("file", "failed");
			e.printStackTrace(FileHolder.getExportCrashReport());
			e.printStackTrace();
		}
		Log.i("main", "finish");
		System.exit(0);
	}
	
	private void backToGame() {
		Log.i("exit window", "close");
		dispose();
	}
	
}
