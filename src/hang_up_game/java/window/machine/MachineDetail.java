package hang_up_game.java.window.machine;

import hang_up_game.java.game.Background;
import hang_up_game.java.game.MachineMiner;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class MachineDetail extends JPanel {
	
	private Set<MachineMiner> on_air;
	private JList<MachineMiner> mmList;
	private final JFrame parent;
	
	public MachineDetail(JFrame window) {
		parent = window;
		
		on_air = Background.getAllMinerOnline();
		
		//layout
		mmList = new JList<>(on_air.toArray(new MachineMiner[0]));
		add(mmList, BorderLayout.CENTER);
		
		JPanel down = new JPanel(new GridLayout(1, 2, 5, 5));
		add(down, BorderLayout.SOUTH);
		
		JButton check = new JButton("確認挖礦機");
		down.add(check);
		
		JButton go = new JButton("新增挖礦機");
		down.add(go);
		
	}
	
	public void changeOnAir(Set<MachineMiner> onAir) {
		on_air = onAir;
		mmList.setListData(on_air.toArray(new MachineMiner[0]));
		mmList.invalidate();
	}
	
}
