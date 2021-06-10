package hang_up_game.java.window;

import org.lf.logger.Log;

import javax.swing.*;
import java.awt.*;

public class Open extends JFrame {


    public Open() {
        Log.d("open", "init");
        setBounds(Constant.getMiddleWindowRectangle(100, 50));
        setUndecorated(true);
        
        JLabel load = new JLabel("Loading...");
        load.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(load, BorderLayout.CENTER);
        
    }
    
}
