package hang_up_game.java.io;

import com.google.gson.Gson;
import com.google.gson.JsonStreamParser;
import hang_up_game.java.game.Mineral;
import hang_up_game.java.io.data.FileHolder;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;

public class FileChecker {
	
	private final File setting = new File("./miner/config.json");
	private final File map = new File("./miner/map.json");
	private final File shop = new File("./miner/shop.json");
	private final File storagePeople = new File("./miner/storage/people.json");
	private final File storageMachine = new File("./miner/storage/machine.json");
	private final File storageMineral = new File("./miner/storage/mineral.json");
	
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public FileChecker() {
		try {
			File rootDir = new File("./miner");
			if(!rootDir.exists()) rootDir.mkdir();
			if(!setting.exists()) setting.createNewFile();
			if(!map.exists()) map.createNewFile();
			if(!shop.exists()) shop.createNewFile();
			File storage = new File("./miner/storage");
			if(!storage.exists()) storage.mkdir();
			if(!storagePeople.exists()) storagePeople.createNewFile();
			if(!storageMachine.exists()) storageMachine.createNewFile();
			if(!storageMineral.exists()) storageMineral.createNewFile();
		}
		catch(IOException e) {
			e.printStackTrace(FileHolder.getExportCrashReport());
			e.printStackTrace();
		}
	}
	
	/**
	 *
	 * @return false if any file is not exist
	 */
	public boolean checkFiles() {
		try {
			if(setting.length() == 0) {
				return false;
			}
			return new JsonStreamParser(new FileReader(setting)).next().getAsJsonObject().get("play").getAsBoolean();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace(FileHolder.getExportCrashReport());
			e.printStackTrace();
		}
		return false;
	}
	
	public void createFiles() throws IOException {
		Random seedRandom = new Random();
		int mapSeed = seedRandom.nextInt();
		int chestSeed = seedRandom.nextInt();
		addJsonToFile("{\"play\":true,\"background\":false,\"notice\":{\"hit\":false,\"lowBattery\":false,\"fullChest\":false},\"gameData\":{\"level\":1,\"exp\":0,\"mapSeed\":" + mapSeed + ",\"chestSeed\":" + chestSeed + "}}", setting);
		addJsonToFile("[]", map);
		addJsonToFile("{\"money\":100,\"unlockItemType\":[-1],\"bluePrintId\":[0,6,12,18,24,30]}", shop);
		addJsonToFile("{\"people\":[{\"name\":\"新手\",\"id\":1,\"maxStamina\":500,\"lastStamina\":500,\"strong\":20,\"skillId\":0," +
				"\"skillValue\": []}],\"pickaxe\":[{\"id\":1,\"name\":\"新手稿子\",\"level\":1,\"maxDamage\":500,\"damage\":0}]," +
				"\"bag\":[{\"id\":1,\"name\":\"新手包包\",\"maxSpace\":20}]}", storagePeople);
		addJsonToFile("{\"engine\":[],\"head\":[],\"battery\":[],\"chest\":[],\"plugin\":[]}", storageMachine);
		StringBuilder sb = new StringBuilder(Mineral.values().length * 10);
		for(Mineral m : Mineral.values()) {
			sb.append(",\"").append(m.name()).append("\":0");
		}
		addJsonToFile("{\"space\":1000" + sb.toString() + "}", storageMineral);
	}
	
	private void addJsonToFile(String json, File file) throws IOException {
		json = new String(json.getBytes(Charset.defaultCharset()));
		String to = new Gson().toJson(new JsonStreamParser(json).next());
		BufferedWriter bw = Files.newBufferedWriter(file.toPath());
		bw.write(to);
		bw.close();
	}
	
}
