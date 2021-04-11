package hang_up_game.java.io.data.storage;

import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import hang_up_game.java.io.data.Saveable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

public class Mineral implements Saveable {
	
	private final File storageMineral = new File(getClass().getResource("/hang_up_game/files/storage/mineral.json").toURI());
	private final JsonObject mineralJson = new JsonStreamParser(new FileReader(storageMineral)).next().getAsJsonObject();
	
	public Mineral() throws URISyntaxException, FileNotFoundException { }
	
	public int getSpace() {
		return mineralJson.get("space").getAsInt();
	}
	
	public int getMineral(hang_up_game.java.game.Mineral m) {
		return mineralJson.get(m.name()).getAsInt();
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
