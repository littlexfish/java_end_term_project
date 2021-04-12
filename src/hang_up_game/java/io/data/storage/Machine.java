package hang_up_game.java.io.data.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.Plugin;
import hang_up_game.java.io.data.Saveable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class Machine implements Saveable {
	
	private final File storageMachine = new File(getClass().getResource("/hang_up_game/files/storage/machine.json").toURI());
	private final JsonObject machineJson = new JsonStreamParser(new FileReader(storageMachine)).next().getAsJsonObject();
	private final LinkedList<Engine> engines = new LinkedList<>();
	private final LinkedList<Head> heads = new LinkedList<>();
	private final LinkedList<Battery> batteries = new LinkedList<>();
	private final LinkedList<Chest> chests = new LinkedList<>();
	private final LinkedList<Plugin> plugin = new LinkedList<>();
	
	public Machine() throws URISyntaxException, FileNotFoundException {
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
		machineJson.get("machine").getAsJsonArray().add(e.toJsonObject());
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
	
	public synchronized void addChest(Chest c) {
		if(getChestFromId(c.id) != null) throw new IllegalStateException("chest exist");
		chests.add(c);
		machineJson.get("chest").getAsJsonArray().add(c.toJsonObject());
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
	
	public static Engine getRandomEngine(int minStrong, int maxStrong) {
		Random r = new Random();
		int strong = minStrong + r.nextInt(maxStrong - minStrong + 1);
		int min = 0;
		int max = 0;
		String name = "";
		if(strong < 30) {
			name = "新手引擎";
			min = 1000;
			max = 5000;
		}
		else if(strong < 50) {
			name = "進階引擎";
			min = 3000;
			max = 7500;
		}
		else if(strong < 100) {
			name = "高級引擎";
			min = 5000;
			max = 10000;
		}
		else if(strong < 300) {
			name = "優秀引擎";
			min = 10000;
			max = 13000;
		}
		else if(strong < 500) {
			name = "大師引擎";
			min = 12000;
			max = 20000;
		}
		else {
			name = "究極引擎";
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
				name = "新手鑽頭";
				min = 1000;
				max = 5000;
				break;
			case 2:
				name = "進階鑽頭";
				min = 3000;
				max = 10000;
				break;
			case 3:
				name = "高級鑽頭";
				min = 5000;
				max = 12000;
				break;
			case 4:
				name = "優秀鑽頭";
				min = 10000;
				max = 30000;
				break;
			case 5:
				name = "大師鑽頭";
				min = 20000;
				max = 35000;
				break;
			case 6:
				name = "究極鑽頭";
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
		String name = "";
		if(battery < 10000) name = "新手電池";
		else if(battery < 15000) name = "進階電池";
		else if(battery < 30000) name = "高級電池";
		else if(battery < 50000) name = "優秀電池";
		else if(battery < 100000) name = "大師電池";
		else name = "究極電池";
		return new Battery(Battery.getEmptyId(), name, battery, 0);
	}
	public static Chest getRandomChest(int min, int max) {
		Random r = new Random();
		int space = min + r.nextInt(max - min + 1);
		String name = "";
		if(space < 1000) name = "新手箱子";
		else if(space < 2000) name = "進階箱子";
		else if(space < 5000) name = "高級箱子";
		else if(space < 10000) name = "優秀箱子";
		else if(space < 15000) name = "大師箱子";
		else name = "究極箱子";
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
