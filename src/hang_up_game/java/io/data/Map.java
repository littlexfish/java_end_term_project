package hang_up_game.java.io.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import hang_up_game.java.pos.Chunk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class Map implements Saveable {
	
	public final File map = new File("./miner/map.json");
	private final JsonArray mapJson = new JsonStreamParser(Files.newBufferedReader(map.toPath())).next().getAsJsonArray();
	
	public Map() throws IOException {
	}
	
	public synchronized int getLastChestCount(int chunkX, int chunkY) {
		return getLastChestCount(new Chunk(chunkX, chunkY));
	}
	public synchronized int getLastChestCount(Chunk chunk) {
		for(JsonElement je : mapJson) {
			JsonObject jo = je.getAsJsonObject();
			if(jo.get("chunkX").getAsInt() == chunk.X && jo.get("chunkY").getAsInt() == chunk.Y) {
				return jo.get("lastChest").getAsInt();
			}
		}
		return -1;
	}
	
	public synchronized void setLastChestCount(Chunk chunk, int count) {
		for(JsonElement je : mapJson) {
			JsonObject jo = je.getAsJsonObject();
			if(jo.get("chunkX").getAsInt() == chunk.X && jo.get("chunkY").getAsInt() == chunk.Y) {
				jo.addProperty("lastChest", count);
				return;
			}
		}
		createNewMapData(chunk, count);
	}
	
	private synchronized void createNewMapData(Chunk chunk, int count) {
		JsonObject jo = new JsonObject();
		jo.addProperty("chunkX", chunk.X);
		jo.addProperty("chunkY", chunk.Y);
		jo.addProperty("lastChest", count);
		mapJson.add(jo);
	}
	
	@Override
	public void save() throws IOException {
		Saveable.jsonSaver(mapJson.toString(), map);
	}
	
}
