package hang_up_game.java.io.data.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import hang_up_game.java.io.data.Saveable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Objects;

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
				engines.add(new Engine(jo.get("id").getAsInt(), jo.get("strong").getAsInt(), jo.get("maxDamage").getAsInt(), jo.get("damage").getAsInt()));
			}
		}
		//head
		{
			JsonArray ja = machineJson.get("head").getAsJsonArray();
			for(JsonElement je : ja) {
				JsonObject jo = je.getAsJsonObject();
				heads.add(new Head(jo.get("id").getAsInt(), jo.get("level").getAsInt(), jo.get("maxDamage").getAsInt(), jo.get("damage").getAsInt()));
			}
		}
		//battery
		{
			JsonArray ja = machineJson.get("battery").getAsJsonArray();
			for(JsonElement je : ja) {
				JsonObject jo = je.getAsJsonObject();
				batteries.add(new Battery(jo.get("id").getAsInt(), jo.get("maxBattery").getAsInt(), jo.get("battery").getAsInt()));
			}
		}
		//chest
		{
			JsonArray ja = machineJson.get("chest").getAsJsonArray();
			for(JsonElement je : ja) {
				JsonObject jo = je.getAsJsonObject();
				chests.add(new Chest(jo.get("id").getAsInt(), jo.get("maxSpace").getAsInt()));
			}
		}
		//plugin
		{
			JsonArray ja = machineJson.get("plugin").getAsJsonArray();
			for(JsonElement je : ja) {
				JsonObject jo = je.getAsJsonObject();
				plugin.add(new Plugin(jo.get("id").getAsInt(), jo.get("count").getAsInt()));
			}
		}
		
	}
	
	public Engine getEngineFromId(int id) {
		for(Engine e : engines) {
			if(e.id == id) return e;
		}
		return null;
	}
	
	public Head getHeadFromId(int id) {
		for(Head h : heads) {
			if(h.id == id) return h;
		}
		return null;
	}
	
	public Battery getBatteryFromId(int id) {
		for(Battery b : batteries) {
			if(b.id == id) return b;
		}
		return null;
	}
	
	public Chest getChestFromId(int id) {
		for(Chest c : chests) {
			if(c.id == id) return c;
		}
		return null;
	}
	
	public int getPluginCountFromId(int id) {
		for(Plugin p : plugin) {
			if(p.id == id) return p.count;
		}
		return -1;
	}
	
	public void addEngine(Engine e) {
		if(getEngineFromId(e.id) != null) throw new IllegalStateException("engine exist");
		engines.add(e);
		machineJson.get("machine").getAsJsonArray().add(e.toJsonObject());
	}
	
	public void addHead(Head h) {
		if(getHeadFromId(h.id) != null) throw new IllegalStateException("head exist");
		heads.add(h);
		machineJson.get("head").getAsJsonArray().add(h.toJsonObject());
	}
	
	public void addBattery(Battery b) {
		if(getBatteryFromId(b.id) != null) throw new IllegalStateException("battery exist");
		batteries.add(b);
		machineJson.get("battery").getAsJsonArray().add(b.toJsonObject());
	}
	
	public void getChest(Chest c) {
		if(getChestFromId(c.id) != null) throw new IllegalStateException("chest exist");
		chests.add(c);
		machineJson.get("chest").getAsJsonArray().add(c.toJsonObject());
	}
	
	public void setPluginCount(int id, int count) {
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
	
	private void createPlugin(int id, int count) {
		Plugin p = new Plugin(id, count);
		plugin.add(p);
		machineJson.get("plugin").getAsJsonArray().add(p.toJsonObject());
	}
	
	@Override
	public void save() throws IOException {
		Saveable.jsonSaver(machineJson.toString(), storageMachine);
	}
	
	public static class Engine {
		
		public final int id;
		public final int strong;
		public final int maxDamage;
		private int damage;
		
		public Engine(int i, int s, int m, int d) {
			id = i;
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
	}
	public static class Head {
		
		public final int id;
		public final int level;
		public final int maxDamage;
		private int damage;
		
		public Head(int i, int l, int m, int d) {
			id = i;
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
	}
	public static class Battery {
		
		public final int id;
		public final int maxBattery;
		private int battery;
		
		public Battery(int i, int m, int b) {
			id = i;
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
	}
	public static class Chest {
		
		public final int id;
		public final int maxSpace;
		
		public Chest(int i, int m) {
			id = i;
			maxSpace = m;
		}
		
		public JsonObject toJsonObject() {
			JsonObject jo = new JsonObject();
			jo.addProperty("id", id);
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
	}
	public static class Plugin {
		
		public final int id;
		private int count;
		
		public Plugin(int i, int c) {
			id = i;
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
	}

}
