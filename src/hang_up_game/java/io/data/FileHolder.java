package hang_up_game.java.io.data;

import hang_up_game.java.game.Blueprint;
import hang_up_game.java.game.Blueprints;
import hang_up_game.java.io.data.storage.Item;
import hang_up_game.java.io.data.storage.Machine;
import hang_up_game.java.io.data.storage.Mineral;
import hang_up_game.java.io.data.storage.People;
import hang_up_game.java.pos.Block;
import hang_up_game.java.window.people.PeopleDetail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

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
		pd.peopleList.addListSelectionListener(e -> {
			pd.peopleLabel.setText(pd.toHtml(pd.peopleList.getSelectedValue().list()));
		});
		pd.peopleList.setSelectedIndex(peoInd);
		pd.pickaxeList.removeListSelectionListener(pd.pickaxeList.getListSelectionListeners()[0]);
		pd.pickaxeList.setListData(pd.allPickaxe.toArray(new People.PickaxeData[0]));
		pd.pickaxeList.addListSelectionListener(e -> {
			pd.pickaxeLabel.setText(pd.toHtml(pd.pickaxeList.getSelectedValue().list()));
		});
		pd.pickaxeList.setSelectedIndex(pickInd);
		pd.peopleLabel.setText(pd.toHtml(pd.peopleList.getSelectedValue().list()));
		pd.pickaxeLabel.setText(pd.toHtml(pd.pickaxeList.getSelectedValue().list()));
	}
	
}
