package hang_up_game.java.game;

import hang_up_game.java.io.Log;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.MiningData;
import hang_up_game.java.io.data.Plugin;
import hang_up_game.java.io.data.storage.Item;
import hang_up_game.java.io.data.storage.Machine;
import hang_up_game.java.window.Constant;

import java.io.Serializable;
import java.util.*;

public class MachineMiner implements Serializable {
	
	public static final long serialVersionUID = 14L;
	
	private static final Map<Integer, String> msgToPrint = new HashMap<>();
	static {
		msgToPrint.put(1, "引擎毀損");
		msgToPrint.put(2, "鑽頭毀損");
		msgToPrint.put(3, "電池沒電");
		msgToPrint.put(4, "箱子已滿");
	}
	
	private final Object directLock = new Object();
	
	private int id;
	private int engineDamage;
	private int headDamage;
	private int battery;
	
	public Machine.Engine engine;
	public Machine.Head head;
	public Machine.Battery machineBattery;
	public Machine.Chest chest;
	public Plugin[] plugins;
	
	private Direct direct;
	private int chunkX;
	private int chunkY;
	private int blockInChunkX;
	private int blockInChunkY;
	private int miningCount;
	
	private Map<Mineral, Integer> mineral;
	private Set<Item> items;
	
	public MachineMiner(Machine.Engine e, Machine.Head h, Machine.Battery b, Machine.Chest c, Plugin ...p) {
		int[] pi = new int[p.length];
		for(int i = 0;i < p.length;i++) {
			pi[i] = Plugin.getIdFromPlugin(p[i]);
		}
		int empty = MiningData.Machine.getEmptyId();
		FileHolder.miningData.addMachine(new MiningData.Machine(empty, new MiningData.Machine.Parts(e.id, h.id, b.id, c.id, pi)));
		miningInit(empty, e, h, b, c, p);
	}
	
	@Deprecated
	private MachineMiner(int id) {
		this.id = id;
	}
	
