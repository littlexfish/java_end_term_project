package hang_up_game.java.io.data.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.MiningData;
import hang_up_game.java.io.data.Saveable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Machine implements Saveable {
	
	public final File storageMachine = new File("./miner/storage/machine.json");
	private final JsonObject machineJson = new JsonStreamParser(Files.newBufferedReader(storageMachine.toPath())).next().getAsJsonObject();
	private final LinkedList<Engine> engines = new LinkedList<>();
	private final LinkedList<Head> heads = new LinkedList<>();
	private final LinkedList<Battery> batteries = new LinkedList<>();
	private final LinkedList<Chest> chests = new LinkedList<>();
	private final LinkedList<Plugin> plugin = new LinkedList<>();
	
	public Machine() throws IOException {
		//engine
		{
			JsonArray ja = machineJson.get("engine").getAsJsonArray();
			for(JsonElement je : ja) {
				JsonObject jo = je.getAsJsonObject();
				engines.add(new Engine(jo.get("id").getAsInt(), jo.get("name").getAsString(), jo.get("strong").getAsInt(), jo.get("maxDamage").getAsInt(), jo.get("damage").getAsInt()));
			}
		}
		//head
		{
			JsonArray ja = machineJson.get("head").getAsJsonArray();
			for(JsonElement je : ja) {
				JsonObject jo = je.getAsJsonObject();
				heads.add(new Head(jo.get("id").getAsInt(), jo.get("name").getAsString(), jo.get("level").getAsInt(), jo.get("maxDamage").getAsInt(), jo.get("damage").getAsInt()));
			}
		}
		//battery
		{
			JsonArray ja = machineJson.get("battery").getAsJsonArray();
			for(JsonElement je : ja) {
				JsonObject jo = je.getAsJsonObject();
				batteries.add(new Battery(jo.get("id").getAsInt(), jo.get("name").getAsString(), jo.get("maxBattery").getAsInt(), jo.get("battery").getAsInt()));
			}
		}
		//chest
		{
			JsonArray ja = machineJson.get("chest").getAsJsonArray();
			for(JsonElement je : ja) {
				JsonObject jo = je.getAsJsonObject();
				chests.add(new Chest(jo.get("id").getAsInt(), jo.get("name").getAsString(), jo.get("maxSpace").getAsInt()));
			}
		}
		//plugin
		{
			JsonArray ja = machineJson.get("plugin").getAsJsonArray();
			for(JsonElement je : ja) {
				JsonObject jo = je.getAsJsonObject();
				plugin.add(new Plugin(jo.get("id").getAsInt(), jo.get("name").getAsString(), jo.get("count").getAsInt()));
			}
		}
		
	}
	
	public synchronized Engine getEngineFromId(int id) {
		for(Engine e : engines) {
			if(e.id == id) return e;
		}
		return null;
	}
	
	public synchronized Head getHeadFromId(int id) {
		for(Head h : heads) {
			if(h.id == id) return h;
		}
		return null;
	}
	
	public synchronized Battery getBatteryFromId(int id) {
		for(Battery b : batteries) {
			if(b.id == id) return b;
		}
		return null;
	}
	
	public synchronized Chest getChestFromId(int id) {
		for(Chest c : chests) {
			if(c.id == id) return c;
		}
		return null;
	}
	
	public synchronized int getPluginCountFromId(int id) {
		for(Plugin p : plugin) {
			if(p.id == id) return p.count;
		}
		return -1;
	}
	
	public synchronized void addEngine(Engine e) {
		if(getEngineFromId(e.id) != null) {
			setEngine(e);
			return;
		}
		engines.add(e);
		machineJson.get("engine").getAsJsonArray().add(e.toJsonObject());
	}
	
	public synchronized void setEngine(Engine e) {
		JsonArray ja = machineJson.get("engine").getAsJsonArray();
		for(int i = 0;i < ja.size();i++) {
			JsonObject jo = ja.get(i).getAsJsonObject();
			if(jo.get("id").getAsInt() == e.id) {
				engines.remove(i);
				engines.add(e);
				ja.remove(i);
				ja.add(e.toJsonObject());
				break;
			}
		}
	}
	
	public synchronized void removeEngine(int engineId) {
		JsonArray ja = machineJson.get("engine").getAsJsonArray();
		for(int i = 0;i < ja.size();i++) {
			JsonObject jo = ja.get(i).getAsJsonObject();
			if(jo.get("id").getAsInt() == engineId) {
				engines.remove(i);
				ja.remove(i);
				break;
			}
		}
	}
	
	public synchronized void addHead(Head h) {
		if(getHeadFromId(h.id) != null) {
			setHead(h);
			return;
		}
		heads.add(h);
		machineJson.get("head").getAsJsonArray().add(h.toJsonObject());
	}
	
	public synchronized void setHead(Head h) {
		JsonArray ja = machineJson.get("head").getAsJsonArray();
		for(int i = 0;i < ja.size();i++) {
			JsonObject jo = ja.get(i).getAsJsonObject();
			if(jo.get("id").getAsInt() == h.id) {
				heads.remove(i);
				heads.add(h);
				ja.remove(i);
				ja.add(h.toJsonObject());
				break;
			}
		}
	}
	
	public synchronized void removeHead(int headId) {
		JsonArray ja = machineJson.get("head").getAsJsonArray();
		for(int i = 0;i < ja.size();i++) {
			JsonObject jo = ja.get(i).getAsJsonObject();
			if(jo.get("id").getAsInt() == headId) {
				heads.remove(i);
				ja.remove(i);
				break;
			}
		}
	}
	
	public synchronized void addBattery(Battery b) {
		if(getBatteryFromId(b.id) != null) {
			setBattery(b);
			return;
		}
		batteries.add(b);
		machineJson.get("battery").getAsJsonArray().add(b.toJsonObject());
	}
	
	public synchronized void setBattery(Battery b) {
		JsonArray ja = machineJson.get("battery").getAsJsonArray();
		for(int i = 0;i < ja.size();i++) {
			JsonObject jo = ja.get(i).getAsJsonObject();
			if(jo.get("id").getAsInt() == b.id) {
				batteries.remove(i);
				batteries.add(b);
				ja.remove(i);
				ja.add(b.toJsonObject());
				break;
			}
		}
	}
	
	public synchronized void removeBattery(int batteryId) {
		JsonArray ja = machineJson.get("battery").getAsJsonArray();
		for(int i = 0;i < ja.size();i++) {
			JsonObject jo = ja.get(i).getAsJsonObject();
			if(jo.get("id").getAsInt() == batteryId) {
				batteries.remove(i);
				ja.remove(i);
				break;
			}
		}
	}
	
	public synchronized void addChest(Chest c) {
		if(getChestFromId(c.id) != null) {
			setChest(c);
			return;
		}
		chests.add(c);
		machineJson.get("chest").getAsJsonArray().add(c.toJsonObject());
	}
	
	public synchronized void setChest(Chest c) {
		JsonArray ja = machineJson.get("chest").getAsJsonArray();
		for(int i = 0;i < ja.size();i++) {
			JsonObject jo = ja.get(i).getAsJsonObject();
			if(jo.get("id").getAsInt() == c.id) {
				chests.remove(i);
				chests.add(c);
				ja.remove(i);
				ja.add(c.toJsonObject());
				break;
			}
		}
	}
	
	public synchronized void removeChest(int chestId) {
		JsonArray ja = machineJson.get("chest").getAsJsonArray();
		for(int i = 0;i < ja.size();i++) {
			JsonObject jo = ja.get(i).getAsJsonObject();
			if(jo.get("id").getAsInt() == chestId) {
				chests.remove(i);
				ja.remove(i);
				break;
			}
		}
	}
	
	public synchronized void setPluginCount(int id, int count) {
		if(count < 0) throw new IllegalArgumentException("count under 0");
		Plugin pl = null;
		for(Plugin p : plugin) {
			if(p.id == id) pl = p;
		}
		if(pl != null) {
			pl.count = count;
			JsonArray ja = machineJson.get("plugin").getAsJsonArray();
			for(JsonElement je : ja) {
				if(je.getAsJsonObject().get("id").getAsInt() == id) {
					je.getAsJsonObject().addProperty("count", count);
					break;
				}
			}
		}
		else {
			createPlugin(id, count);
		}
	}
	
	private synchronized void createPlugin(int id, int count) {
		Plugin p = new Plugin(id, hang_up_game.java.io.data.Plugin.getPluginFromId(id).name(), count);
		plugin.add(p);
		machineJson.get("plugin").getAsJsonArray().add(p.toJsonObject());
	}
	
	public synchronized ArrayList<Item> getAllPart() {
		List<MiningData.Machine> data = FileHolder.miningData.getData();
		ArrayList<Item> itemsNotOnline = new ArrayList<>();
		ES:
		for(Engine e : engines) {
			for(MiningData.Machine datum : data) {
				if(e.id == datum.parts.engineId) {
					continue ES;
				}
			}
			itemsNotOnline.add(e);
		}
		HS:
		for(Head h : heads) {
			for(MiningData.Machine datum : data) {
				if(h.id == datum.parts.headId) {
					continue HS;
				}
			}
			itemsNotOnline.add(h);
		}
		BS:
		for(Battery b : batteries) {
			for(MiningData.Machine datum : data) {
				if(b.id == datum.parts.batteryId) {
					continue BS;
				}
			}
			itemsNotOnline.add(b);
		}
		CS:
		for(Chest c : chests) {
			for(MiningData.Machine datum : data) {
				if(c.id == datum.parts.chestId) {
					continue CS;
				}
			}
			itemsNotOnline.add(c);
		}
		ArrayList<Plugin> newPlugin = new ArrayList<>(plugin);
		for(MiningData.Machine datum : data) {
			int[] ps = datum.parts.getPluginIds();
			for(int p : ps) {
				int index = plugin.indexOf(Plugin.getCompareInstance(p));
				newPlugin.get(index).setCount(newPlugin.get(index).getCount() - 1);
			}
		}
		itemsNotOnline.addAll(newPlugin);
		return itemsNotOnline;
	}
	
	public synchronized ArrayList<Battery> getBatteryNeedCharge() {
		List<MiningData.Machine> data = FileHolder.miningData.getData();
		ArrayList<Battery> batteryCharge = new ArrayList<>();
		BS:
		for(Battery b : batteries) {
			for(MiningData.Machine datum : data) {
				if(b.id == datum.parts.batteryId) {
					continue BS;
				}
			}
			if(b.battery < b.maxBattery) {
				batteryCharge.add(b);
			}
		}
		return batteryCharge;
	}
	
	public synchronized List<Engine> getUsableEngine() {
		LinkedList<Engine> es = new LinkedList<>();
		List<MiningData.Machine> ma = FileHolder.miningData.getData();
		Engine:
		for(Engine e : engines) {
			for(MiningData.Machine m : ma) {
				if(m.parts.engineId == e.id) {
					continue Engine;
				}
			}
			es.add(e);
		}
		return es;
	}
	public synchronized List<Head> getUsableHead() {
		LinkedList<Head> hs = new LinkedList<>();
		List<MiningData.Machine> ma = FileHolder.miningData.getData();
		Head:
		for(Head h : heads) {
			for(MiningData.Machine m : ma) {
				if(m.parts.headId == h.id) {
					continue Head;
				}
			}
			hs.add(h);
		}
		return hs;
	}
	public synchronized List<Battery> getUsableBattery() {
		LinkedList<Battery> bs = new LinkedList<>();
		List<MiningData.Machine> ma = FileHolder.miningData.getData();
		Battery:
		for(Battery b : batteries) {
			for(MiningData.Machine m : ma) {
				if(m.parts.batteryId == b.id) {
					continue Battery;
				}
			}
			bs.add(b);
		}
		return bs;
	}
	public synchronized List<Chest> getUsableChest() {
		LinkedList<Chest> cs = new LinkedList<>();
		List<MiningData.Machine> ma = FileHolder.miningData.getData();
		Chest:
		for(Chest c : chests) {
			for(MiningData.Machine m : ma) {
				if(m.parts.batteryId == c.id) {
					continue Chest;
				}
			}
			cs.add(c);
		}
		return cs;
	}
	public synchronized List<hang_up_game.java.io.data.Plugin> getUsablePlugin() {
		Map<hang_up_game.java.io.data.Plugin, Integer> plugins = new EnumMap<>(hang_up_game.java.io.data.Plugin.class);
		List<MiningData.Machine> ma = FileHolder.miningData.getData();
		for(MiningData.Machine m : ma) {
			int[] ps = m.parts.getPluginIds();
			if(ps.length <= 0) {
				continue;
			}
			for(int p : ps) {
				plugins.put(hang_up_game.java.io.data.Plugin.getPluginFromId(p), plugins.getOrDefault(hang_up_game.java.io.data.Plugin.getPluginFromId(p), plugin.get(plugin.indexOf(Plugin.getCompareInstance(p))).count - 1));
			}
		}
		LinkedList<hang_up_game.java.io.data.Plugin> ps = new LinkedList<>();
		for(hang_up_game.java.io.data.Plugin pl : plugins.keySet()) {
			if(plugins.get(pl) > 0) {
				ps.add(pl);
			}
		}
		return ps;
	}
	
	public static Engine getRandomEngine(int minStrong, int maxStrong) {
		Random r = new Random();
		int strong = minStrong + r.nextInt(maxStrong - minStrong + 1);
		int min;
		int max;
		String name;
		if(strong < 30) {
			name = "????????????";
			min = 1000;
			max = 5000;
		}
		else if(strong < 50) {
			name = "????????????";
			min = 3000;
			max = 7500;
		}
		else if(strong < 100) {
			name = "????????????";
			min = 5000;
			max = 10000;
		}
		else if(strong < 300) {
			name = "????????????";
			min = 10000;
			max = 13000;
		}
		else if(strong < 500) {
			name = "????????????";
			min = 12000;
			max = 20000;
		}
		else {
			name = "????????????";
			min = 15000;
			max = 30000;
		}
		int damage = min + r.nextInt(max - min + 1);
		return new Engine(Engine.getEmptyId(), name, strong, damage, 0);
	}
	public static Head getRandomHead(int minLevel, int maxLevel) {
		Random r = new Random();
		int level = minLevel + r.nextInt(maxLevel - minLevel + 1);
		int min = 0;
		int max = 0;
		String name = "";
		switch(level) {
			case 1:
				name = "????????????";
				min = 1000;
				max = 5000;
				break;
			case 2:
				name = "????????????";
				min = 3000;
				max = 10000;
				break;
			case 3:
				name = "????????????";
				min = 5000;
				max = 12000;
				break;
			case 4:
				name = "????????????";
				min = 10000;
				max = 30000;
				break;
			case 5:
				name = "????????????";
				min = 20000;
				max = 35000;
				break;
			case 6:
				name = "????????????";
				min = 30000;
				max = 50000;
				break;
		}
		int damage = min + r.nextInt(max - min + 1);
		return new Head(Head.getEmptyId(), name, level, damage, 0);
		
	}
	public static Battery getRandomBattery(int min, int max) {
		Random r = new Random();
		int battery = min + r.nextInt(max - min + 1);
		String name;
		if(battery < 10000) name = "????????????";
		else if(battery < 15000) name = "????????????";
		else if(battery < 30000) name = "????????????";
		else if(battery < 50000) name = "????????????";
		else if(battery < 100000) name = "????????????";
		else name = "????????????";
		return new Battery(Battery.getEmptyId(), name, battery, 0);
	}
	public static Chest getRandomChest(int min, int max) {
		Random r = new Random();
		int space = min + r.nextInt(max - min + 1);
		String name;
		if(space < 1000) name = "????????????";
		else if(space < 2000) name = "????????????";
		else if(space < 5000) name = "????????????";
		else if(space < 10000) name = "????????????";
		else if(space < 15000) name = "????????????";
		else name = "????????????";
		return new Chest(Chest.getEmptyId(), name, space);
	}
	public static Plugin getRandomPlugin() {
		Random r = new Random();
		int id = r.nextInt(hang_up_game.java.io.data.Plugin.getMaxId() + 1);
		hang_up_game.java.io.data.Plugin pl = hang_up_game.java.io.data.Plugin.getPluginFromId(id);
		return new Plugin(id, pl.name, 1);
	}
	
	@Override
	public void save() throws IOException {
		Saveable.jsonSaver(machineJson.toString(), storageMachine);
	}
	
	public static class Engine extends Item {
		
		public static int getEmptyId() {
			int nowId = 1;
			while(true) {
				boolean contain = false;
				for(Machine.Engine p : FileHolder.machine.engines) {
					if(p.id == nowId) {
						contain = true;
						break;
					}
				}
				if(contain) {
					nowId++;
					continue;
				}
				return nowId;
			}
		}
		public static Engine quickData(Engine e, int damage) {
			return new Engine(e.id, e.name, e.strong, e.maxDamage, damage);
		}
		
		public final int strong;
		public final int maxDamage;
		private int damage;
		
		public Engine(int i, String n, int s, int m, int d) {
			super(i, n);
			strong = s;
			maxDamage = m;
			damage = d;
		}
		
		public int getDamage() {
			return damage;
		}
		
		public void setDamage(int damage) {
			if(damage < 0 && damage > maxDamage) throw new IllegalArgumentException("damage must in 0~" + maxDamage);
			this.damage = damage;
		}
		
		public JsonObject toJsonObject() {
			JsonObject jo = new JsonObject();
			jo.addProperty("id", id);
			jo.addProperty("name", name);
			jo.addProperty("strong", strong);
			jo.addProperty("maxDamage", maxDamage);
			jo.addProperty("damage", damage);
			return jo;
		}
		
		public String list() {
			return "??????:" + name + "\n??????:" + strong + "\n?????????:" + damage + "/" + maxDamage;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			Engine engine = (Engine)o;
			return id == engine.id;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
		@Override
		public String toString() {
			return name;
		}
	}
	public static class Head extends Item {
		
		public static int getEmptyId() {
			int nowId = 1;
			while(true) {
				boolean contain = false;
				for(Machine.Head p : FileHolder.machine.heads) {
					if(p.id == nowId) {
						contain = true;
						break;
					}
				}
				if(contain) {
					nowId++;
					continue;
				}
				return nowId;
			}
		}
		public static Head quickData(Head h, int damage) {
			return new Head(h.id, h.name, h.level, h.maxDamage, damage);
		}
		
		public final int level;
		public final int maxDamage;
		private int damage;
		
		public Head(int i, String n, int l, int m, int d) {
			super(i, n);
			level = l;
			maxDamage = m;
			damage = d;
		}
		
		public int getDamage() {
			return damage;
		}
		
		public void setDamage(int damage) {
			if(damage < 0 && damage > maxDamage) throw new IllegalArgumentException("damage must in 0~" + maxDamage);
			this.damage = damage;
		}
		
		public JsonObject toJsonObject() {
			JsonObject jo = new JsonObject();
			jo.addProperty("id", id);
			jo.addProperty("name", name);
			jo.addProperty("level", level);
			jo.addProperty("maxDamage", maxDamage);
			jo.addProperty("damage", damage);
			return jo;
		}
		
		public String list() {
			return "??????:" + name + "\n????????????:" + level + "\n?????????:" + damage + "/" + maxDamage;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			Head head = (Head)o;
			return id == head.id;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
		@Override
		public String toString() {
			return name;
		}
	}
	public static class Battery extends Item {
		
		public static int getEmptyId() {
			int nowId = 1;
			while(true) {
				boolean contain = false;
				for(Machine.Battery p : FileHolder.machine.batteries) {
					if(p.id == nowId) {
						contain = true;
						break;
					}
				}
				if(contain) {
					nowId++;
					continue;
				}
				return nowId;
			}
		}
		public static Battery quickData(Battery b, int battery) {
			return new Battery(b.id, b.name, b.maxBattery, battery);
		}
		
		public final int maxBattery;
		private int battery;
		
		public Battery(int i, String n, int m, int b) {
			super(i, n);
			maxBattery = m;
			battery = b;
		}
		
		public int getBattery() {
			return battery;
		}
		
		public void setBattery(int battery) {
			if(battery < 0 && battery > maxBattery) throw new IllegalArgumentException("battery must in 0~" + maxBattery);
			this.battery = battery;
		}
		
		public JsonObject toJsonObject() {
			JsonObject jo = new JsonObject();
			jo.addProperty("id", id);
			jo.addProperty("name", name);
			jo.addProperty("maxBattery", maxBattery);
			jo.addProperty("battery", battery);
			return jo;
		}
		
		public String list() {
			return "??????:" + name + "\n??????:" + battery + "/" + maxBattery;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			Battery battery = (Battery)o;
			return id == battery.id;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
		@Override
		public String toString() {
			return name;
		}
	}
	public static class Chest extends Item {
		
		public static int getEmptyId() {
			int nowId = 1;
			while(true) {
				boolean contain = false;
				for(Machine.Chest p : FileHolder.machine.chests) {
					if(p.id == nowId) {
						contain = true;
						break;
					}
				}
				if(contain) {
					nowId++;
					continue;
				}
				return nowId;
			}
		}
		
		public final int maxSpace;
		
		public Chest(int i, String n, int m) {
			super(i, n);
			maxSpace = m;
		}
		
		public JsonObject toJsonObject() {
			JsonObject jo = new JsonObject();
			jo.addProperty("id", id);
			jo.addProperty("name", name);
			jo.addProperty("maxSpace", maxSpace);
			return jo;
		}
		
		public String list() {
			return "??????:" + name + "\n??????:" + maxSpace;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			Chest chest = (Chest)o;
			return id == chest.id;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
		@Override
		public String toString() {
			return name;
		}
	}
	public static class Plugin extends Item {
		
		public static Plugin getCompareInstance(int id) {
			return new Plugin(id, "", 0);
		}
		
		private int count;
		
		public Plugin(int i, String n, int c) {
			super(i, n);
			count = c;
		}
		
		public int getCount() {
			return count;
		}
		
		public void setCount(int count) {
			if(count < 0) throw new IllegalArgumentException("count under 0");
			this.count = count;
		}
		
		public JsonObject toJsonObject() {
			JsonObject jo = new JsonObject();
			jo.addProperty("id", id);
			jo.addProperty("name", name);
			jo.addProperty("count", count);
			return jo;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			Plugin plugin = (Plugin)o;
			return id == plugin.id;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(id);
		}
		@Override
		public String toString() {
			return name;
		}
	}

}
