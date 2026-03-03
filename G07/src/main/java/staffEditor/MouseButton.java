package staffEditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MouseButton extends IconButton{
	
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel rightRest;
	private TopToolbar parentTopToolbar;

	public MouseButton(Toolbar p, JPanel leftPanel, JPanel rightPanel, JPanel rightRest,  TopToolbar parentTopToolbar) 
	{
		super(p);
		this.leftPanel = leftPanel;
		this.rightPanel = rightPanel; 
		this.rightRest = rightRest;
        imageURL   = cldr.getResource("images/direct-selection.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("滑鼠");
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	parentTopToolbar.showNoteTools(false, false);
                disableRightButtons();
                updateBtnColor();
                MainWindow mainWindow = parent.parent;
                resetCursor();
            }
        });
	}
	
		
	private void disableRightButtons() 
	{		
		for (Component btn : rightPanel.getComponents()) 
		{
            if (btn instanceof JButton) 
            {
                ((JButton) btn).setEnabled(false);  
            }
        }
		
		for (Component btn : rightRest.getComponents()) 
		{
            if (btn instanceof JButton) 
            {
                ((JButton) btn).setEnabled(false);  
            }
        }
    }
	
    // 將滑鼠切換回預設鼠標
    public void resetCursor() {
        // System.out.println("clicked!");
        Cursor cu = new Cursor(Cursor.DEFAULT_CURSOR);
        for(int i=0;i<parent.parent.tabbedPane.getTabCount();i++) {
            parent.parent.tabbedPane.getComponentAt(i).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        parent.inputtype = inputType.Cursor;
        parent.longtype=longType.non;

        parent.topToolbar.setLengthEnable(false);
    }

	
	private void updateBtnColor()
	{
		for (Component btn : leftPanel.getComponents()) 
		{
            if (btn instanceof JButton) 
            {
                ((JButton) btn).setBackground(Color.WHITE);
            }
        }
		
		for (Component btn : rightPanel.getComponents()) 
		{
            if (btn instanceof JButton) 
            {
                ((JButton) btn).setBackground(Color.WHITE);
            }
        }
		
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
