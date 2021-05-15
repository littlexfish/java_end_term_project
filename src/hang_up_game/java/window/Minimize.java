package hang_up_game.java.window;

import hang_up_game.java.io.Log;

import java.awt.*;

public class Minimize {
	
	private TrayIcon ti;
	
	public Minimize(GameFrame gf) throws AWTException {
		if(!SystemTray.isSupported()) {
			System.err.println("system tray not support");
			return;
		}
		
		PopupMenu pm = new PopupMenu();
		
		MenuItem open = new MenuItem("open window");
		open.setFont(new Font("Arial", Font.PLAIN, 18));
		open.addActionListener(e -> gf.openWindow());
		pm.add(open);
		
		MenuItem exit = new MenuItem("exit");
		exit.setFont(new Font("Arial", Font.PLAIN, 18));
		exit.addActionListener(e -> gf.onExit());
		pm.add(exit);
		
		SystemTray st = SystemTray.getSystemTray();
		ti = new TrayIcon(Constant.icon, Constant.ApplicationName, pm);
		
		ti.addActionListener(e -> gf.openWindow());
		
		ti.setImageAutoSize(true);
		
		st.add(ti);
	}
	
	public void throwMsg(String title, String msg) {
		ti.displayMessage(Constant.ApplicationName +  ": " + title, msg, TrayIcon.MessageType.NONE);
		Log.i("message", "throw msg title:" + title + ", msg:" + msg);
	}
	
}
