package hang_up_game.java.io.data.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import hang_up_game.java.io.data.Saveable;

import javax.xml.parsers.SAXParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

public class People implements Saveable {
	
	private final File storagePeople = new File(getClass().getResource("/hang_up_game/files/storage/people.json").toURI());
	private final JsonObject peopleJson = new JsonStreamParser(new FileReader(storagePeople)).next().getAsJsonObject();
	private final LinkedList<PeopleData> peopleData = new LinkedList<>();
	private final LinkedList<PickaxeData> pickaxeData = new LinkedList<>();
	private final LinkedList<BagData> bagData = new LinkedList<>();
	
	public People() throws URISyntaxException, FileNotFoundException {
		//people
		{
			JsonArray ja = peopleJson.get("people").getAsJsonArray();
			for(JsonElement je : ja) {
				JsonObject jo = je.getAsJsonObject();
				peopleData.add(new PeopleData(jo.get("id").getAsInt(), jo.get("name").getAsString(), jo.get("maxStamina").getAsInt(),
						jo.get("lastStamina").getAsInt(), jo.get("strong").getAsInt(), jo.get("skillId").getAsInt(), jo.get("skillValue").getAsJsonArray()));
			}
		}
		//pickaxe
		{
			JsonArray ja = peopleJson.get("pickaxe").getAsJsonArray();
			for(JsonElement je : ja) {
				JsonObject jo = je.getAsJsonObject();
				pickaxeData.add(new PickaxeData(jo.get("id").getAsInt(), jo.get("level").getAsInt(), jo.get("maxDamage").getAsInt(), jo.get("damage").getAsInt()));
			}
		}
		//bag
		{
			JsonArray ja = peopleJson.get("bag").getAsJsonArray();
			for(JsonElement je : ja) {
				JsonObject jo = je.getAsJsonObject();
				bagData.add(new BagData(jo.get("id").getAsInt(), jo.get("maxSpace").getAsInt()));
			}
		}
	}
	
	/**
	 * @param id - the id of people data
	 * @return people data that id, or null didn't find any
	 */
	public PeopleData getPeopleFromId(int id) {
		for(PeopleData p : peopleData) {
			if(p.id == id) return p;
		}
		return null;
	}
	
	/**
	 * @param id - the id of pickaxe
	 * @return pickaxe that id, or null didn't find any
	 */
	public PickaxeData getPickaxeFromId(int id) {
		for(PickaxeData p : pickaxeData) {
			if(p.id == id) return p;
		}
		return null;
	}
	
	/**
	 * @param id - the id of bag
	 * @return bag that id, or null didn't find any
	 */
	public BagData getBagFromId(int id) {
		for(BagData b : bagData) {
			if(b.id == id) return b;
		}
		return null;
	}
	
	public void addPeople(PeopleData pd) {
		if(getPeopleFromId(pd.id) != null) throw new IllegalStateException("people exist");
		peopleData.add(pd);
		peopleJson.get("people").getAsJsonArray().add(pd.toJsonObject());
	}
	
	public void addPickaxe(PickaxeData pd) {
		if(getPickaxeFromId(pd.id) != null) throw new IllegalStateException("pickaxe exist");
		pickaxeData.add(pd);
		peopleJson.get("pickaxe").getAsJsonArray().add(pd.toJsonObject());
	}
	
	public void addBag(BagData bd) {
		if(getBagFromId(bd.id) != null) throw new IllegalStateException("bag exist");
		bagData.add(bd);
		peopleJson.get("bag").getAsJsonArray().add(bd.toJsonObject());
	}
	
	@Override
	public void save() throws IOException {
		Saveable.jsonSaver(peopleJson.toString(), storagePeople);
	}
	
	public static class PeopleData {
		
		public final int id;
		public final String name;
		public final int maxStamina;
		public final int lastStamina;
		public final int strong;
		public final int skillId;
		private final int[] skillValue;
		
		public PeopleData(int id, String n, int max, int last, int str, int skill, int ...skillVal) {
			this.id = id;
			name = n;
			maxStamina = max;
			lastStamina = last;
			strong = str;
			skillId = skill;
			skillValue = skillVal;
		}
		
		public PeopleData(int id, String n, int max, int last, int str, int skill, JsonArray skillVal) {
			this.id = id;
			name = n;
			maxStamina = max;
			lastStamina = last;
			strong = str;
			skillId = skill;
			int size = skillVal.size();
			skillValue = new int[size];
			for(int i = 0;i < size;i++) {
				skillValue[i] = skillVal.get(i).getAsInt();
			}
		}
		
		public int[] getSkillValue() {
			return skillValue;
		}
		
		public JsonObject toJsonObject() {
			JsonObject jo = new JsonObject();
			jo.addProperty("id", id);
			jo.addProperty("name", name);
			jo.addProperty("maxStamina", maxStamina);
			jo.addProperty("lastStamina", lastStamina);
			jo.addProperty("strong", strong);
			jo.addProperty("skillId", skillId);
			JsonArray ja = new JsonArray(skillValue.length);
			for(int i : skillValue) {
				ja.add(i);
			}
			jo.add("skillValue", ja);
			return jo;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			PeopleData that = (PeopleData)o;
			return id == that.id;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
	}
	public static class PickaxeData {
		
		public final int id;
		public final int level;
		public final int maxDamage;
		public final int damage;
		
		public PickaxeData(int i, int l, int max, int d) {
			id = i;
			level = l;
			maxDamage = max;
			damage = d;
		}
		
		public JsonObject toJsonObject() {
			JsonObject jo = new JsonObject();
			jo.addProperty("id", id);
			jo.addProperty("level", level);
			jo.addProperty("maxDamage", maxDamage);
			jo.addProperty("damage", damage);
			return jo;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			PickaxeData that = (PickaxeData)o;
			return id == that.id &&
					level == that.level;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
	}
	public static class BagData {
		
		public final int id;
		public final int maxSpace;
		
		public BagData(int i, int max) {
			id = i;
			maxSpace = max;
		}
		
		public JsonObject toJsonObject() {
			JsonObject jo = new JsonObject();
			jo.addProperty("id", id);
			jo.addProperty("maxSpace", maxSpace);
			return jo;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			BagData bagData = (BagData)o;
			return id == bagData.id;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
	}
	
}
