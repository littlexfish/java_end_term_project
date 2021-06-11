package hang_up_game.java.window;

import hang_up_game.java.game.Background;
import hang_up_game.java.io.FileChecker;
import hang_up_game.java.io.data.FileHolder;
import org.lf.logger.Log;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImportFile extends JDialog {
	
	public static final int Confirm = 1;
	public static final int Error = 0;
	public static final int Cancel = -1;
	
	private final JLabel msg = new JLabel();
	private File importFile;
	private int option = Error;
	
	private ImportFile(JFrame parent, FileChecker fc) {
		super(parent, true);
		setBounds(Constant.getMiddleWindowRectangle(300, 200));
		importFile = fc.getImport(0);
		
		JPanel root = new JPanel(new BorderLayout(5, 5));
		
		msg.setText("<html>發現可導入的檔案<br>檔名:" + importFile.getName() + "<br>請問是否導入</html>");
		root.add(msg, BorderLayout.CENTER);
		setContentPane(root);
		
		JPanel buttons = new JPanel(new GridLayout(1, 3, 5, 5));
		root.add(buttons, BorderLayout.SOUTH);
		
		JButton confirm = new JButton("確定");
		confirm.addActionListener(e -> {
			Log.d("import", "confirm");
			try {
				if(!fc.extractZIP(importFile)) {
					Log.i("import", "has some file fail");
					Background.throwMsg("導入", "有部分檔案導入失敗，取消導入作業");
					option = Cancel;
				}
				else {
					option = Confirm;
				}
			}
			catch(IOException ioException) {
				ioException.printStackTrace(FileHolder.getExportCrashReport());
				ioException.printStackTrace();
			}
		});
		buttons.add(confirm);
		if(!fc.checkImportIsOnlyOne()) {
			JButton choose = new JButton("選擇其他檔案");
			choose.addActionListener(e -> {
				Log.d("import", "choose other file");
				JFileChooser jfc = new JFileChooser("./");
				jfc.addChoosableFileFilter(new FileNameExtensionFilter("miner檔案", "miner"));
				jfc.setMultiSelectionEnabled(false);
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.setDialogType(JFileChooser.OPEN_DIALOG);
				jfc.setSelectedFile(importFile);
				int get = jfc.showOpenDialog(null);
				if(get == JFileChooser.APPROVE_OPTION) {
					importFile = jfc.getSelectedFile();
					msg.setText("<html>發現可導入的檔案<br>檔名:" + importFile.getName() + "<br>請問是否導入</html>");
				}
			});
			buttons.add(choose);
		}
		JButton cancel = new JButton("取消");
		cancel.addActionListener(e -> {
			Log.d("import", "cancel");
			option = Cancel;
		});
		buttons.add(cancel);
		
		
	}
	
	public int getOption() {
		return option;
	}
	
	public static int openDialog(JFrame parent, FileChecker fc) {
		Log.d("import", "open check dialog");
		ImportFile dialog = new ImportFile(parent, fc);
		dialog.setVisible(true);
		while(dialog.getOption() == Error);
		dialog.dispose();
		return dialog.getOption();
	}
	
}
