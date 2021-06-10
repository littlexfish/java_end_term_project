package hang_up_game.java.game;

import hang_up_game.java.io.data.FileHolder;
import org.lf.logger.Log;

import java.util.ArrayList;
import java.util.LinkedList;

public class Blueprints {
	
	public static final ArrayList<Blueprint> blueprints = new ArrayList<>(36);
	
	static {
		Log.d("blueprint", "init blueprint");
		//pickaxe
		new Blueprint(0, 10);
		new Blueprint(1, 50);
		new Blueprint(2, 10, 10);
		new Blueprint(3, 0, 20, 10, 5, 5);
		new Blueprint(4, 0, 0, 0, 10, 10, 10);
		new Blueprint(5, 0, 0, 0, 0, 0, 30, 10);
		//bag
		new Blueprint(6, 5);
		new Blueprint(7, 10);
		new Blueprint(8, 10, 1);
		new Blueprint(9, 0, 10, 10, 5, 5);
		new Blueprint(10, 0, 0, 0, 10, 10, 5);
		new Blueprint(11, 0, 0, 0, 0, 0, 10, 5);
		//engine
		new Blueprint(12, 50);
		new Blueprint(13, 100);
		new Blueprint(14, 50, 20);
		new Blueprint(15, 0, 50, 50, 50, 50);
		new Blueprint(16, 0, 0, 0, 30, 30, 50);
		new Blueprint(17, 0, 0, 0, 0, 0, 100, 50);
		//head
		new Blueprint(18, 50);
		new Blueprint(19, 100);
		new Blueprint(20, 50, 20);
		new Blueprint(21, 0, 50, 50, 50, 50);
		new Blueprint(22, 0, 0, 0, 30, 30, 50);
		new Blueprint(23, 0, 0, 0, 0, 0, 100, 50);
		//battery
		new Blueprint(24, 50);
		new Blueprint(25, 100);
		new Blueprint(26, 50, 20);
		new Blueprint(27, 0, 50, 50, 50, 50);
		new Blueprint(28, 0, 0, 0, 30, 30, 50);
		new Blueprint(29, 0, 0, 0, 0, 0, 100, 50);
		//chest
		new Blueprint(30, 30);
		new Blueprint(31, 50);
		new Blueprint(32, 30, 10);
		new Blueprint(33, 0, 30, 30, 15, 15);
		new Blueprint(34, 0, 0, 0, 20, 20, 30);
		new Blueprint(35, 0, 0, 0, 0, 0, 50, 30);
	}
	
	public static LinkedList<Blueprint> getBlueprintCanBuy() {
		int[] unlock = FileHolder.shop.getBlueprints();
		LinkedList<Blueprint> bs = new LinkedList<>();
		Bl:
		for(int i = 0;i < blueprints.size();i++) {
			for(int un : unlock) {
				if(i == un) {
					continue Bl;
				}
			}
			bs.add(blueprints.get(i));
		}
		return bs;
	}
	
	public static Blueprint findBluePrintFromName(String name) {
		Item i = Item.getItemFromName(name);
		for(Blueprint b : blueprints) {
			if(b.id == Item.getIdFromItem(i)) return b;
		}
		return null;
	}
	
}
