package hang_up_game.java.window;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Minimize {
	
	private TrayIcon ti;
	private SystemTray st;
	private PopupMenu pm;
	
	public Minimize(GameFrame gf) throws AWTException {
		if(!SystemTray.isSupported()) {
			System.err.println("system tray not support");
			return;
		}
		
		pm = new PopupMenu();
		
		MenuItem open = new MenuItem("open window");
		open.setFont(new Font("Arial", Font.PLAIN, 18));
		open.addActionListener(e -> {
			gf.openWindow();
		});
		pm.add(open);
		
		MenuItem exit = new MenuItem("exit");
		exit.setFont(new Font("Arial", Font.PLAIN, 18));
		exit.addActionListener(e -> {
			gf.onExit();
		});
		pm.add(exit);
		
		st = SystemTray.getSystemTray();
		ti = new TrayIcon(Constant.icon, Constant.ApplicationName, pm);
		
		ti.addActionListener(e -> {
			gf.openWindow();
		});
		
		ti.setImageAutoSize(true);
		
		st.add(ti);
	}
	
	public void throwMsg(String title, String msg) {
		ti.displayMessage(Constant.ApplicationName +  ": " + title, msg, TrayIcon.MessageType.NONE);
	}
	
}
