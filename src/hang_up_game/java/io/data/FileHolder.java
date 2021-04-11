package hang_up_game.java.io.data;

import hang_up_game.java.io.data.storage.Machine;
import hang_up_game.java.io.data.storage.Mineral;
import hang_up_game.java.io.data.storage.People;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class FileHolder {
	
	public static ArrayList<Saveable> saver = new ArrayList<>(7);
	public static Config config;
	public static Map map;
	public static MiningData miningData;
	public static Shop shop;
	public static Machine machine;
	public static Mineral mineral;
	public static People people;
	
	static {
		try {
			config = new Config();
			map = new Map();
			miningData = new MiningData();
			shop = new Shop();
			machine = new Machine();
			mineral = new Mineral();
			people = new People();
			saver.add(config);
			saver.add(map);
			saver.add(miningData);
			saver.add(shop);
			saver.add(machine);
			saver.add(mineral);
			saver.add(people);
		}
		catch(URISyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveFile() throws IOException {
		for(Saveable s : saver) {
			s.save();
		}
	}
	
}
