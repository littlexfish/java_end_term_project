package hang_up_game.java.window;

import hang_up_game.java.window.machine.MachineDetail;
import hang_up_game.java.window.people.PeopleDetail;

import javax.swing.*;
import java.awt.*;

public class Mining extends JPanel {
	
	private final GameFrame frame;
	
	public Mining(GameFrame f) {
		frame = f;
		
		JPanel centerButton = new JPanel(new GridLayout(8, 1, 5, 5));
		centerButton.setPreferredSize(new Dimension(400, 300));
		add(centerButton, BorderLayout.CENTER);
		
		JLabel upPadding = new JLabel();
		centerButton.add(upPadding);
		
		JButton people = new JButton("人工挖礦");
		people.setFocusable(false);
		people.addActionListener(e -> frame.setPanel(new PeopleDetail(frame), "挖礦準備"));
		centerButton.add(people);
		
		JLabel centerPadding1 = new JLabel();
		centerButton.add(centerPadding1);
		
		JButton machine = new JButton("機器挖礦");
		machine.setFocusable(false);
		machine.addActionListener(e -> frame.setPanel(new MachineDetail(frame), "正在挖礦中的機器"));
		centerButton.add(machine);
		
	}
	

}
