package hang_up_game.java.window;

import java.awt.*;

public class Constant {
    
    public static Point getMiddleWindowPoint(int width, int height) {
        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        return new Point(p.x - width / 2, p.y - height / 2);
    }
    
    public static Rectangle getMiddleWindowRectangle(int width, int height) {
        return new Rectangle(getMiddleWindowPoint(width, height), new Dimension(width, height));
    }
    
    
    
    private Constant() {
        throw new IllegalStateException("This class is just use static method.");
    }

}
