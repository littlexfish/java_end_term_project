package hang_up_game.java.window;

import javax.swing.*;
import java.awt.*;

public class Constant {
	
	public static final String detail = "<html>Version: beta 1.0<br>Create by: Little Fish</html>";
	public static final Image icon = Toolkit.getDefaultToolkit().getImage(Constant.class.getResource("/hang_up_game/files/img/icon.png"));
	public static final String ApplicationName = "Miner";
	
	public static Point getMiddleWindowPoint(int width, int height) {
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		return new Point(p.x - width / 2, p.y - height / 2);
	}
	
	public static Rectangle getMiddleWindowRectangle(int width, int height) {
		return new Rectangle(getMiddleWindowPoint(width, height), new Dimension(width, height));
	}
	
	public static Image getImage(String path) {
		return Toolkit.getDefaultToolkit().getImage(Constant.class.getResource("/hang_up_game/files/img/" + path + ".png"));
	}
	
	public static ImageIcon getIcon(String path) {
		return new ImageIcon(getImage(path));
	}
	
	public static ImageIcon getIcon(String path, int width, int height) {
		return new ImageIcon(getImage(path).getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING));
	}
	
	private Constant() {
		throw new IllegalStateException("This class is just use static method.");
	}
	
	
}
