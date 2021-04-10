package hang_up_game.java.window;

import hang_up_game.java.io.FileChecker;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Open extends JFrame {


    public Open() {
        setBounds(Constant.getMiddleWindowRectangle(100, 50));
        setUndecorated(true);
        
        JLabel load = new JLabel("Loading...");
        load.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(load, BorderLayout.CENTER);
        
    }
    
}
