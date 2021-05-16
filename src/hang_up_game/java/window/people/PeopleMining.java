package hang_up_game.java.window.people;

import hang_up_game.java.game.Background;
import hang_up_game.java.game.Direct;
import hang_up_game.java.game.Mineral;
import hang_up_game.java.game.MiningMap;
import hang_up_game.java.io.Log;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.storage.Item;
import hang_up_game.java.io.data.storage.People;
import hang_up_game.java.window.Constant;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PeopleMining extends JDialog {
	
	private final Color Status = Color.GRAY;
	private final Color Error = Color.RED;
	private final Color Get_Item = Color.ORANGE;
	private final Color Get_Part = Color.MAGENTA;
	
	private int chunkX;
	private int chunkY;
	private int blockInChunkX;
	private int blockInChunkY;
	private int miningCount;
	private int stamina;
	private int damage;
	private Map<Mineral, Integer> mineral;
	private Set<Item> items;
	
	private final JScrollPane scroll;
	private final JTextPane console = new JTextPane();
	private final StyledDocument styledDocument = console.getStyledDocument();
	private final Style style = console.addStyle("console", null);
	
	public PeopleMining(PeopleDetail owner, People.PeopleData people, People.PickaxeData pickaxe, People.BagData bag) {
		super(owner.getOwner(), true);
		Log.d("peopleMining panel", "init");
		//init screen
		setTitle("人工挖礦");
		setBounds(Constant.getMiddleWindowRectangle(500, 500));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout(5, 5));
		
		scroll = new JScrollPane();
		getContentPane().add(scroll, BorderLayout.CENTER);
		
		console.setEditable(false);
		scroll.setViewportView(console);
		
		JPanel bottom = new JPanel(new GridLayout(1, 2, 5, 5));
		getContentPane().add(bottom, BorderLayout.SOUTH);
		
		JComboBox<Direct> direct = new JComboBox<>(Direct.values());
		bottom.add(direct);
		
		JButton mining = new JButton("挖");
		mining.setFocusable(false);
		mining.addActionListener(e -> mine(mining, (Direct)direct.getSelectedItem(), people, pickaxe, bag));
		bottom.add(mining);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Log.d("peopleMining panel", "close");
				FileHolder.storage(mineral, items);
				FileHolder.changeTools(owner, People.PeopleData.quickData(people, stamina), People.PickaxeData.quickData(pickaxe, damage));
				mineral.clear();
				items.clear();
				Background.startRecover();
			}
		});
		
		//game
		miningInit(people, pickaxe, bag);
		
	}
	
	private void appendText(String msg, Color c) {
		StyleConstants.setForeground(style, c);
		try {
			styledDocument.insertString(styledDocument.getLength(), msg, style);
			scroll.getViewport().setViewPosition(new Point(0, scroll.getVerticalScrollBar().getMaximum()));
		}
		catch(BadLocationException ignored) {}
	}
	private void appendTextWithBlack(String msg) {
		appendText(msg, Color.BLACK);
	}
	private void appendTextWithNewLine(String msg, Color c) {
		appendText(msg + "\n", c);
	}
	private void appendTextWithBlackAndNewLine(String msg) {
		appendTextWithBlack(msg + "\n");
	}
	
	private void mine(JButton needLock, Direct d, People.PeopleData people, People.PickaxeData pickaxe, People.BagData bag) {
		if(!checkMining(pickaxe, bag)) {
			return;
		}
		Log.d("peopleMining panel", "mining with time:" + getDelay(people.strong));
		appendTextWithBlackAndNewLine("開始挖掘礦物，預計挖掘時間: " + String.format("%.2f", (getDelay(people.strong) / 1000.0)) + "秒");
		countDown(needLock, getDelay(people.strong), d, people, pickaxe, bag);
	}
	private void doMine(Direct d, People.PeopleData people, People.PickaxeData pickaxe, People.BagData bag) {
		Mineral m = MiningMap.getMineral(chunkX, chunkY);
		Set<Item> itemGet = MiningMap.findItem(chunkX, chunkY, blockInChunkX,blockInChunkY);
		dealMineral(m, people, pickaxe);
		postItemMsg(itemGet);
		if(!itemGet.isEmpty()) {
			Log.i("peopleMining panel", "get " + itemGet.size() + " item(s)");
		}
		items.addAll(itemGet);
		forward(d);
		appendTextWithNewLine(status(people, pickaxe, bag), Status);
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
	private void dealMineral(Mineral m, People.PeopleData people, People.PickaxeData pickaxe) {
		if(pickaxe.level >= m.level) {
			Log.i("peopleMining panel", "get " + m.name());
			int damage = Mineral.getHighestLevel() - pickaxe.level + m.level;
			this.damage += damage;
			this.stamina -= 10;
			if(this.damage >= pickaxe.maxDamage) this.damage = pickaxe.maxDamage;
			appendTextWithNewLine("你挖到了" + m.chinese, Get_Item);
			mineral.put(m, mineral.getOrDefault(m, 0) + 1);
			FileHolder.people.addPickaxe(People.PickaxeData.quickData(pickaxe, this.damage));
			FileHolder.people.addPeople(People.PeopleData.quickData(people, this.stamina));
		}
		else {
			Log.i("peopleMining panel", "find " + m.name() + " but level too high");
			appendTextWithNewLine("你找到了" + m.chinese + "，但你挖不起來", Error);
		}
	}
	private int getDelay(int strong) {
		return (int)(1000.0 / strong * 100);
	}
	private void postItemMsg(Set<Item> items) {
		if(items.size() == 0) return;
		StringBuilder sb = new StringBuilder(items.size() * 5);
		sb.append("[");
		for(Item item : items) {
			sb.append(item.name).append(",");
		}
		sb = new StringBuilder(sb.substring(0, sb.toString().length() - 1));
		sb.append("]");
		appendTextWithNewLine(sb.toString(), Get_Part);
	}
	private void miningInit(People.PeopleData people, People.PickaxeData pickaxe, People.BagData bag) {
		Log.i("peopleMining panel", "mining init");
		chunkX = 0;
		chunkY = 0;
		blockInChunkX = 0;
		blockInChunkY = 0;
		miningCount = 0;
		stamina = people.lastStamina;
		damage = pickaxe.damage;
		mineral = new EnumMap<>(Mineral.class);
		items = new HashSet<>();
		appendTextWithNewLine(status(people, pickaxe, bag), Status);
	}
	private boolean checkMining(People.PickaxeData pickaxe, People.BagData bag) {
		if(stamina <= 0) {
			appendTextWithNewLine("體力不足", Error);
			return false;
		}
		if(damage >= pickaxe.maxDamage) {
			if(damage > pickaxe.maxDamage) damage = pickaxe.maxDamage;
			appendTextWithNewLine("工具毀損", Error);
			return false;
		}
		if(countItems() >= bag.maxSpace) {
			appendTextWithNewLine("包包空間不足", Error);
			return false;
		}
		return true;
	}
	private String status(People.PeopleData people, People.PickaxeData pickaxe, People.BagData bag) {
		return "座標:(" + blockInChunkX + "," + blockInChunkY + ") in (" + chunkX + "," + chunkY + ")" +
				"\n體力:" + stamina + "/" + people.maxStamina +
				"\n毀損值:" + damage + "/" + pickaxe.maxDamage +
				"\n背包:" + countItems() + "/" + bag.maxSpace;
	}
	private int countItems() {
		int count = items.size();
		for(Mineral m : mineral.keySet()) {
			count += mineral.get(m);
		}
		return count;
	}
	private void countDown(JButton needLock, int time, Direct d, People.PeopleData people, People.PickaxeData pickaxe, People.BagData bag) {
		needLock.setEnabled(false);
		new Thread(() -> {
			long ft = System.currentTimeMillis();
			while((System.currentTimeMillis() - ft) < time) {
				needLock.setText(String.format("挖(%.2f)", (time - (System.currentTimeMillis() - ft)) / 1000f));
			}
			doMine(d, people, pickaxe, bag);
			needLock.setEnabled(true);
			needLock.setText("挖");
		}).start();
	}
	
}
