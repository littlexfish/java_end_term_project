package hang_up_game.java.game;

import hang_up_game.java.io.Log;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.storage.Item;
import hang_up_game.java.io.data.storage.Machine;
import hang_up_game.java.io.data.storage.People;
import hang_up_game.java.window.GameFrame;
import hang_up_game.java.window.Minimize;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Background {
	
	private static final Object machineLock = new Object();
	private static final Thread minerThread;
	
	private static final Thread recoverThread;
	private static boolean recover = true;
	private static final int recoverDelay = 500;
	private static final Object recoverLock = new Object();
	
	private static final Thread chargeThread;
	private static boolean charge = true;
	private static final int chargeDelay = 50;
	private static final Object chargeLock = new Object();
	
	private static final Map<MachineMiner, long[]> machines;
	private static Minimize minimize = null;
	
	static {
		Log.d("background", "init thread");
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
		
		recoverThread = new Thread(() -> {
			while(true) {
				if(!recover) {
					synchronized(recoverLock) {
						try {
							recoverLock.wait();
							recover = true;
						}
						catch(InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				//stamina
				ArrayList<People.PeopleData> needRecover = FileHolder.people.getPeopleData();
				for(People.PeopleData p : needRecover) {
					FileHolder.people.setPeople(People.PeopleData.quickData(p, p.lastStamina + 1));
				}
				
				try {
					//noinspection BusyWait
					Thread.sleep(recoverDelay);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		chargeThread = new Thread(() -> {
			while(true) {
				if(!charge) {
					synchronized(chargeLock) {
						try {
							chargeLock.wait();
							charge = true;
						}
						catch(InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				//battery
				ArrayList<Machine.Battery> needCharge = FileHolder.machine.getBatteryNeedCharge();
				for(Machine.Battery b : needCharge) {
					FileHolder.machine.setBattery(Machine.Battery.quickData(b, b.getBattery() + 1));
				}
				try {
					//noinspection BusyWait
					Thread.sleep(chargeDelay);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
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
		return 1000 / (strong * 100);
	}
	public static Set<MachineMiner> getAllMinerOnline() {
		return machines.keySet();
	}
	public static void startRecover() {
		synchronized(recoverLock) {
			recoverLock.notify();
			recover = true;
		}
	}
	public static void stopRecover() {
		recover = false;
	}
	public static void startCharge() {
		synchronized(chargeLock) {
			chargeLock.notify();
			charge = true;
		}
	}
	public static void stopCharge() {
		charge = false;
	}
	public static void makeSureBackground(GameFrame gf) {
		try {
			minimize = new Minimize(gf);
		}
		catch(AWTException e) {
			e.printStackTrace(FileHolder.getExportCrashReport());
			e.printStackTrace();
		}
		machineRun();
//		stopCharge();
		chargeThread.start();
		recoverThread.start();
	}
	
}
