package hang_up_game.java.io;

import com.google.gson.*;
import hang_up_game.java.game.Background;
import hang_up_game.java.game.Mineral;
import hang_up_game.java.io.data.FileHolder;
import org.lf.logger.Log;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
	
	public boolean checkImport() {
//		File dir = new File("./");
//		File[] files = dir.listFiles((dir1, name) -> {
//			if(name.endsWith(".miner")) return true;
//			return false;
//		});
//		assert files != null; //will not occur
//		return files.length >= 1;
		return false; //TODO: original zip from java not support chinese
	}
	
	public boolean checkImportIsOnlyOne() {
		File dir = new File("./");
		File[] files = dir.listFiles((dir1, name) -> {
			if(name.endsWith(".miner")) return true;
			return false;
		});
		assert files != null; //will not occur
		return files.length > 1;
	}
	
	public File getImport(int index) {
		File dir = new File("./");
		File[] files = dir.listFiles(pathname -> {
			if(pathname.getPath().endsWith(".miner")) return true;
			return false;
		});
		assert files != null; //will not occur
		return files[index];
	}
	
	public boolean extractZIP(File minerFile) throws IOException {
		if(!minerFile.exists() || !minerFile.getName().endsWith(".miner")) throw new IllegalArgumentException("not miner file");
		Log.i("import", "path: " + minerFile.getAbsolutePath());
		ZipFile zip = new ZipFile(minerFile);
		boolean isAnyFail = false;
		if(!writeToFile(zip, "miner/storage/machine.json", new File("miner/storage/machine.json"), new JsonObject()))
			isAnyFail = true;
		if(!writeToFile(zip, "miner/storage/mineral.json", new File("miner/storage/mineral.json"), new JsonObject()))
			isAnyFail = true;
		if(!writeToFile(zip, "miner/storage/people.json", new File("miner/storage/people.json"), new JsonObject()))
			isAnyFail = true;
		if(!writeToFile(zip, "miner/config.json", new File("miner/config.json"), new JsonObject()))
			isAnyFail = true;
		if(!writeToFile(zip, "miner/map.json", new File("miner/map.json"), new JsonArray()))
			isAnyFail = true;
		if(!writeToFile(zip, "miner/shop.json", new File("miner/shop.json"), new JsonObject()))
			isAnyFail = true;
		if(!isAnyFail) {
			Log.i("import", "import successful");
			Background.throwMsg("導入", "導入成功，檔案位置: " + minerFile.getAbsolutePath());
		}
		return !isAnyFail;
	}
	
	private boolean writeToFile(ZipFile zip, String entryName, File in, JsonElement je) throws IOException {
		Log.d("import", entryName);
		InputStream is = zip.getInputStream(new ZipEntry(entryName));
		BufferedWriter bw = Files.newBufferedWriter(in.toPath());
		int c = is.read();
		while(c != -1) {
			bw.write(c);
			c = is.read();
		}
		is.close();
		bw.close();
		Log.d("import", "check file done...");
		if(checkImportSuccess(in, je)) {
			Log.i("import", "import fail");
			Background.throwMsg("導入", "導入失敗，檔案位置: " + entryName);
			return false;
		}
		return true;
	}
	
	private boolean checkImportSuccess(File in, JsonElement je) throws IOException {
		if(je instanceof JsonArray) return new JsonStreamParser(Files.newBufferedReader(in.toPath())).next().isJsonArray();
		if(je instanceof JsonObject) return new JsonStreamParser(Files.newBufferedReader(in.toPath())).next().isJsonObject();
		return false;
	}
	
	public void createFiles() throws IOException {
		Log.d("file", "create new files...");
		Random seedRandom = new Random();
		long mapSeed = seedRandom.nextLong();
		long chestSeed = seedRandom.nextLong();
		int strong = 10 + seedRandom.nextInt(91);
		addJsonToFile("{\"play\":true,\"background\":false,\"notice\":{\"hit\":false,\"lowBattery\":false,\"fullChest\":false},\"gameData\":{\"level\":1,\"exp\":0,\"mapSeed\":" + mapSeed + ",\"chestSeed\":" + chestSeed + "}}", setting);
		addJsonToFile("[]", map);
		addJsonToFile("{\"money\":100,\"unlockItemType\":[-1],\"bluePrintId\":[0,6,12,18,24,30]}", shop);
		addJsonToFile("{\"people\":[{\"name\":\"新手\",\"id\":1,\"maxStamina\":500,\"lastStamina\":500,\"strong\":" + strong + ",\"skillId\":0," +
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
