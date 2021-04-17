package hang_up_game.java.game;

public enum Item {
	//pickaxe extra:level
	NewlyPickaxe("新手稿子", 5, 100, 500, 1),
	AdvPickaxe("進階稿子", 10, 300, 1000, 2),
	HighPickaxe("高級稿子", 50, 500, 1200, 3),
	GoodPickaxe("優秀稿子", 100, 1000, 3000, 4),
	MasterPickaxe("大師稿子", 300, 2000, 3500, 5),
	FinalPickaxe("究極稿子", 500, 3000, 5000, 6),
	
	//bag no extra
	NewlyBag("新手包包", 5, 10, 100),
	AdvBag("進階包包", 10, 100, 500),
	HighBag("高級包包", 30, 500, 1500),
	GoodBag("優秀包包", 50, 1500, 3000),
	MasterBag("大師包包", 100, 3000, 5000),
	FinalBag("究極包包", 300, 5000, 10000),
	
	//engine extra:strong
	NewlyEngine("新手引擎", 10, 1000, 5000, 10, 30),
	AdvEngine("進階引擎", 50, 3000, 7500, 30, 50),
	HighEngine("高級引擎", 100, 5000, 10000, 50, 100),
	GoodEngine("優秀引擎", 250, 10000, 13000, 100, 300),
	MasterEngine("大師引擎", 500, 12000, 20000, 300, 500),
	FinalEngine("究極引擎", 1000, 15000, 30000, 500, 1000),
	
	//head extra:level
	NewlyHead("新手鑽頭", 10, 1000, 5000, 1),
	AdvHead("進階鑽頭", 50, 3000, 10000, 2),
	HighHead("高級鑽頭", 100, 5000, 12000, 3),
	GoodHead("優秀鑽頭", 250, 10000, 30000, 4),
	MasterHead("大師鑽頭", 500, 20000, 35000, 5),
	FinalHead("究極鑽頭", 1000, 30000, 50000, 6),
	
	//battery no extra
	NewlyBattery("新手電池", 10, 5000, 10000),
	AdvBattery("進階電池", 50, 10000, 15000),
	HighBattery("高級電池", 100, 15000, 30000),
	GoodBattery("優秀電池", 250, 30000, 50000),
	MasterBattery("大師電池", 500, 50000, 100000),
	FinalBattery("究極電池", 1000, 100000, 1000000),
	
	//chest no extra
	NewlyChest("新手箱子", 5, 100, 1000),
	AdvChest("進階箱子", 30, 1000, 2000),
	HighChest("高級箱子", 75, 2000, 5000),
	GoodChest("優秀箱子", 150, 5000, 10000),
	MasterChest("大師箱子", 300, 10000, 15000),
	FinalChest("究極箱子", 500, 15000, 30000),
	;
	
	public final String name;
	public final int price;
	private final int maxValue;
	private final int minValue;
	private final int minExtraValue;
	private final int maxExtraValue;
	
	Item(String name, int p, int min, int max, int minExtra, int maxExtra) {
		this.name = name;
		price = p;
		minValue = min;
		maxValue = max;
		minExtraValue = minExtra;
		maxExtraValue = maxExtra;
	}
	
	Item(String name, int p, int min, int max, int extra) {
		this(name, p, min, max, extra, extra);
	}
	
	Item(String name, int p, int min, int max) {
		this(name, p, min, max, -1);
	}
	
	public int getRandomValue() {
		return (int)(minValue + (Math.random() * (maxValue - minValue + 1)));
	}
	
	public int getRandomExtra() {
		if(minExtraValue == -1) throw new UnsupportedOperationException("no extra");
		if(minExtraValue == maxExtraValue) return minExtraValue;
		return (int)(minExtraValue + (Math.random() * (maxExtraValue - minExtraValue + 1)));
	}
	
	public int getBuyPrice() {
		return price * 100;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static Item getItemFromId(int id){
		return Item.values()[id];
	}
	
}
