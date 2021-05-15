package hang_up_game.java.window;

import hang_up_game.java.io.Log;

import javax.swing.*;
import java.awt.*;

public class WarningWindow extends JDialog {
	
	public static final int defaultDelayTime = 3000;
	
	public static final int DialogType_Time    = 0b000001;
	public static final int DialogType_CONFIRM = 0b000010;
	public static final int DialogType_CANCEL  = 0b000100;
	
	public static final int Result_OK = 1;
	public static final int Result_CANCEL = 2;
	public static final int Result_ERROR = -1;
	public static final int Result_OTHER = 3;
	public static final int Result_NONE = 0;
	
	public int result = Result_NONE;
	public int CloseType;
	public boolean isTimeType = false;
	public int delay;
	
	public WarningWindow(String title, String msg, int dialogType, int closeType, int time) {
		Log.i("warning panel", "warning " + title + ", msg: " + msg);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(closeType);
		setTitle(title);
		CloseType = closeType;
		
		JLabel Msg = new JLabel(msg);
		Msg.setForeground(Color.ORANGE);
		Msg.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(Msg, BorderLayout.CENTER);
		
		if((dialogType & DialogType_Time) == DialogType_Time) {
			setBounds(Constant.getMiddleWindowRectangle(msg.length() * 20, 50));
			setUndecorated(true);
			isTimeType = true;
			if(time <= 0) {
				delay = defaultDelayTime;
			}
			else {
				delay = time;
			}
		}
		else {
			setBounds(Constant.getMiddleWindowRectangle(msg.length() * 10, 100));
			boolean isOdd = true;
			JPanel buttonHolder = new JPanel();
			getContentPane().add(buttonHolder, BorderLayout.SOUTH);
			if((dialogType & DialogType_CONFIRM) == DialogType_CONFIRM) {
				isOdd = false;
				JButton confirm = new JButton("確定");
				confirm.addActionListener(event -> {
					result = Result_OK;
					this.dispose();
				});
				buttonHolder.add(confirm, BorderLayout.EAST);
			}
			if((dialogType & DialogType_CANCEL) == DialogType_CANCEL) {
				isOdd = false;
				JButton cancel = new JButton("確定");
				cancel.addActionListener(event -> {
					result = Result_CANCEL;
					this.dispose();
				});
				buttonHolder.add(cancel, BorderLayout.WEST);
			}
			if(isOdd) {
				throw new IllegalArgumentException("Dialog Type not type of time, must have at least one button");
			}
		}
		
	}
	
	public int getResult() {
		return result;
	}
	
	public boolean isChooseButton() {
		return result != Result_NONE && result != Result_ERROR;
	}
	
	public void waitToChooseButton() {
		while(!isChooseButton()) {
			if(getResult() == Result_ERROR) {
				throw new RuntimeException("error when warning");
			}
		}
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if(isTimeType) delayClose();
	}
	
	private void delayClose() {
		WarningWindow f = this;
		new Thread(() -> {
			long ft = System.currentTimeMillis();
			//noinspection StatementWithEmptyBody
			while((System.currentTimeMillis() - ft) < delay);
			if(CloseType == JFrame.EXIT_ON_CLOSE) {
				System.exit(0);
			}
			else {
				f.dispose();
			}
		}).start();
	}
	
	/**
	 *
	 * @return true if click ok, or false else
	 */
	public static boolean openDialogAndWait(String title, String msg) {
		WarningWindow ww = new WarningWindow(title, msg, DialogType_CONFIRM | DialogType_CANCEL, JFrame.DISPOSE_ON_CLOSE, 0);
		ww.setVisible(true);
		ww.waitToChooseButton();
		return ww.getResult() == Result_OK;
	}
	
}
