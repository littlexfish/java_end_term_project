package hang_up_game.java.io.data;

import hang_up_game.java.game.Background;
import hang_up_game.java.game.Blueprint;
import hang_up_game.java.game.Blueprints;
import hang_up_game.java.io.Log;
import hang_up_game.java.io.data.storage.Item;
import hang_up_game.java.io.data.storage.Machine;
import hang_up_game.java.io.data.storage.Mineral;
import hang_up_game.java.io.data.storage.People;
import hang_up_game.java.window.people.PeopleDetail;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
		Log.d("file", "files init");
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
			saver.add(shop);
			saver.add(machine);
			saver.add(mineral);
			saver.add(people);
		}
		catch(IOException e) {
			e.printStackTrace(FileHolder.getExportCrashReport());
			e.printStackTrace();
		}
	}
	
	public static void saveFile() throws IOException {
		for(Saveable s : saver) {
			Log.d("saver", s.toString());
			s.save();
		}
	}
	
	public static ArrayList<Item> getAllStorage() {
		ArrayList<Item> items = new ArrayList<>();
		items.addAll(people.getAllData());
		items.addAll(machine.getAllPart());
		return items;
	}
	
	public static ArrayList<Item> getNeedFix() {
		LinkedList<Item> all = new LinkedList<>(getAllStorage());
		ArrayList<Item> toReturn = new ArrayList<>();
		for(Item item : all) {
			if(item instanceof People.BagData) continue;
			if(item instanceof Machine.Chest) continue;
			if(item instanceof Machine.Battery) continue;
			if(item instanceof Machine.Plugin) continue;
			if(item instanceof People.PickaxeData) {
				People.PickaxeData p = (People.PickaxeData)item;
				if(p.damage <= 0) continue;
			}
			if(item instanceof Machine.Engine) {
				Machine.Engine e = (Machine.Engine)item;
				if(e.getDamage() <= 0) continue;
			}
			if(item instanceof Machine.Head) {
				Machine.Head h = (Machine.Head)item;
				if(h.getDamage() <= 0) continue;
			}
			toReturn.add(item);
		}
		return toReturn;
	}
	
	public static ArrayList<Blueprint> getBlueprintUnlock() {
		int[] blue = shop.getBlueprints();
		ArrayList<Blueprint> blueprints = new ArrayList<>(blue.length);
		for(int i : blue) {
			blueprints.add(Blueprints.blueprints.get(i));
		}
		return blueprints;
	}
	
	public static void storage(java.util.Map<hang_up_game.java.game.Mineral, Integer> mineralWithAmount, Set<Item> items) {
		Set<hang_up_game.java.game.Mineral> allMineral = mineralWithAmount.keySet();
		for(hang_up_game.java.game.Mineral m : allMineral) {
			mineral.setMineral(m, mineral.getMineral(m) + mineralWithAmount.get(m));
		}
		for(Item i : items) {
			if(i instanceof Machine.Engine) {
				machine.addEngine((Machine.Engine)i);
			}
			if(i instanceof Machine.Head) {
				machine.addHead((Machine.Head)i);
			}
			if(i instanceof Machine.Battery) {
				machine.addBattery((Machine.Battery)i);
			}
			if(i instanceof Machine.Chest) {
				machine.addChest((Machine.Chest)i);
			}
			if(i instanceof Machine.Plugin) {
				machine.setPluginCount(i.id, machine.getPluginCountFromId(i.id) + ((Machine.Plugin)i).getCount());
			}
			if(i instanceof People.PickaxeData) {
				people.addPickaxe((People.PickaxeData)i);
			}
			if(i instanceof People.BagData) {
				people.addBag((People.BagData)i);
			}
		}
		try {
			saveFile();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static void changeTools(PeopleDetail pd, People.PeopleData peopleData, People.PickaxeData pickaxeData) {
		people.setPeople(peopleData);
		people.setPickaxe(pickaxeData);
		pd.allPeople = new ArrayList<>(people.peopleData);
		pd.allPickaxe = new ArrayList<>(people.pickaxeData);
		int peoInd = pd.peopleList.getSelectedIndex();
		int pickInd = pd.pickaxeList.getSelectedIndex();
		pd.peopleList.removeListSelectionListener(pd.peopleList.getListSelectionListeners()[0]);
		pd.peopleList.setListData(pd.allPeople.toArray(new People.PeopleData[0]));
		pd.peopleList.addListSelectionListener(e -> pd.peopleLabel.setText(pd.toHtml(pd.peopleList.getSelectedValue().list())));
		pd.peopleList.setSelectedIndex(peoInd);
		pd.pickaxeList.removeListSelectionListener(pd.pickaxeList.getListSelectionListeners()[0]);
		pd.pickaxeList.setListData(pd.allPickaxe.toArray(new People.PickaxeData[0]));
		pd.pickaxeList.addListSelectionListener(e -> pd.pickaxeLabel.setText(pd.toHtml(pd.pickaxeList.getSelectedValue().list())));
		pd.pickaxeList.setSelectedIndex(pickInd);
		pd.peopleLabel.setText(pd.toHtml(pd.peopleList.getSelectedValue().list()));
		pd.pickaxeLabel.setText(pd.toHtml(pd.pickaxeList.getSelectedValue().list()));
	}
	public static PrintStream getExportCrashReport() {
		Log.d("crash", "start create crash file");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		File crash = new File("miner/crash-report");
		Log.d("crash", "check dir");
		if(!crash.exists()) {
			crash.mkdir();
			Log.d("crash", "create dir");
		}
		Log.d("crash", "format file name");
		File f = new File("miner/crash-report/" + sdf.format(date) + ".crash");
		if(!f.exists()) {
			try {
				Log.d("crash", "create file");
				//noinspection ResultOfMethodCallIgnored
				f.createNewFile();
			}
			catch(IOException e) {
				Log.d("crash", "create file error with exception:" + e.toString());
				//創建檔案錯誤
				e.printStackTrace();
				//will throw NullPointException
				return null;
			}
		}
		try {
			Log.d("file", "get crash file with path:" + f.getPath());
			return new PrintStream(f);
		}
		catch(FileNotFoundException e) {
			//照理來說不會發生
			e.printStackTrace();
		}
		//will throw NullPointException
		return null;
	}
	
	public static void extract() throws IOException {
		File ext = getExtractFile();
		Log.i("extract", "path: " + ext.getAbsolutePath());
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(ext));
		writeToZip(zos, "miner/storage/machine.json", machine.storageMachine);
		writeToZip(zos, "miner/storage/mineral.json", mineral.storageMineral);
		writeToZip(zos, "miner/storage/people.json", people.storagePeople);
		writeToZip(zos, "miner/config.json", config.config);
		writeToZip(zos, "miner/map.json", map.map);
		writeToZip(zos, "miner/shop.json", shop.shop);
		zos.close();
		Log.i("extract", "extract successful");
		Background.throwMsg("匯出", "匯出成功，檔案位置: " + ext.getAbsolutePath());
	}
	private static void writeToZip(ZipOutputStream zos, String entryName, File file) throws IOException {
		Log.d("extract", entryName);
		ZipEntry ze = new ZipEntry(entryName);
		BufferedReader br = Files.newBufferedReader(file.toPath());
		zos.putNextEntry(ze);
		int c = br.read();
		while(c != -1) {
			zos.write(c);
			c = br.read();
		}
		br.close();
	}
	private static File getExtractFile() throws IOException {
		File directory = new File("./miner/extract");
		if(!directory.exists()) directory.mkdir();
		int extN = 0;
		File out = new File("miner/extract/extract.miner");
		while(out.exists()) {
			extN++;
			out = new File("miner/extract/extract(" + extN + ").miner");
		}
		out.createNewFile();
		return out;
	}
	
}
