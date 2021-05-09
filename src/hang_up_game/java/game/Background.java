package hang_up_game.java.game;

import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.storage.Item;
import hang_up_game.java.io.data.storage.Machine;
import hang_up_game.java.io.data.storage.People;
import hang_up_game.java.window.GameFrame;
import hang_up_game.java.window.Minimize;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Background {
	
	private static final Object machineLock = new Object();
	private static final Thread minerThread;
	private static final Map<MachineMiner, long[]> machines;
	private static Minimize minimize = null;
	
	static {
		minerThread = new Thread(() -> {
			while(true) {
				Set<MachineMiner> machineSet;
				synchronized(machineLock) {
					machineSet = Background.machines.keySet();
				}
				for(MachineMiner m : machineSet) {
					long ft = Background.machines.get(m)[0];
					long delay = Background.machines.get(m)[1];
					if((System.currentTimeMillis() - ft) >= delay) {
						Background.machines.get(m)[0] = System.currentTimeMillis();
						m.mine();
					}
				}
			}
		});
		machines = new HashMap<>();
		
	}
	public static void machineRun() {
		minerThread.start();
	}
	public static void addMachineMiner(MachineMiner mm) {
		synchronized(machineLock) {
			machines.put(mm, new long[] {System.currentTimeMillis(), getDelay(mm.strong())});
		}
	}
	public static void returnMachineMiner(MachineMiner mm) {
		synchronized(machineLock) {
			machines.remove(mm);
			FileHolder.miningData.releaseMachine(mm.getId());
			Set<Mineral> ms = mm.getMineral().keySet();
			for(Mineral m : ms) {
				FileHolder.mineral.setMineral(m, FileHolder.mineral.getMineral(m) + mm.getMineral().get(m));
			}
			Set<Item> items = mm.getItems();
			for(Item item : items) {
				if(item instanceof People.PickaxeData) {
					FileHolder.people.addPickaxe((People.PickaxeData)item);
				}
				if(item instanceof People.BagData) {
					FileHolder.people.addBag((People.BagData)item);
				}
				if(item instanceof Machine.Engine) {
					FileHolder.machine.addEngine((Machine.Engine)item);
				}
				if(item instanceof Machine.Head) {
					FileHolder.machine.addHead((Machine.Head)item);
				}
				if(item instanceof Machine.Battery) {
					FileHolder.machine.addBattery((Machine.Battery)item);
				}
				if(item instanceof Machine.Chest) {
					FileHolder.machine.addChest((Machine.Chest)item);
				}
			}
		}
	}
	public static void throwMsg(String title, String msg) {
		if(minimize != null) {
			minimize.throwMsg(title, msg);
		}
	}
	private static int getDelay(int strong) {
		return 1000 / strong * 100;
	}
	public static Set<MachineMiner> getAllMinerOnline() {
		return machines.keySet();
	}
	public static void makeSureBackground(GameFrame gf) {
		try {
			minimize = new Minimize(gf);
		}
		catch(AWTException e) {
			e.printStackTrace();
		}
		machineRun();
	}
	
}
