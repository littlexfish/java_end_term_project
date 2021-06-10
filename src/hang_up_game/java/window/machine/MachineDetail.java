package hang_up_game.java.window.machine;

import hang_up_game.java.game.Background;
import hang_up_game.java.game.MachineMiner;
import hang_up_game.java.window.GameFrame;
import hang_up_game.java.window.WarningWindow;
import org.lf.logger.Log;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class MachineDetail extends JPanel {
	
	private Set<MachineMiner> on_air;
	private final JList<MachineMiner> mmList;
	private final GameFrame parent;
	
	public MachineDetail(GameFrame window) {
		Log.d("machineDetail panel", "init");
		parent = window;
		
		on_air = Background.getAllMinerOnline();
		
		//layout
		mmList = new JList<>(on_air.toArray(new MachineMiner[0]));
		mmList.setPreferredSize(new Dimension(400, 250));
		add(mmList, BorderLayout.CENTER);
		
		JPanel down = new JPanel(new GridLayout(1, 2, 5, 5));
		add(down, BorderLayout.SOUTH);
		
		JButton check = new JButton("確認挖礦機");
		check.setFocusable(false);
		check.addActionListener(e -> {
			if(mmList.isSelectionEmpty()) return;
			if(on_air.size() > 0) {
				parent.setPanel(new MachineCheck(parent, this, mmList.getSelectedValue()), "確認挖礦機");
			}
		});
		down.add(check);
		
		JButton go = new JButton("新增挖礦機");
		go.setFocusable(false);
		go.addActionListener(e -> {
			try {
				parent.setPanel(new MachineGo(parent, this), "新增挖礦機");
			}
			catch(RuntimeException ex) {
				new WarningWindow("無法新增挖礦機", ex.getMessage(), WarningWindow.DialogType_Time, JFrame.DISPOSE_ON_CLOSE, 1000).setVisible(true);
			}
		});
		down.add(go);
		
		if(on_air.size() > 0) {
			mmList.setSelectedIndex(0);
		}
		
	}
	
	public void changeOnAir(Set<MachineMiner> onAir) {
		on_air = onAir;
		mmList.setListData(on_air.toArray(new MachineMiner[0]));
		mmList.invalidate();
	}
	
}
