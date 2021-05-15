package hang_up_game.java.window.machine;

import hang_up_game.java.game.Background;
import hang_up_game.java.game.MachineMiner;
import hang_up_game.java.io.Log;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.Plugin;
import hang_up_game.java.io.data.storage.Machine;
import hang_up_game.java.window.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MachineGo extends JPanel {

	private final MachineDetail detail;
	private final GameFrame frame;
	
	private final JLabel engineText = new JLabel();
	private final JLabel headText = new JLabel();
	private final JLabel batteryText = new JLabel();
	private final JLabel chestText = new JLabel();
	private final JLabel pluginText = new JLabel();
	
	public MachineGo(GameFrame gf, MachineDetail md) {
		Log.d("machineGo panel", "init");
		detail = md;
		frame = gf;
		
		setLayout(new BorderLayout(5, 5));
		
		//layout
		
		JPanel center = new JPanel(new GridLayout(2, 1, 5, 5));
		add(center, BorderLayout.CENTER);
		
		JPanel up = new JPanel(new GridLayout(1, 5, 5, 5));
		center.add(up);
		
		JScrollPane engineS = new JScrollPane();
		up.add(engineS);
		
		Machine.Engine[] es = FileHolder.machine.getUsableEngine().toArray(new Machine.Engine[0]);
		if(es.length <= 0) throw new RuntimeException("no engine");
		JList<Machine.Engine> engineList = new JList<>(es);
		engineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		engineList.setSelectedIndex(0);
		engineList.addListSelectionListener(e -> engineText.setText(toHtml(engineList.getSelectedValue().list())));
		engineS.setViewportView(engineList);
		
		JScrollPane headS = new JScrollPane();
		up.add(headS);
		
		Machine.Head[] hs = FileHolder.machine.getUsableHead().toArray(new Machine.Head[0]);
		if(hs.length <= 0) throw new RuntimeException("no head");
		JList<Machine.Head> headList = new JList<>(hs);
		headList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		headList.setSelectedIndex(0);
		headList.addListSelectionListener(e -> headText.setText(toHtml(headList.getSelectedValue().list())));
		headS.setViewportView(headList);
		
		JScrollPane batteryS = new JScrollPane();
		up.add(batteryS);
		
		Machine.Battery[] bs = FileHolder.machine.getUsableBattery().toArray(new Machine.Battery[0]);
		if(bs.length <= 0) throw new RuntimeException("no battery");
		JList<Machine.Battery> batteryList = new JList<>(bs);
		batteryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		batteryList.setSelectedIndex(0);
		batteryList.addListSelectionListener(e -> batteryText.setText(toHtml(batteryList.getSelectedValue().list())));
		batteryS.setViewportView(batteryList);
		
		JScrollPane chestS = new JScrollPane();
		up.add(chestS);
		
		Machine.Chest[] cs = FileHolder.machine.getUsableChest().toArray(new Machine.Chest[0]);
		if(cs.length <= 0) throw new RuntimeException("no chest");
		JList<Machine.Chest> chestList = new JList<>(cs);
		chestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		chestList.setSelectedIndex(0);
		chestList.addListSelectionListener(e -> chestText.setText(toHtml(chestList.getSelectedValue().list())));
		chestS.setViewportView(chestList);
		
		JScrollPane pluginS = new JScrollPane();
		up.add(pluginS);
		
		Plugin[] ps = FileHolder.machine.getUsablePlugin().toArray(new Plugin[0]);
		JList<Plugin> pluginList = new JList<>(ps);
		if(ps.length > 0) {
			pluginList.setSelectedIndex(0);
		}
		pluginList.addListSelectionListener(e -> pluginText.setText(toHtml(pluginList.getSelectedValuesList())));
		pluginS.setViewportView(pluginList);
		
		JPanel down = new JPanel(new GridLayout(1, 5, 5, 5));
		center.add(down);
		
		engineText.setText(toHtml(engineList.getSelectedValue().list()));
		down.add(engineText);
		headText.setText(toHtml(headList.getSelectedValue().list()));
		down.add(headText);
		batteryText.setText(toHtml(batteryList.getSelectedValue().list()));
		down.add(batteryText);
		chestText.setText(toHtml(chestList.getSelectedValue().list()));
		down.add(chestText);
		down.add(pluginText);
		
		JButton go = new JButton("開始挖礦");
		go.setFocusable(false);
		go.addActionListener(e -> {
			new MachineMiner(engineList.getSelectedValue(), headList.getSelectedValue(), batteryList.getSelectedValue(),
					chestList.getSelectedValue(), pluginList.getSelectedValuesList().toArray(new Plugin[0])).startMining();
			detail.changeOnAir(Background.getAllMinerOnline());
			frame.goBack();
		});
		add(go, BorderLayout.SOUTH);
		
	}
	
	public String toHtml(String old) {
		return "<html>".concat(old).replace("\n", "<br>").concat("</html>");
	}
	
	public String toHtml(List<Plugin> ps) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		for(Plugin p : ps) {
			sb.append(p.name).append("<br>");
		}
		return sb.substring(0, sb.length() - 4) + "</html>";
	}
	
}
