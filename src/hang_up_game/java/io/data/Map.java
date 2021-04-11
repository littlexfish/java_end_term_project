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
import java.net.URISyntaxException;

public class Map implements Saveable {
	
	private final File map = new File(getClass().getResource("/hang_up_game/files/map.json").toURI());
	private final JsonArray mapJson = new JsonStreamParser(new FileReader(map)).next().getAsJsonArray();
	
	public Map() throws URISyntaxException, FileNotFoundException {
	}
	
	public int getLastChestCount(int chunkX, int chunkY) {
		return getLastChestCount(new Chunk(chunkX, chunkY));
	}
	public int getLastChestCount(Chunk chunk) {
		for(JsonElement je : mapJson) {
			JsonObject jo = je.getAsJsonObject();
			if(jo.get("chunkX").getAsInt() == chunk.X && jo.get("chunkY").getAsInt() == chunk.Y) {
				return jo.get("lastChest").getAsInt();
			}
		}
		return -1;
	}
	
	public void setLastChestCount(Chunk chunk, int count) {
		for(JsonElement je : mapJson) {
			JsonObject jo = je.getAsJsonObject();
			if(jo.get("chunkX").getAsInt() == chunk.X && jo.get("chunkY").getAsInt() == chunk.Y) {
				jo.addProperty("lastChest", count);
				return;
			}
		}
		createNewMapData(chunk, count);
	}
	
	private void createNewMapData(Chunk chunk, int count) {
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
