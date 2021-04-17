package hang_up_game.java.window.shop;

import hang_up_game.java.game.Item;

import javax.swing.*;

public class ItemHolder extends JButton {
	
	private Item item;
	
	public ItemHolder(Item item) {
		super(item.name);
		this.item = item;
	}
	
	public Item getItem() {
		return item;
	}
	
}