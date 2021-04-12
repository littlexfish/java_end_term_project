package hang_up_game.java.io.data.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.Saveable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Random;

@SuppressWarnings("unused")
public class People implements Saveable {
	
	private final File storagePeople = new File(getClass().getResource("/hang_up_game/files/storage/people.json").toURI());
	private final JsonObject peopleJson = new JsonStreamParser(new FileReader(storagePeople)).next().getAsJsonObject();
	public final LinkedList<PeopleData> peopleData = new LinkedList<>();
	public final LinkedList<PickaxeData> pickaxeData = new LinkedList<>();
	public final LinkedList<BagData> bagData = new LinkedList<>();
	
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
				pickaxeData.add(new PickaxeData(jo.get("id").getAsInt(), jo.get("name").getAsString(), jo.get("level").getAsInt(), jo.get("maxDamage").getAsInt(), jo.get("damage").getAsInt()));
			}
		}
		//bag
		{
			JsonArray ja = peopleJson.get("bag").getAsJsonArray();
			for(JsonElement je : ja) {
				JsonObject jo = je.getAsJsonObject();
				bagData.add(new BagData(jo.get("id").getAsInt(), jo.get("name").getAsString(), jo.get("maxSpace").getAsInt()));
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
		if(getPeopleFromId(pd.id) != null) {
			setPeople(pd);
			return;
		}
		peopleData.add(pd);
		peopleJson.get("people").getAsJsonArray().add(pd.toJsonObject());
	}
	
	public void setPeople(PeopleData pd) {
		JsonArray ja = peopleJson.get("people").getAsJsonArray();
		for(int i = 0;i < ja.size();i++) {
			JsonObject jo = ja.get(i).getAsJsonObject();
			if(jo.get("id").getAsInt() == pd.id) {
				peopleData.remove(i);
				peopleData.add(pd);
				ja.remove(i);
				ja.add(pd.toJsonObject());
				break;
			}
		}
	}
	
	public void addPickaxe(PickaxeData pd) {
		if(getPickaxeFromId(pd.id) != null) {
			setPickaxe(pd);
			return;
		}
		pickaxeData.add(pd);
		peopleJson.get("pickaxe").getAsJsonArray().add(pd.toJsonObject());
	}
	
	public void setPickaxe(PickaxeData pd) {
		JsonArray ja = peopleJson.get("pickaxe").getAsJsonArray();
		for(int i = 0;i < ja.size();i++) {
			JsonObject jo = ja.get(i).getAsJsonObject();
			if(jo.get("id").getAsInt() == pd.id) {
				pickaxeData.remove(i);
				pickaxeData.add(pd);
				ja.remove(i);
				ja.add(pd.toJsonObject());
				break;
			}
		}
	}
	
	public void addBag(BagData bd) {
		if(getBagFromId(bd.id) != null) {
			setBag(bd);
			return;
		}
		bagData.add(bd);
		peopleJson.get("bag").getAsJsonArray().add(bd.toJsonObject());
	}
	
	public void setBag(BagData bd) {
		JsonArray ja = peopleJson.get("bag").getAsJsonArray();
		for(int i = 0;i < ja.size();i++) {
			JsonObject jo = ja.get(i).getAsJsonObject();
			if(jo.get("id").getAsInt() == bd.id) {
				//noinspection SuspiciousListRemoveInLoop
				bagData.remove(i);
				bagData.add(bd);
				ja.remove(i);
				ja.add(bd.toJsonObject());
			}
		}
	}
	
	public static PickaxeData getRandomPickaxe(int minLevel, int maxLevel) {
		Random r = new Random();
		int level = minLevel + r.nextInt(maxLevel - minLevel + 1);
		String name = "";
		int min = 0;
		int max = 0;
		switch(level) {
			case 1:
				name = "新手稿子";
				min = 100;
				max = 500;
				break;
			case 2:
				name = "進階稿子";
				min = 300;
				max = 1000;
				break;
			case 3:
				name = "高級稿子";
				min = 500;
				max = 1200;
				break;
			case 4:
				name = "優秀稿子";
				min = 1000;
				max = 3000;
				break;
			case 5:
				name = "大師稿子";
				min = 2000;
				max = 3500;
				break;
			case 6:
				name = "究極稿子";
				min = 3000;
				max = 5000;
				break;
		}
		int dur = min + r.nextInt(max - min + 1);
		return new PickaxeData(PickaxeData.getEmptyId(), name, level, dur, 0);
	}
	public static BagData getRandomBag(int minSpace, int maxSpace) {
		Random r = new Random();
		int space = minSpace + r.nextInt(maxSpace - minSpace + 1);
		String name;
		if(space < 100) name = "新手包包";
		else if(space < 500) name = "進階包包";
		else if(space < 1500) name = "高級包包";
		else if(space < 3000) name = "優秀包包";
		else if(space < 5000) name = "大師包包";
		else name = "究極包包";
		return new BagData(BagData.getEmptyId(), name, space);
	}
	
	@Override
	public void save() throws IOException {
		Saveable.jsonSaver(peopleJson.toString(), storagePeople);
	}
	
	public static class PeopleData {
		
		public static int getEmptyId() {
			int nowId = 1;
			while(true) {
				boolean contain = false;
				for(PeopleData p : FileHolder.people.peopleData) {
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
		public static PeopleData quickData(PeopleData pd, int stamina) {
			return new PeopleData(pd.id, pd.name, pd.maxStamina, stamina, pd.strong, pd.skillId, pd.skillValue);
		}
		
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
		
		//TODO: let skill comes true
		public String list() {
			return "名字:" + name + "\n體力:" + lastStamina + "/" + maxStamina + "\n力量:" + strong;
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
		
		@Override
		public String toString() {
			return name;
		}
	}
	public static class PickaxeData extends Item {
		
		public static int getEmptyId() {
			int nowId = 1;
			while(true) {
				boolean contain = false;
				for(PickaxeData p : FileHolder.people.pickaxeData) {
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
		public static PickaxeData quickData(PickaxeData pd, int damage) {
			return new PickaxeData(pd.id, pd.name, pd.level, pd.maxDamage, damage);
		}
		
		public final int level;
		public final int maxDamage;
		public final int damage;
		
		public PickaxeData(int i, String n, int l, int max, int d) {
			super(i, n);
			level = l;
			maxDamage = max;
			damage = d;
		}
		
		public JsonObject toJsonObject() {
			JsonObject jo = new JsonObject();
			jo.addProperty("id", id);
			jo.addProperty("name", name);
			jo.addProperty("level", level);
			jo.addProperty("maxDamage", maxDamage);
			jo.addProperty("damage", damage);
			return jo;
		}
		
		public String list() {
			return "名字:" + name + "\n挖掘等級:" + level + "\n毀損值:" + damage + "/" + maxDamage;
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
		
		@Override
		public String toString() {
			return name;
		}
	}
	public static class BagData extends Item {
		
		public static int getEmptyId() {
			int nowId = 1;
			while(true) {
				boolean contain = false;
				for(BagData p : FileHolder.people.bagData) {
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
		
		public final int maxSpace;
		
		public BagData(int i, String n, int max) {
			super(i, n);
			maxSpace = max;
		}
		
		public JsonObject toJsonObject() {
			JsonObject jo = new JsonObject();
			jo.addProperty("id", id);
			jo.addProperty("name", name);
			jo.addProperty("maxSpace", maxSpace);
			return jo;
		}
		
		public String list() {
			return "名字:" + name + "\n空間:" + maxSpace;
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
		@Override
		public String toString() {
			return name;
		}
	}
	
}
