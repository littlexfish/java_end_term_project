package hang_up_game.java.io.data.storage;

import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import hang_up_game.java.io.data.Saveable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.EnumMap;

public class Mineral implements Saveable {
	
	public final File storageMineral = new File("./miner/storage/mineral.json");
	private final JsonObject mineralJson = new JsonStreamParser(Files.newBufferedReader(storageMineral.toPath())).next().getAsJsonObject();
	
	public Mineral() throws IOException { }
	
	public int getSpace() {
		return mineralJson.get("space").getAsInt();
	}
	
	public int getMineral(hang_up_game.java.game.Mineral m) {
		return mineralJson.get(m.name()).getAsInt();
	}
	
	public EnumMap<hang_up_game.java.game.Mineral, Integer> getAllMineral() {
		EnumMap<hang_up_game.java.game.Mineral, Integer> minerals = new EnumMap<>(hang_up_game.java.game.Mineral.class);
		for(hang_up_game.java.game.Mineral m : hang_up_game.java.game.Mineral.values()) {
			minerals.put(m, getMineral(m));
		}
		return minerals;
	}
	
	public void setSpace(int value) {
		if(value < 0) throw new IllegalArgumentException("count must over 0");
		mineralJson.addProperty("space", value);
	}
	
	public void setMineral(hang_up_game.java.game.Mineral m, int value) {
		if(value < 0) throw new IllegalArgumentException("count must over 0");
		mineralJson.addProperty(m.name(), value);
	}
	
	@Override
	public void save() throws IOException {
		Saveable.jsonSaver(mineralJson.toString(), storageMineral);
	}
	
}
