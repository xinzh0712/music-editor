package staffEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class QuarterButton extends IconButton{
    Toolbar parent;
    ImageIcon imageIcon;
    private JPanel rightPanel;

	public QuarterButton(Toolbar p, JPanel rightPanel) 
	{
		super(p);
		parent=p;
		this.rightPanel = rightPanel; 
        imageURL   = cldr.getResource("images/quarter_note.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("四分音符");
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateBtnColor();
            }
        });
	}

	public void doSomething() {
	    // 檢查 parent 是否為 null
	    if (parent == null) {
	        System.out.println("Error: parent is null");
	        return;
	    }
	    if (parent.inputtype == inputType.Cursor) {
	        System.out.println("Input type is Cursor, exiting doSomething");
	        return;
	    }

	    
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    // 初始化 icon 和 imageIcon
	    if (imageURL == null) {
	        return;
	    }
	    icon = new ImageIcon(imageURL);
	    imageIcon = new ImageIcon(icon.getImage().getScaledInstance(30, 45, Image.SCALE_DEFAULT));
	    
	    Cursor cu = tk.createCustomCursor(imageIcon.getImage(), new Point(16, 29), "Quarter Note Cursor");
	    parent.longtype = longType.quarter;
	    // 設置所有 tab 的鼠標
	    if (parent.parent == null) 
	    {
	        return;
	    }
	    if (parent.parent.tabbedPane == null) 
	    {
	        return;
	    }
	    int tabCount = parent.parent.tabbedPane.getTabCount();
	    for (int i = 0; i < tabCount; i++) {
	        Component tab = parent.parent.tabbedPane.getComponentAt(i);
	        if (tab == null) 
	        {
	            System.out.println("Error: Tab at index " + i + " is null");
	        } else 
	        {
	            tab.setCursor(cu);
	        }
	    }
	}
	
	private void updateBtnColor()
	{
		for (Component btn : rightPanel.getComponents()) 
		{
            if (btn instanceof JButton) 
            {
                ((JButton) btn).setBackground(Color.WHITE);
            }
        }
		
		this.setBackground(Color.LIGHT_GRAY);
	}
}