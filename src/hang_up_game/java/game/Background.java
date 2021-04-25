package hang_up_game.java.game;

import hang_up_game.java.io.data.FileHolder;
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
		try {
			minimize = new Minimize();
		}
		catch(AWTException e) {
			e.printStackTrace();
		}
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
	public static void makeSureBackground() {
	}
}
