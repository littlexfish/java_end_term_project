package hang_up_game.java.io.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

public class Config implements Saveable {
	
	private final File config = new File(getClass().getResource("/hang_up_game/files/config.json").toURI());
	private final JsonElement configJson = new JsonStreamParser(new FileReader(config)).next();
	
	public Config() throws URISyntaxException, FileNotFoundException {
	}
	
	/**
	 *
	 * @param key - can use "/" go to sub config
	 * @param clazz - type need return
	 * @param <T> - what type
	 */
	public <T> T getConfig(String key, Class<T> clazz) {
		String[] slice = key.split("/");
		JsonElement je = configJson;
		for(String k : slice) {
			je = je.getAsJsonObject().get(k);
		}
		if(clazz == Boolean.class) {
			return (T)Boolean.valueOf(je.getAsBoolean());
		}
		if(clazz == Integer.class) {
			return (T)Integer.valueOf(je.getAsInt());
		}
		if(clazz == Long.class) {
			return (T)Long.valueOf(je.getAsLong());
		}
		return null;
	}
	
	public <T> void setConfig(String key, T value) {
		String[] slice = key.split("/");
		JsonElement je = configJson;
		for(int i = 0;i < slice.length - 1;i++) {
			je = je.getAsJsonObject().get(slice[i]);
		}
		if(value instanceof Boolean) {
			je.getAsJsonObject().addProperty(slice[slice.length - 1], (Boolean)value);
		}
		if(value instanceof Integer) {
			je.getAsJsonObject().addProperty(slice[slice.length - 1], (Integer)value);
		}
	}
	
	@Override
	public void save() throws IOException {
		Saveable.jsonSaver(configJson.toString(), config);
	}
	
}
