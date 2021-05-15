package hang_up_game;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import hang_up_game.java.io.FileChecker;
import hang_up_game.java.io.Log;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.window.GameFrame;
import hang_up_game.java.window.MainMenu;
import hang_up_game.java.window.Open;
import hang_up_game.java.window.menu_bar.Manual;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
	
	public static void main(String[] args) {
		//set log type
		if(args.length > 0) {
			for(String s : args) {
				if("DEBUG".equals(s.toUpperCase())) {
					Log.setLogType(Log.DEBUG);
					break;
				}
				if("INFO".equals(s.toUpperCase())) {
					Log.setLogType(Log.INFO);
					break;
				}
				if("WARNING".equals(s.toUpperCase())) {
					Log.setLogType(Log.WARNING);
					break;
				}
				if("ERROR".equals(s.toUpperCase())) {
					Log.setLogType(Log.ERROR);
					break;
				}
			}
		}
		Log.i("main", "set windows look and feel...");
		try {
			UIManager.setLookAndFeel(new WindowsLookAndFeel());
			Log.i("main", "set windows look and feel success");
		}
		catch(UnsupportedLookAndFeelException e) {
			Log.i("main", "windows look and feel not supported");
			e.printStackTrace(FileHolder.getExportCrashReport());
			e.printStackTrace();
		}
		try {
			Log.i("main", "checking files...");
			Open openFrame = new Open();
			openFrame.setVisible(true);
			checkPlay(openFrame);
		}
		catch(Exception e) {
			Log.w("main", "exception occurred");
			e.printStackTrace(FileHolder.getExportCrashReport());
			e.printStackTrace();
		}
	}
	
	public static void checkPlay(Open f) {
		FileChecker fc = new FileChecker();
		boolean t = !fc.checkFiles();
		if(t) {
			Log.i("main", "first play");
			firstPlay(f, fc);
		}
		Log.i("main", "loading game...");
		loadGame(f, t);
	}
	
	public static void firstPlay(Open f, FileChecker fc) {
		try {
			Log.i("main", "create game needed files");
			fc.createFiles();
		}
		catch(IOException e) {
			e.printStackTrace(FileHolder.getExportCrashReport());
			e.printStackTrace();
		}
		f.dispose();
	}
	
	public static void loadGame(Open f, boolean isFirst) {
		if(f.isVisible()) f.dispose();
		GameFrame gf = new GameFrame();
		Log.i("main", "open main menu");
		gf.setPanel(new MainMenu(gf), "主頁面", 500, 380);
		gf.setVisible(true);
		if(isFirst) {
			Log.i("main", "open manual");
			Manual.openManual();
		}
	}
	
}
