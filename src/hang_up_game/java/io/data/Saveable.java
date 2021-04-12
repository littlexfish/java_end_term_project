package hang_up_game.java.io.data;

import com.google.gson.Gson;
import com.google.gson.JsonStreamParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public interface Saveable {
	
	void save() throws IOException;
	
	static void jsonSaver(String json, File file) throws IOException {
		json = new String(StandardCharsets.UTF_8.encode(StandardCharsets.ISO_8859_1.decode(ByteBuffer.wrap(json.getBytes()))).array());
		String pretty = new Gson().newBuilder().setPrettyPrinting().create().toJson(new JsonStreamParser(json).next());
		FileOutputStream fos = new FileOutputStream(file);
		for(char c : pretty.toCharArray()) {
			fos.write(c);
		}
		fos.close();
	}
	
}
