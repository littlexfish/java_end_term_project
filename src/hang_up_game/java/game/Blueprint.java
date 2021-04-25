package hang_up_game.java.game;

import java.util.EnumMap;
import java.util.Objects;

public class Blueprint {
	
	public final int id;
	public final EnumMap<Mineral, Integer> craftRes;
	
	public Blueprint(int id, int ...mineral) {
		this.id = id;
		craftRes = new EnumMap<>(Mineral.class);
		Mineral[] ms = Mineral.values();
		for(int i = 0;i < mineral.length;i++) {
			if(mineral[i] <= 0) {
				continue;
			}
			craftRes.put(ms[i], mineral[i]);
		}
		Blueprints.blueprints.add(this);
	}
	
	@Override
	public String toString() {
		return Item.getItemFromId(id).name;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Blueprint blueprint = (Blueprint)o;
		return id == blueprint.id;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
}
