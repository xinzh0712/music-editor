package staffEditor;

import java.awt.*;
import javax.swing.*;

public class Toolbar extends JPanel{
	
	private static final long serialVersionUID = 1L;//因為 Swing 元件可序列化，IDE 建議你加 ID 以避免未來版本不一致問題。

	MainWindow parent;
	TopToolbar topToolbar;
	private TabbedPane tabbedPane;
	
	longType longtype;
	inputType inputtype;
	Toolbar(MainWindow p) {
	    parent = p;
		longtype = longType.non;
		inputtype = inputType.Cursor;

	    topToolbar = new TopToolbar(this);
	    this.setLayout(new BorderLayout());
	    //this.setBackground(Color.darkGray);
	    this.add(topToolbar,BorderLayout.CENTER);
	    this.setPreferredSize(new Dimension(0,95));
	    
	    this.tabbedPane = p.getTabbedPane();

	    this.tabbedPane = p.getTabbedPane();
	    
	}

    public MainWindow getMainWindow() 
    {
        return parent;
    }

    //讓其他類別能夠訪問 TabbedPane
    public TabbedPane getTabbedPane() {
        return tabbedPane;
    }

}