	public void startMining() {
		Background.addMachineMiner(this);
	}
	public void mine() {
		int check = checkMining();
		if(check != 0) {
			Log.i("machine", "id:" + id + " " + msgToPrint.get(check));
			checkPlugin(check);
			if(check != 4) return;
		}
		if(check != 4) {
			doMine();
		}
		this.battery -= 10;
		machineBattery = Machine.Battery.quickData(machineBattery, this.battery);
		FileHolder.machine.addBattery(machineBattery);
	}
	private void doMine() {
		Mineral m = MiningMap.getMineral(chunkX, chunkY);
		Set<Item> itemGet = MiningMap.findItem(chunkX, chunkY, blockInChunkX,blockInChunkY);
		dealMineral(m);
		items.addAll(itemGet);
		if(!itemGet.isEmpty()) {
			Log.i("machine", "id:" + id + " get " + itemGet.size() + " item(s)");
		}
		synchronized(directLock) {
			forward(direct);
		}
	}
	private void dealMineral(Mineral m) {
		if(head.level >= m.level) {
			Log.i("machine", "id:" + id + " get " + m.name());
			int damage = Mineral.getHighestLevel() - head.level + m.level;
			this.engineDamage++;
			this.headDamage += damage;
			mineral.put(m, mineral.getOrDefault(m, 0) + 1);
			head = Machine.Head.quickData(head, this.headDamage);
			engine = Machine.Engine.quickData(engine, this.engineDamage);
			FileHolder.machine.addEngine(engine);
			FileHolder.machine.addHead(head);
		}
	}
	private void miningInit(int i, Machine.Engine e, Machine.Head h, Machine.Battery b, Machine.Chest c, Plugin[] p) {
		Log.i("machine", "id:" + id + " init");
		chunkX = 0;
		chunkY = 0;
		blockInChunkX = 0;
		blockInChunkY = 0;
		miningCount = 0;
		direct = Direct.North;
		
		engine = e;
		head = h;
		machineBattery = b;
		chest = c;
		List<Plugin> tmpL = Arrays.asList(p);
		tmpL.sort((o1, o2) -> {
			int id1 = Plugin.getIdFromPlugin(o1), id2 = Plugin.getIdFromPlugin(o2);
			if(id1 > id2) return 1;
			else if(id1 < id2) return 2;
			return 0;
		});
		plugins = tmpL.toArray(new Plugin[0]);
		
		id = i;
		engineDamage = e.getDamage();
		headDamage = h.getDamage();
		battery = b.getBattery();
		
		mineral = new EnumMap<>(Mineral.class);
		items = new HashSet<>();
	}
	private void forward(Direct d) {
		if(miningCount < 5) {
			miningCount++;
			return;
		}
		miningCount = 0;
		switch(d) {
			case North:
				if(blockInChunkY < 9) {
					blockInChunkY++;
					return;
				}
				blockInChunkY = 0;
				chunkY++;
				return;
			case South:
				if(blockInChunkY > 0) {
					blockInChunkY--;
					return;
				}
				blockInChunkY = 9;
				chunkY--;
				return;
			case East:
				if(blockInChunkX < 9) {
					blockInChunkX++;
					return;
				}
				blockInChunkY = 0;
				chunkX++;
				return;
			case West:
				if(blockInChunkX > 0) {
					blockInChunkX--;
					return;
				}
				blockInChunkY = 9;
				chunkX--;
		}
	}
	private void checkPlugin(int check) {
		for(Plugin p : plugins) {
			pluginRun(p, check);
		}
	}
	private int checkMining() {
		if(engineDamage >= engine.maxDamage) {
			engineDamage = engine.maxDamage;
			return 1;
		}
		if(headDamage >= head.maxDamage) {
			headDamage = head.maxDamage;
			return 2;
		}
		if(battery <= 0) {
			battery = 0;
			return 3;
		}
		if(countItems() >= chest.maxSpace) {
			return 4;
		}
		return 0;
	}
	public int strong() {
		return engine.strong;
	}
	private int countItems() {
		int sum = items.size();
		for(Mineral m : mineral.keySet()) {
			sum += mineral.get(m);
		}
		return sum;
	}
	public Direct getDirect() {
		synchronized(directLock) {
			return direct;
		}
	}
	public void setDirect(Direct d) {
		synchronized(directLock) {
			direct = d;
		}
	}
	
	private void pluginRun(Plugin p, int check) {
		switch(p) {
			case PartBreakNotice:
				if(check == 1 || check == 2) {
					sendNotice(msgToPrint.get(check));
				}
				break;
			case LowBatteryNotice:
				if(check == 3) {
					sendNotice(msgToPrint.get(check));
				}
				break;
			case FullChestNotice:
				if(check == 4) {
					sendNotice(msgToPrint.get(check));
				}
				break;
			case PartBreakBack:
				if(check == 1 || check == 2) {
					sendNotice(msgToPrint.get(check) + "，自動返回");
					Background.returnMachineMiner(this);
				}
				break;
			case LowBatteryBack:
				if(check == 3) {
					sendNotice(msgToPrint.get(check) + "，自動返回");
					Background.returnMachineMiner(this);
				}
				break;
			case FullChestBack:
				if(check == 4) {
					sendNotice(msgToPrint.get(check) + "，自動返回");
					Background.returnMachineMiner(this);
				}
		}
	}
	
	public int getId() {
		return id;
	}
	
	public Map<Mineral, Integer> getMineral() {
		return mineral;
	}
	
	public Set<Item> getItems() {
		return items;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		MachineMiner that = (MachineMiner)o;
		return id == that.id;
	}
	@Override
	public int hashCode() {
		return id;
	}
	@Override
	public String toString() {
		return "" + id;
	}
	
	private void sendNotice(String des) {
		Background.throwMsg(Constant.ApplicationName + "-" + id, des);
	}
	
	public static MachineMiner getCompareInstance(int id) {
		return new MachineMiner(id);
	}
	
}
