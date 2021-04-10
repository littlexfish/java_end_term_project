package hang_up_game.java.io;

import com.google.gson.Gson;
import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.net.URISyntaxException;

public class FileChecker {
	
	private final File setting = new File(getClass().getResource("/hang_up_game/files/config.json").toURI());
	private final File map = new File(getClass().getResource("/hang_up_game/files/map.json").toURI());
	private final File miningData = new File(getClass().getResource("/hang_up_game/files/miningData.json").toURI());
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
			System.out.println(setting.length());
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
	
//	@SuppressWarnings("StatementWithEmptyBody")
//	public void createFilesAndOverride() throws IOException {
//		while(!setting.delete());
//		while(!map.delete());
//		while(!miningData.delete());
//		while(!shop.delete());
//		while(!storageMineral.delete());
//		while(!storageMachine.delete());
//		while(!storagePeople.delete());
//		while(!storage.delete());
//		createFiles();
//	}
	
	public void createFiles() throws IOException {
		System.out.println("set data");
		addJsonToFile("{\"play\":false,\"background\":false,\"notice\":{\"hit\":false,\"lowBattery\":false,\"fullChest\":false},\"gameData\":{\"level\":1,\"exp\":0}}", setting);
		addJsonToFile("[]", map);
		addJsonToFile("[]", miningData);
		addJsonToFile("{\"unlockItemType\":[],\"bluePrintId\":[]}", shop);
		addJsonToFile("{\"people\":[],\"pickaxe\":[],\"bag\":[]}", storagePeople);
		addJsonToFile("{\"engine\":[],\"head\":[],\"battery\":[],\"chest\":[],\"plugin\":[]}", storageMachine);
		addJsonToFile("{\"space\":1000,\"copper\":0,\"iron\":0,\"silver\":0,\"aluminum\":0,\"gold\":0,\"crystal\":0}", storageMineral);
		System.out.println("set successful");
	}
	
	private void addJsonToFile(String json, File file) throws IOException {
		String to = new Gson().newBuilder().setPrettyPrinting().create().toJson(new JsonStreamParser(json).next());
		FileOutputStream fos = new FileOutputStream(file);
		for(char c : to.toCharArray()) {
			fos.write(c);
		}
	}
	
}
