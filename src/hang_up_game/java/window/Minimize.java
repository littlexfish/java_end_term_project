package hang_up_game.java.window;

import java.awt.*;

//TODO: resume write this when game can run normally
public class Minimize {
	
	private TrayIcon ti;
	private SystemTray st;
	private PopupMenu pm;
	
	public Minimize() throws AWTException {
		if(!SystemTray.isSupported()) {
			System.err.println("system tray not support");
			return;
		}
		st = SystemTray.getSystemTray();
		ti = new TrayIcon(Constant.icon, Constant.ApplicationName);
		
		st.add(ti);
	}
	
	public void throwMsg(String title, String msg) {
		ti.displayMessage(title, msg, TrayIcon.MessageType.NONE);
	}
	
}
