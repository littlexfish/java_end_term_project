package hang_up_game.java.io.data;

import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

public class Shop implements Saveable {
	
	private final File shop = new File(getClass().getResource("/hang_up_game/files/shop.json").toURI());
	private final JsonObject shopJson = new JsonStreamParser(new FileReader(shop)).next().getAsJsonObject();
	
	public Shop() throws URISyntaxException, FileNotFoundException {
	}
	
	public synchronized int[] getUnlockItems() {
		JsonArray ja = shopJson.get("unlockItemType").getAsJsonArray();
		int size = ja.size();
		int[] items = new int[size];
		for(int i = 0;i < size;i++) {
			items[i] = ja.get(i).getAsInt();
		}
		return items;
	}
	
	public synchronized int[] getBlueprints() {
		JsonArray ja = shopJson.get("bluePrintId").getAsJsonArray();
		int size = ja.size();
		int[] blueprints = new int[size];
		for(int i = 0;i < size;i++) {
			blueprints[i] = ja.get(i).getAsInt();
		}
		return blueprints;
	}
	
	public synchronized int getMoney() {
		return shopJson.get("money").getAsInt();
	}
	
	/**
	 * @param itemId - the id of item
	 * @return true if add successfully
	 */
	public synchronized boolean addUnlockItem(int itemId) {
		JsonArray ja = shopJson.get("unlockItemType").getAsJsonArray();
		boolean contain = ja.contains(new JsonPrimitive(itemId));
		if(!contain) {
			ja.add(itemId);
		}
		return !contain;
	}
	
	/**
	 * @param blueprintId - the id of blueprint
	 * @return true if add successfully
	 */
	public synchronized boolean addBlueprint(int blueprintId) {
		JsonArray ja = shopJson.get("bluePrintId").getAsJsonArray();
		boolean contain = ja.contains(new JsonPrimitive(blueprintId));
		if(!contain) {
			ja.add(blueprintId);
		}
		return !contain;
	}
	
	public synchronized void setMoney(int value) {
		shopJson.addProperty("money", value);
	}
	
	public synchronized void addMoney(int value) {
		setMoney(getMoney() + value);
	}
	
	@Override
	public void save() throws IOException {
		Saveable.jsonSaver(shopJson.toString(), shop);
	}
	
}
