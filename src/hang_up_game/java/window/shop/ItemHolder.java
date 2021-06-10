package hang_up_game.java.window.shop;

import hang_up_game.java.game.Item;
import org.lf.logger.Log;

import javax.swing.*;

public class ItemHolder extends JButton {
	
	private final Item item;
	
	public ItemHolder(Item item) {
		super("<html>" + item.name + "<br>$" + item.getBuyPrice() + "</html>");
		Log.d("itemHolder", "init with item:" + item.name);
		this.item = item;
	}
	
	public Item getItem() {
		return item;
	}
	
}
