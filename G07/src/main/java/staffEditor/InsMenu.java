package staffEditor;

import javax.swing.*;
import java.awt.*;

public class InsMenu extends JPanel {
    MainWindow parent;
    InsList instrumentList;

    public InsMenu(MainWindow p) {
        parent = p;

        // 設置主佈局
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(200, 0));

        // ======= 樂器列表 =======
        instrumentList = new InsList(this);

        // 添加到面板
        this.add(Box.createVerticalStrut(20)); 
        this.add(instrumentList);
    }
}
