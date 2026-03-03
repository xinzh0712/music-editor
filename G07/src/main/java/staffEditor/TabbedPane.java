package staffEditor;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class TabbedPane extends JTabbedPane {

    private static final long serialVersionUID = 1L;

    private MainWindow parent;
    
    public MainWindow getMainWindow() {
        return parent;
    }


    /** 儲存所有 StaffPage（避免每次去掃描 tab） */
    private List<StaffPage> staffPages = new ArrayList<>();


    // ---------------------------------------------------------
    // ★ 建構子：自動建立第一頁
    // ---------------------------------------------------------
    public TabbedPane(MainWindow parent) {
        this.parent = parent;

        this.setBackground(Color.white);
        this.setPreferredSize(new Dimension(40, 0));

        this.parent.setVisible(true);

        // 建立第一頁
        addTab("page", new StaffPage(this));
    }


    // ---------------------------------------------------------
    // ★ 取得目前選取的 StaffPage
    // ---------------------------------------------------------
    public StaffPage getCurrentPage() {
        Component comp = this.getSelectedComponent();
        return (comp instanceof StaffPage) ? (StaffPage) comp : null;
    }


    // ---------------------------------------------------------
    // ★ 取得所有頁面的 StaffPage
    // ---------------------------------------------------------
    public List<StaffPage> getAllStaffPages() {
        return staffPages;
    }


    // ---------------------------------------------------------
    // ★ 新增一頁（你缺少的最重要方法）
    // ---------------------------------------------------------
    public StaffPage createNewPage() {
        StaffPage newPage = new StaffPage(this);
        addTab("page", newPage);
        setSelectedComponent(newPage);  // 自動切換過去
        return newPage;
    }


    // ---------------------------------------------------------
    // ★ 自動更新所有頁碼（顯示於畫面 PageNumber）
    // ---------------------------------------------------------
    public void refreshStaffPageNumbers() {
        for (int i = 0; i < getTabCount(); i++) {
            Component comp = getComponentAt(i);
            if (comp instanceof StaffPage) {
                ((StaffPage) comp).setPageNumber(i + 1);
            }
        }
    }


    // ---------------------------------------------------------
    // ★ 改寫 addTab：加入關閉按鈕、維護 staffPages
    // ---------------------------------------------------------
    @Override
    public void addTab(String title, final Component content) {

        int pageID = getTabCount() + 1;

        JPanel tab = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title + pageID);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));

        CloseTabBtn closeTabBtn = new CloseTabBtn(this, content);

        tab.add(label, BorderLayout.WEST);
        tab.add(closeTabBtn, BorderLayout.EAST);
        tab.setOpaque(false);
        tab.setBorder(BorderFactory.createEmptyBorder(2, 1, 1, 1));

        // 加入頁面
        super.addTab(title, content);
        this.setTabComponentAt(getTabCount() - 1, tab);

        // 如果加入的是 StaffPage，加入清單
        if (content instanceof StaffPage) {
            staffPages.add((StaffPage) content);
            refreshStaffPageNumbers();
        }
    }


    // ---------------------------------------------------------
    // ★ 移除 tab 時，同步移除 staffPages 內記錄
    // ---------------------------------------------------------
    @Override
    public void removeTabAt(int index) {
        Component content = getComponentAt(index);

        if (content instanceof StaffPage) {
            staffPages.remove(content);
        }

        super.removeTabAt(index);
        refreshStaffPageNumbers();
    }


    // ---------------------------------------------------------
    // ★ 備用：取得目前 tab 的 StaffPage
    // ---------------------------------------------------------
    public StaffPage getSelectedStaffPage() {
        return getCurrentPage();
    }
    public void clearAllPages() {
        // 移除所有 Tab
        this.removeAll();

        // 清空 StaffPage 列表
        staffPages.clear();

        // 建立一個新的空白頁面
        StaffPage newPage = new StaffPage(this);
        addTab("page", newPage);

        // 重設頁碼
        refreshStaffPageNumbers();
    }


}


// ====================================================================
// ★ 關閉按鈕
// ====================================================================
class CloseTabBtn extends JButton {

    private TabbedPane parent;

    public CloseTabBtn(TabbedPane p, final Component content) {
        this.parent = p;

        this.setText("x");
        this.setPreferredSize(new Dimension(17, 17));
        this.setToolTipText("關閉");
        this.setUI(new BasicButtonUI());

        this.setContentAreaFilled(false);
        this.setFocusable(false);
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBorderPainted(false);

        this.setBackground(new Color(255, 0, 0));
        this.setForeground(Color.white);
        this.setOpaque(true);

        this.setRolloverEnabled(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "確定關閉頁面？",
                        "警告",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    parent.removeTabAt(parent.indexOfComponent(content));
                }
            }
        });
    }
}
