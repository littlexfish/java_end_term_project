package hang_up_game.java.window.people;

import hang_up_game.java.game.Background;
import hang_up_game.java.io.Log;
import hang_up_game.java.io.data.FileHolder;
import hang_up_game.java.io.data.storage.People;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PeopleDetail extends JPanel {
	
	public ArrayList<People.PeopleData> allPeople = new ArrayList<>(FileHolder.people.peopleData);
	public ArrayList<People.PickaxeData> allPickaxe = new ArrayList<>(FileHolder.people.pickaxeData);
	public ArrayList<People.BagData> allBag = new ArrayList<>(FileHolder.people.bagData);
	
	public JList<People.PeopleData> peopleList = new JList<>();
	public JList<People.PickaxeData> pickaxeList = new JList<>();
	public JList<People.BagData> bagList = new JList<>();
	
	public JLabel peopleLabel = new JLabel();
	public JLabel pickaxeLabel = new JLabel();
	public JLabel bagLabel = new JLabel();
	private final JFrame parent;
	
	public PeopleDetail(JFrame window) {
		Log.d("peopleDetail panel", "init");
		parent = window;
		setLayout(new BorderLayout());
		
		JPanel mainP = new JPanel(new GridLayout(2, 1, 5, 5));
		add(mainP, BorderLayout.CENTER);
		
		JPanel listP = new JPanel(new GridLayout(1, 3, 5, 5));
		mainP.add(listP);
		
		//people
		peopleList.setListData(allPeople.toArray(new People.PeopleData[0]));
		peopleList.setSelectedIndex(0);
		peopleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		peopleList.addListSelectionListener(e -> peopleLabel.setText(toHtml(peopleList.getSelectedValue().list())));
		peopleLabel.setText(toHtml(peopleList.getSelectedValue().list()));
		listP.add(peopleList);
		
		//pickaxe
		pickaxeList.setListData(allPickaxe.toArray(new People.PickaxeData[0]));
		pickaxeList.setSelectedIndex(0);
		pickaxeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pickaxeList.addListSelectionListener(e -> pickaxeLabel.setText(toHtml(pickaxeList.getSelectedValue().list())));
		pickaxeLabel.setText(toHtml(pickaxeList.getSelectedValue().list()));
		listP.add(pickaxeList);
		
		
		//bag
		bagList.setListData(allBag.toArray(new People.BagData[0]));
		bagList.setSelectedIndex(0);
		bagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bagList.addListSelectionListener(e -> bagLabel.setText(toHtml(bagList.getSelectedValue().list())));
		bagLabel.setText(toHtml(bagList.getSelectedValue().list()));
		listP.add(bagList);
		
		//showDetail
		JScrollPane jScrollPane = new JScrollPane();
		mainP.add(jScrollPane);
		
		JPanel labelP = new JPanel(new GridLayout(1, 3, 5, 5));
		jScrollPane.setViewportView(labelP);
		
		labelP.add(peopleLabel);
		labelP.add(pickaxeLabel);
		labelP.add(bagLabel);
		
		//button
		
		JButton confirm = new JButton("開始挖礦");
		confirm.setFocusable(false);
		confirm.addActionListener(e -> openPeopleMining());
		add(confirm, BorderLayout.SOUTH);
		
	}
	
	public String toHtml(String old) {
		return "<html>".concat(old).replace("\n", "<br>").concat("</html>");
	}
	
	private void openPeopleMining() {
		PeopleMining pm = new PeopleMining(this, peopleList.getSelectedValue(), pickaxeList.getSelectedValue(), bagList.getSelectedValue());
		Background.stopRecover();
		pm.setVisible(true);
	}
	
	public JFrame getOwner() {
		return parent;
	}
	
}
