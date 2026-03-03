package staffEditor; //msToggleButton

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import java.net.URL;

public class IconButton extends JButton{
    
    Toolbar parent;

    ClassLoader cldr;
    URL imageURL;
    public ImageIcon icon;
    
    IconButton(Toolbar p) {
        // super();
        parent = p;
        cldr = this.getClass().getClassLoader();
        imageURL = this.cldr.getResource("images/save.png");
        icon =new ImageIcon(imageURL);
        
        UIManager.put("ToolTip.background", Color.DARK_GRAY);
        UIManager.put("ToolTip.foreground", Color.WHITE);
        UIManager.put("ToolTip.font", new FontUIResource("SansSerif", Font.BOLD, 14));
        
        this.setFocusable(false);
        this.setToolTipText("py");
        this.setIcon(icon);
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doSomething();
            }
        });
    }


    public void doSomething() {}
}

