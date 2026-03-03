package staffEditor;

import org.jfugue.player.Player;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class MainWindow extends JFrame {
    SECreate parent;
    Dimension size = new Dimension(935, 700);

    public Toolbar toolbar;
    public InsMenu instrumentMenu;
    public TabbedPane tabbedPane;

    public MainWindow(SECreate p) {
        parent = p;

        this.setSize(size);
        
        
        tabbedPane = new TabbedPane(this);
        toolbar = new Toolbar(this);
        instrumentMenu = new InsMenu(this);
        this.setLayout(new BorderLayout());

        this.add(toolbar, BorderLayout.NORTH);
        this.add(instrumentMenu, BorderLayout.EAST);
        this.add(tabbedPane, BorderLayout.CENTER);

        this.setLocation(25, 50);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    
    // 新增方法：獲取當前所有的 StaffPage
    public List<StaffPage> getAllStaffPages() {
        return tabbedPane.getAllStaffPages();
    }
    
    public TabbedPane getTabbedPane() {
        return tabbedPane;
    }
    public InsList getInstrumentList() {
        return instrumentMenu.instrumentList;
    }



}
