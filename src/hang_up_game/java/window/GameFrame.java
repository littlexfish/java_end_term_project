package hang_up_game.java.window;

import hang_up_game.java.game.Background;
import hang_up_game.java.io.Log;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.window.crafting.Crafting;
import hang_up_game.java.window.menu_bar.Manual;
import hang_up_game.java.window.menu_bar.Version;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class GameFrame extends JFrame {
	
	private final Object backLock = new Object();
	public LinkedHashMap<String, PanelCache> panelList;
	public LinkedList<String> keyList;
	
	public GameFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setIconImage(Constant.icon);
		
		panelList = new LinkedHashMap<>();
		keyList = new LinkedList<>();
		
		JMenuBar jmb = new JMenuBar();
		setJMenuBar(jmb);
		
		JMenu file = new JMenu("檔案");
		file.setFont(new Font("SimSun", Font.PLAIN, 15));
		jmb.add(file);
		
		JMenuItem save = new JMenuItem("手動存檔", Constant.getIcon("save", 20, 20));
		save.setFont(new Font("SimSun", Font.PLAIN, 15));
		save.addActionListener(e -> {
			Log.i("file", "saving...");
			try {
				FileHolder.saveFile();
				Log.i("file", "succeed");
				Background.throwMsg("檔案", "儲存完成");
			}
			catch(IOException ioException) {
				ioException.printStackTrace(FileHolder.getExportCrashReport());
				ioException.printStackTrace();
				Log.e("file", "failed");
				Background.throwMsg("檔案", "儲存失敗");
			}
		});
		file.add(save);
		
		JMenuItem export = new JMenuItem("匯出(尚未實作)", Constant.getIcon("export", 20, 20));
		export.setFont(new Font("SimSun", Font.PLAIN, 15));
		export.setEnabled(false);
		file.add(export);
		
		JMenu help = new JMenu("幫助");
		help.setFont(new Font("SimSun", Font.PLAIN, 15));
		jmb.add(help);
		
		JMenuItem version = new JMenuItem("關於  ", Constant.getIcon("info", 20, 20));
		version.setFont(new Font("SimSun", Font.PLAIN, 15));
		version.addActionListener(e -> new Version(this).setVisible(true));
		help.add(version);
		
		JMenuItem manual = new JMenuItem("說明  ", Constant.getIcon("help", 20, 20));
		manual.setFont(new Font("SimSun", Font.PLAIN, 15));
		manual.addActionListener(e -> Manual.openManual());
		help.add(manual);
		
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				synchronized(backLock) {
					if(keyList.size() > 1) { // not main menu
						Log.i("game", "back to last panel");
						goBack();
					}
					else { // is main menu, check close game or not
						Log.i("game", "on exit");
						onExit();
					}
				}
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				dispose();
				Log.i("game", "minimized");
				Background.throwMsg("最小化", "遊戲已隱藏至圖示");
			}
			
		});
		
		Background.makeSureBackground(this);
		
	}
	
	public void openWindow() {
		setVisible(true);
		setState(JFrame.NORMAL);
	}
	
	public void setPanel(JPanel p, String title, int width, int height) {
		synchronized(backLock) {
			setTitle(title);
			setContentPane(p);
			pack();
			setBounds(Constant.getMiddleWindowRectangle(width, height));
			panelList.put(title, new PanelCache(p, width, height));
			keyList.add(title);
		}
	}
	
	public void setPanel(JPanel p, String title) {
		synchronized(backLock) {
//			if(p instanceof Crafting) {
//				Background.startCharge();
//			}
			setTitle(title);
			setContentPane(p);
			int width = getWidth(), height = getHeight();
			pack();
			setSize(width, height);
			panelList.put(title, new PanelCache(p, 0, 0));
			keyList.add(title);
		}
	}
	
	public void goBack() {
//		if(panelList.get(getTitle()).pane instanceof Crafting) {
//			Background.stopCharge();
//		}
		panelList.remove(getTitle());
		keyList.removeLast();
		PanelCache pc = panelList.get(keyList.getLast());
		setContentPane(pc.pane);
		setTitle(keyList.getLast());
		if(pc.width > 0 && pc.height > 0) {
			setSize(pc.width, pc.height);
		}
	}
	
	public void onExit() {
		Background.stopCharge();
		Background.stopRecover();
		GameExitDialog ged = new GameExitDialog(this);
		ged.setVisible(true);
	}
	
	static class PanelCache {
		public final JPanel pane;
		public final int width;
		public final int height;
		public PanelCache(JPanel panel, int w, int h) {
			pane = panel;
			width = w;
			height = h;
		}
	}
	
}
