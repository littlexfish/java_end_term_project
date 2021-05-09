package hang_up_game.java.io;

import com.google.gson.Gson;
import com.google.gson.JsonStreamParser;
import hang_up_game.java.game.Mineral;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class FileChecker {
	
	private final File setting = new File(getClass().getResource("/hang_up_game/files/config.json").toURI());
	private final File map = new File(getClass().getResource("/hang_up_game/files/map.json").toURI());
	private final File shop = new File(getClass().getResource("/hang_up_game/files/shop.json").toURI());
	private final File storagePeople = new File(getClass().getResource("/hang_up_game/files/storage/people.json").toURI());
	private final File storageMachine = new File(getClass().getResource("/hang_up_game/files/storage/machine.json").toURI());
	private final File storageMineral = new File(getClass().getResource("/hang_up_game/files/storage/mineral.json").toURI());
	
	public FileChecker() throws URISyntaxException {}
	
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
			e.printStackTrace();
		}
		return false;
	}
	
	public void createFiles() throws IOException {
		Random seedRandom = new Random();
		long mapSeed = seedRandom.nextLong();
		long chestSeed = seedRandom.nextLong();
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
		json = new String(StandardCharsets.UTF_8.encode(StandardCharsets.ISO_8859_1.decode(ByteBuffer.wrap(json.getBytes()))).array());
		String to = new Gson().newBuilder().setPrettyPrinting().create().toJson(new JsonStreamParser(json).next());
		FileOutputStream fos = new FileOutputStream(file);
		for(char c : to.toCharArray()) {
			fos.write(c);
		}
		fos.close();
	}
	
}
