package hang_up_game.java.game;

import hang_up_game.java.io.data.storage.Item;
import hang_up_game.java.io.data.storage.Machine;
import hang_up_game.java.io.data.storage.People;

public enum ChestContent {
	
	Common(10000, 0, 0)
	,
	CommonMine(30, 1, 2, People.getRandomPickaxe(1, 2), Machine.getRandomHead(1, 2))
	,
	CommonMachine(30, 1, 2, Machine.getRandomEngine(10, 50), Machine.getRandomHead(1, 2), Machine.getRandomBattery(5000, 13000),
			Machine.getRandomChest(500, 2000))
	,
	CommonPeople(30, 1, 2, People.getRandomPickaxe(1, 2), People.getRandomBag(50, 300))
	,
	CommonMinePlus(10, 2, 3, People.getRandomPickaxe(1, 2), Machine.getRandomHead(1, 2))
	,
	CommonMachinePlus(10, 2, 3, Machine.getRandomEngine(10, 50), Machine.getRandomHead(1, 2), Machine.getRandomBattery(5000, 13000),
			Machine.getRandomChest(500, 2000))
	,
	CommonPeoplePlus(10, 2, 3, People.getRandomPickaxe(1, 2), People.getRandomBag(50, 300))
	,
	CommonMinePlus2(5, 3, 5, People.getRandomPickaxe(1, 2), Machine.getRandomHead(1, 2))
	,
	CommonMachinePlus2(5, 3, 5, Machine.getRandomEngine(10, 50), Machine.getRandomHead(1, 2), Machine.getRandomBattery(5000, 13000),
			Machine.getRandomChest(500, 2000))
	,
	CommonPeoplePlus2(5, 3, 5, People.getRandomPickaxe(1, 2), People.getRandomBag(50, 300))
	,
	CommonMinePlus3(1, 5, 5, People.getRandomPickaxe(1, 2), Machine.getRandomHead(1, 2))
	,
	CommonMachinePlus3(1, 5, 5, Machine.getRandomEngine(10, 50), Machine.getRandomHead(1, 2), Machine.getRandomBattery(5000, 13000),
			Machine.getRandomChest(500, 2000))
	,
	CommonPeoplePlus3(1, 5, 5, People.getRandomPickaxe(1, 2), People.getRandomBag(50, 300))
	
	
	
	,
	RareMine(10, 1, 2, People.getRandomPickaxe(1, 3), Machine.getRandomHead(1, 3))
	,
	RareMachine(10, 1, 2, Machine.getRandomEngine(10, 70), Machine.getRandomHead(1, 3), Machine.getRandomBattery(10000, 30000),
			Machine.getRandomChest(1000, 3000))
	,
	RarePeople(10, 1, 2, People.getRandomPickaxe(1, 3), People.getRandomBag(100, 500))
	,
	RareMinePlus(5, 2, 3, People.getRandomPickaxe(1, 3), Machine.getRandomHead(1, 3))
	,
	RareMachinePlus(5, 2, 3, Machine.getRandomEngine(10, 70), Machine.getRandomHead(1, 3), Machine.getRandomBattery(10000, 30000),
			Machine.getRandomChest(1000, 3000))
	,
	RarePeoplePlus(5, 2, 3, People.getRandomPickaxe(1, 3), People.getRandomBag(100, 500))
	,
	RareMinePlus2(1, 3, 5, People.getRandomPickaxe(1, 3), Machine.getRandomHead(1, 3))
	,
	RareMachinePlus2(1, 3, 5, Machine.getRandomEngine(10, 70), Machine.getRandomHead(1, 3), Machine.getRandomBattery(10000, 30000),
			Machine.getRandomChest(1000, 3000))
	,
	RarePeoplePlus2(1, 3, 5, People.getRandomPickaxe(1, 3), People.getRandomBag(100, 500))
	
	
	
	,
	EpicMine(5, 1, 2, People.getRandomPickaxe(2, 5), Machine.getRandomHead(2, 5))
	,
	EpicMachine(5, 1, 2, Machine.getRandomEngine(50, 200), Machine.getRandomHead(2, 5), Machine.getRandomBattery(15000, 40000),
			Machine.getRandomChest(2000, 7000))
	,
	EpicPeople(5, 1, 2, People.getRandomPickaxe(2, 5), People.getRandomBag(1000, 3000))
	,
	EpicMinePlus(1, 2, 3, People.getRandomPickaxe(2, 5), Machine.getRandomHead(2, 5))
	,
	EpicMachinePlus(1, 2, 3, Machine.getRandomEngine(50, 200), Machine.getRandomHead(2, 5), Machine.getRandomBattery(15000, 40000),
			Machine.getRandomChest(2000, 7000))
	,
	EpicPeoplePlus(1, 2, 3, People.getRandomPickaxe(2, 5), People.getRandomBag(1000, 3000))
	
	
	
	,
	LegendMine(1, 1, 1, People.getRandomPickaxe(4, 5), Machine.getRandomHead(4, 5))
	,
	LegendMachine(1, 1, 1, Machine.getRandomEngine(100, 500), Machine.getRandomHead(4, 5), Machine.getRandomBattery(30000, 50000),
			Machine.getRandomChest(5000, 15000))
	,
	LegendPeople(1, 1, 1, People.getRandomPickaxe(4, 5), People.getRandomBag(1500, 5000))
	
	
	
	,
//	Plugin(1, 1, 1);
	
	;
	
	int weight;
	int minCount;
	int maxCount;
	Item[] content;
	ChestContent(int w, int min, int max, Item ...content) {
		weight = w;
		minCount = min;
		maxCount = max;
		this.content = content;
	}
	
	public static int getTotalWeight() {
		int w = 0;
		for(ChestContent cc : ChestContent.values()) {
			w += cc.weight;
		}
		return w;
	}
	
	public static ChestContent getRandom(int value) {
		if(value >= getTotalWeight()) throw new IllegalArgumentException("value bigger than total weight");
		int now = value;
		for(ChestContent m : ChestContent.values()) {
			if(now >= m.weight) {
				now -= m.weight;
			}
			else {
				return m;
			}
		}
		return ChestContent.Common;
	}
	
}
