package hang_up_game.java.io.data;

import java.util.EnumMap;

public enum Plugin {
	
	LowBatteryNotice("低電量提示"), FullChestNotice("滿箱提示"), PartBreakNotice("零件損壞提示"),
	LowBatteryBack("低電量自動返回"), FullChestBack("滿箱自動返回"), PartBreakBack("零件損壞自動返回"),
	
	;
	
	public final String name;
	Plugin(String name) {
		this.name = name;
		
	}
	private static final EnumMap<Plugin, Integer> pluginMap;
	static {
		pluginMap = new EnumMap<>(Plugin.class);
		Plugin[] ps = Plugin.values();
		for(int i = 0;i < ps.length;i++) {
			Plugin.pluginMap.put(ps[i], i);
		}
	}
	public static int getIdFromPlugin(Plugin p) {
		return pluginMap.get(p);
	}
	public static Plugin getPluginFromId(int id) {
		return Plugin.values()[id];
	}
	public static int getMaxId() {
		return Plugin.values().length - 1;
	}
}
