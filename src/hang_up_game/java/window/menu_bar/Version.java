package hang_up_game.java.window.menu_bar;

import hang_up_game.java.game.Background;
import hang_up_game.java.window.Constant;
import org.lf.logger.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Version extends JDialog {
	
	public Version(JFrame owner) {
		super(owner, true);
		Log.d("machineDetail panel", "init");
		
		setTitle("關於");
		setBounds(Constant.getMiddleWindowRectangle(500, 150));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(5, 5));
		
		JLabel ver = new JLabel(Constant.detail);
		ver.setHorizontalAlignment(SwingConstants.CENTER);
		ver.setFont(new Font("Arial", Font.BOLD, 18));
		ver.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
					StringSelection ss = new StringSelection(Constant.github);
					clip.setContents(ss, null);
					Log.d("info", "copy \"" + Constant.github + "\" to clipboard");
					Background.throwMsg("關於", "已複製至剪貼簿");
				}
			}
		});
		getContentPane().add(ver, BorderLayout.CENTER);
		
	}
	
}
