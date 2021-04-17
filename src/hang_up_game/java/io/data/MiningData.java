package hang_up_game.java.io.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import hang_up_game.java.io.data.storage.Machine;
import hang_up_game.java.io.data.storage.People;
import hang_up_game.java.pos.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MiningData implements Saveable {
	
	private final File miningData = new File(getClass().getResource("/hang_up_game/files/miningData.json").toURI());
	private final JsonArray miningDataJson = new JsonStreamParser(new FileReader(miningData)).next().getAsJsonArray();
	private final LinkedList<Machine> data = new LinkedList<>();
	
	public MiningData() throws FileNotFoundException, URISyntaxException {
		for(JsonElement je : miningDataJson) {
			JsonObject jo = je.getAsJsonObject();
			JsonObject part = jo.get("parts").getAsJsonObject();
			data.add(new Machine(jo.get("machineId").getAsInt(),
					new Machine.Parts(part.get("engineId").getAsInt(), part.get("headId").getAsInt(), part.get("batteryId").getAsInt(), part.get("chestId").getAsInt(), part.get("pluginId").getAsJsonArray())));
			
		}
	}
	
	/**
	 * @param id - the id of machine
	 * @return machine that id, or null didn't find any
	 */
	public synchronized Machine getMachineFromId(int id) {
		for(Machine m : data) {
			if(m.id == id) return m;
		}
		return null;
	}
	
	public synchronized Machine releaseMachine(int id) {
		Machine machine = null;
		for(Machine m : data) {
			if(m.id == id) {
				machine = m;
				break;
			}
		}
		if(machine != null) {
			data.remove(machine);
		}
		return machine;
	}
	
	public synchronized void addMachine(Machine m) {
		if(getMachineFromId(m.id) != null) throw new IllegalStateException("machine is exist");
		data.add(m);
		miningDataJson.add(m.toJsonObject());
	}
	
	public synchronized List<Machine> getData() {
		return data;
	}
	
	@Override
	public void save() throws IOException {
		Saveable.jsonSaver(miningDataJson.toString(), miningData);
	}
	
	public static class Machine {
		
		public static int getEmptyId() {
			int nowId = 1;
			while(true) {
				boolean contain = false;
				for(People.PickaxeData p : FileHolder.people.pickaxeData) {
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
		
		public final int id;
		public final Parts parts;
		
		public Machine(int i, Parts p) {
			id = i;
			parts = p;
		}
		
		public JsonObject toJsonObject() {
			JsonObject jo = new JsonObject();
			jo.addProperty("machineId", id);
			jo.add("parts", parts.toJsonObject());
			return jo;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			Machine machine = (Machine)o;
			return id == machine.id;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
		
		public static class Parts {
			
			public final int engineId;
			public final int headId;
			public final int batteryId;
			public final int chestId;
			private final int[] pluginIds;
			
			public Parts(int engine, int head, int battery, int chest, int ...plugin) {
				engineId = engine;
				headId = head;
				batteryId = battery;
				chestId = chest;
				pluginIds = plugin;
			}
			
			public Parts(int engine, int head, int battery, int chest, JsonArray plugin) {
				engineId = engine;
				headId = head;
				batteryId = battery;
				chestId = chest;
				int size = plugin.size();
				pluginIds = new int[size];
				for(int i = 0;i < size;i++) {
					pluginIds[i] = plugin.get(i).getAsInt();
				}
			}
			
			public int[] getPluginIds() {
				return pluginIds;
			}
			
			public JsonObject toJsonObject() {
				JsonObject jo = new JsonObject();
				jo.addProperty("engineId", engineId);
				jo.addProperty("headId", headId);
				jo.addProperty("batteryId", batteryId);
				jo.addProperty("chestId", chestId);
				JsonArray ja = new JsonArray(pluginIds.length);
				for(int i : pluginIds) {
					ja.add(i);
				}
				jo.add("pluginId", ja);
				return jo;
			}
			
		}
		
	}
	
}
