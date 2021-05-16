package hang_up_game.java.window.menu_bar;

import hang_up_game.java.io.Log;
import hang_up_game.java.window.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Manual extends JFrame {
	
	private final ArrayList<ImageIcon> images = new ArrayList<>(8);
	private final Thread eggThread;
	private boolean isReset = false;
	private volatile boolean isStop = false;
	private long ft = System.currentTimeMillis();
	private final int eggClick = 50;
	private int clickCount = 0;
	private int index = 0;
	
	public Manual() {
		Log.d("manual panel", "init");
		//add image
		Log.d("manual panel", "manual image init");
		images.add(Constant.getIcon("manual/0"));
		images.add(Constant.getIcon("manual/1"));
		images.add(Constant.getIcon("manual/2"));
		images.add(Constant.getIcon("manual/3"));
		images.add(Constant.getIcon("manual/4"));
		images.add(Constant.getIcon("manual/5"));
		images.add(Constant.getIcon("manual/6"));
		images.add(Constant.getIcon("manual/7"));
		images.add(Constant.getIcon("manual/egg"));
		
		int size = images.size();
		
		//layout
		setResizable(false);
		setTitle("說明");
		setIconImage(Constant.getImage("help"));
		setBounds(Constant.getMiddleWindowRectangle(875, 690));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(5, 5));
		
		JLabel imgP = new JLabel();
		imgP.setIcon(images.get(0));
		getContentPane().add(imgP, BorderLayout.CENTER);
		
		eggThread = new Thread(() -> {
			while(true) {
				//noinspection StatementWithEmptyBody
				while(isStop);
				if(isReset) {
					ft = System.currentTimeMillis();
					isReset = false;
					clickCount = 0;
					Log.d("manual panel", "egg reset");
				}
				if((System.currentTimeMillis() - ft) > 1000) {
					isReset = true;
				}
			}
		});
		
		JPanel change = new JPanel(new BorderLayout(5, 5));
		getContentPane().add(change, BorderLayout.SOUTH);
		
		JLabel page = new JLabel((index + 1) + "/" + (size - 1));
		page.setHorizontalAlignment(SwingConstants.CENTER);
		change.add(page, BorderLayout.CENTER);
		
		JButton right = new JButton(">");
		right.setFocusable(false);
		right.addActionListener(e ->  {
			if(index >= size - 2) {
				if(index >= size - 1) {
					return;
				}
				if(clickCount >= eggClick) {
					index++;
					isStop = true;
					clickCount = 0;
					imgP.setIcon(images.get(index));
					page.setText((index + 1) + "/" + (size - 1));
					right.setEnabled(false);
				}
				else {
					ft = System.currentTimeMillis();
					if(!eggThread.isAlive()) {
						Log.d("manual panel", "egg thread start");
						eggThread.start();
					}
					isStop = false;
					clickCount++;
				}
				return;
			}
			index++;
			imgP.setIcon(images.get(index));
			page.setText((index + 1) + "/" + (size - 1));
		});
		change.add(right, BorderLayout.EAST);
		
		JButton left = new JButton("<");
		left.setFocusable(false);
		left.addActionListener(e -> {
			if(index <= 0) return;
			if(index < size - 1) {
				isStop = true;
			}
			index--;
			imgP.setIcon(images.get(index));
			page.setText((index + 1) + "/" + (size - 1));
			right.setEnabled(true);
		});
		change.add(left, BorderLayout.WEST);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				isStop = true;
				eggThread.interrupt();
			}
		});
		
	}
	
	public static void openManual() {
		new Manual().setVisible(true);
	}
	
}
