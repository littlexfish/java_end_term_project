package hang_up_game.java.window;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class GameFrame extends JFrame {
	
	public LinkedHashMap<String, JPanel> panelList;
	public LinkedList<String> keyList;
	
	public GameFrame() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		panelList = new LinkedHashMap<>();
		keyList = new LinkedList<>();
		
		
		
		
		
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(panelList.size() > 1) { // not main menu
					goBack();
				}
				else { // is main menu, check close game or not
					onExit();
				}
			}
		});
		
	}
	
	public void setPanel(JPanel p, String title, int width, int height) {
		setBounds(Constant.getMiddleWindowRectangle(width, height));
		setTitle(title);
		setContentPane(p);
		panelList.put(title, p);
		keyList.add(title);
	}
	
	public void goBack() {
		panelList.remove(getTitle());
		keyList.removeLast();
		setContentPane(panelList.get(keyList.getLast()));
	}
	
	public void onExit() {
		GameExitDialog ged = new GameExitDialog(this);
		ged.setVisible(true);
	}
	
}
