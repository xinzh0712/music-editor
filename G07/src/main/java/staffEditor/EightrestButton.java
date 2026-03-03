package staffEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EightrestButton extends IconButton {
    Toolbar parent;
    ImageIcon imageIcon;
    private JPanel rightRest;

	public EightrestButton(Toolbar p, JPanel rightRest) 
	{
		super(p);
		parent=p;
		this.rightRest = rightRest;
        imageURL   = cldr.getResource("images/eight-note-rest.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);

        this.setToolTipText("八分休止符");
        
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

	    // 創建 Toolkit
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    // 初始化 icon 和 imageIcon
	    if (imageURL == null) {
	        return;
	    }
	    icon = new ImageIcon(imageURL);
	    imageIcon = new ImageIcon(icon.getImage().getScaledInstance(18, 24, Image.SCALE_DEFAULT));
	    // 創建自定義鼠標
	    Cursor cu = tk.createCustomCursor(imageIcon.getImage(), new Point(16, 16), "Quarter Note Cursor");
	    parent.longtype = longType.eighthR;
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
		for (Component btn : rightRest.getComponents()) 
		{
            if (btn instanceof JButton) 
            {
                ((JButton) btn).setBackground(Color.WHITE);
            }
        }
		
		this.setBackground(Color.LIGHT_GRAY);
	}
}
