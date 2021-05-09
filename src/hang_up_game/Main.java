package hang_up_game;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import hang_up_game.java.io.FileChecker;
import hang_up_game.java.window.GameFrame;
import hang_up_game.java.window.MainMenu;
import hang_up_game.java.window.Open;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.SortedMap;

public class Main {
	
	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(new WindowsLookAndFeel());
		Open openFrame = new Open();
		openFrame.setVisible(true);
		checkPlay(openFrame);
//		SortedMap<String, Charset> set = Charset.availableCharsets();
//		ArrayList<String> se = new ArrayList<>(set.keySet());
//		System.out.println(Arrays.toString(se.toArray(new String[0])));
	}
	
	public static void checkPlay(Open f) {
		try {
			FileChecker fc = new FileChecker();
			if(!fc.checkFiles()) {;
				firstPlay(f, fc);
			}
		}
		catch(URISyntaxException e) {
			e.printStackTrace();
		}
		loadGame(f);
	}
	
	public static void firstPlay(Open f, FileChecker fc) {
		try {
			fc.createFiles();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		f.dispose();
	}
	
	public static void loadGame(Open f) {
		if(f.isVisible()) f.dispose();
		GameFrame gf = new GameFrame();
		gf.setPanel(new MainMenu(gf), "主頁面", 500, 380);
		gf.setVisible(true);
	}
	
}
