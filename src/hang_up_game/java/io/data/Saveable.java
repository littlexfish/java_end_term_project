package hang_up_game.java.io.data;

import com.google.gson.Gson;
import com.google.gson.JsonStreamParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public interface Saveable {
	
	void save() throws IOException;
	
	static void jsonSaver(String json, File file) throws IOException {
		json = new String(json.getBytes(Charset.defaultCharset()));
		String to = new Gson().toJson(new JsonStreamParser(json).next());
		BufferedWriter bw = Files.newBufferedWriter(file.toPath());
		bw.write(to);
		bw.close();
	}
	
}
