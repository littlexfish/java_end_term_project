package hang_up_game.java.game;

public enum Mineral {
	
	Stone(500, "石頭", 1, 10),
	Copper(50, "銅", 2, 20),
	Iron(20, "鐵", 3, 20),
	Silver(10, "銀", 3, 10),
	Aluminum(10, "鋁", 3, 15),
	Gold(5, "金", 4, 5),
	Diamond(2, "鑽石", 5, 30),
	Crystal(1, "水晶", 6, 50);
	
	public final String chinese;
	int weight;
	public final int level;
	public final int hard;
	Mineral(int w, String trans, int l, int h) {
		weight = w;
		chinese = trans;
		level = l;
		hard = h;
	}
	
	Mineral(String trans) {
		this(1, trans, 1, 1);
	}
	
	public static int getHighestLevel() {
		return 6;
	}
	
	public static int getTotalWeight() {
		int w = 0;
		for(Mineral m : Mineral.values()) {
			w += m.weight;
		}
		return w;
	}
	
	public static Mineral getRandom(int value) {
		if(value >= getTotalWeight()) throw new IllegalArgumentException("value bigger than total weight");
		int now = (int)(Math.random() * value);
		for(Mineral m : Mineral.values()) {
			if(now >= m.weight) {
				now -= m.weight;
			}
			else {
				return m;
			}
		}
		return null;
	}
	
}
