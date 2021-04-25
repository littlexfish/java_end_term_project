package hang_up_game.java.window;

import hang_up_game.java.game.Background;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class GameFrame extends JFrame {
	
	private final Object backLock = new Object();
	public LinkedHashMap<String, PanelCache> panelList;
	public LinkedList<String> keyList;
	
	public GameFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//		setIconImage(Constant.icon);
		
		panelList = new LinkedHashMap<>();
		keyList = new LinkedList<>();
		
		
		
		
		
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				synchronized(backLock) {
					if(keyList.size() > 1) { // not main menu
						goBack();
					}
					else { // is main menu, check close game or not
						onExit();
					}
				}
			}
		});
		
		Background.makeSureBackground();
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
