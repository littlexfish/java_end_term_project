package hang_up_game.java.io.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hang_up_game.java.io.data.storage.People;

import java.util.LinkedList;
import java.util.List;

public class MiningData {
	
	private final LinkedList<Machine> data = new LinkedList<>();
	
	public MiningData() {
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
		Machine machine = getMachineFromId(id);
		if(machine != null) {
			data.remove(machine);
		}
		return machine;
	}
	
	public synchronized void addMachine(Machine m) {
		if(getMachineFromId(m.id) != null) throw new IllegalStateException("machine is exist");
		data.add(m);
	}
	
	public synchronized List<Machine> getData() {
		return data;
	}
	
	public static class Machine {
		
		public static int getEmptyId() {
			int nowId = 1;
			while(true) {
				boolean contain = false;
				for(Machine p : FileHolder.miningData.getData()) {
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
