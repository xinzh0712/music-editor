package staffEditor;

import javax.swing.*;

public class NewPageButton extends IconButton{

	NewPageButton(Toolbar p) 
	{
		super(p);
        imageURL   = cldr.getResource("images/add-file.png");
        icon = new ImageIcon(imageURL);
        this.setIcon(icon);
        this.setToolTipText("新增頁面");
	}

    public void doSomething(){

        this.parent.parent.tabbedPane.addTab("page",new StaffPage(this.parent.parent.tabbedPane));

       if(this.parent.parent.tabbedPane.getTabCount()==1) {

           //parent.parent.toolbar.editBar.setTypeEnable(true);
           //parent.parent.toolbar.editBar.setLengthEnable(false);
       }

   }
}
