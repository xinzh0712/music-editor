package staffEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StaffLabel extends JLabel {

    private final StaffPage parent;
    private final String labelName; // 用來識別標籤名稱用於存入 labelsData

    public StaffLabel(String text, int alignment, StaffPage p, String labelName) {
        super(text, alignment);

        this.parent = p;
        this.labelName = labelName;

        setVisible(true);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (isCursorMode()) {
                    setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isCursorMode()) {
                    setBorder(null);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (isCursorMode()) {
                    String newText = JOptionPane.showInputDialog("請輸入新的文字：", getText());

                    // ★ null = 按取消，不應覆寫
                    if (newText != null) {
                        setText(newText);

                        // ★ 同步到 StaffPage.labelsData
                        parent.labelsData.put(labelName, newText);

                        parent.panel.revalidate();
                        parent.panel.repaint();
                    }
                }
            }
        });
    }

    /**
     * ★ 判斷工具列是否為 Cursor 模式（才能編輯標籤）
     */
    private boolean isCursorMode() {
        try {
            return parent.getToolbar().inputtype == inputType.Cursor;
        } catch (Exception e) {
            return false; // 若 toolbar 尚未建立，不要報錯
        }
    }
}
