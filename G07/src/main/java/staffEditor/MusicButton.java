package staffEditor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class MusicButton extends IconButton{
	
	
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel rightRest;
	private TopToolbar parentTopToolbar;

	public MusicButton(Toolbar p, JPanel leftPanel, JPanel rightPanel, JPanel rightRest, TopToolbar parentTopToolbar) 
	{
		super(p);
		
		this.leftPanel = leftPanel;
		this.rightPanel = rightPanel; 
		this.rightRest = rightRest;
		this.parentTopToolbar = parentTopToolbar;
		
        imageURL   = cldr.getResource("images/music-note.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("選擇音符");
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	parentTopToolbar.showNoteTools(true, false);
                enableRightButtons();
                updateRightPanel();
                updateBtnColor();
                MainWindow mainWindow = parent.parent;
            }
        });
        
	}
    public void doSomething(){
        parentTopToolbar.setLengthEnable(true);
        if(parentTopToolbar.inputtype != inputType.Note) { //如果不是音符模式
            // parentTopToolbar.resetlongButtongroup();
            parentTopToolbar.longtype=longType.non;
            for(int i=0;i<parentTopToolbar.parent.parent.tabbedPane.getTabCount();i++) {
                parentTopToolbar.parent.parent.tabbedPane.getComponentAt(i).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

        }

        parent.inputtype = inputType.Note; //切換成音符模式
    }

	private void enableRightButtons() 
	{		
		for (Component btn : rightPanel.getComponents()) 
		{
            if (btn instanceof JButton) 
            {
                ((JButton) btn).setEnabled(true);  
            }
        }
    }
	
	public void updateRightPanel() 
	{
		parentTopToolbar.removeAll();

		parentTopToolbar.add(leftPanel, BorderLayout.WEST);
        parentTopToolbar.add(rightPanel, BorderLayout.EAST);
        
        parentTopToolbar.revalidate();
        parentTopToolbar.repaint();

	}
	
	public void updateBtnColor()
	{
		for (Component btn : leftPanel.getComponents()) 
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
