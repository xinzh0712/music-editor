package staffEditor;

import javax.swing.*;
import java.awt.event.*;

public class backButton extends JButton {

    StaffPage parent;
    ClassLoader cldr;
    ImageIcon icon;

    public backButton(StaffPage p) {
        this.setFocusable(false);
        this.parent = p;

        this.setBorderPainted(false);
        this.setBorder(null);

        cldr = getClass().getClassLoader();
        icon = new ImageIcon(cldr.getResource("images/back.png"));
        this.setIcon(icon);

        this.setToolTipText("返回上一步");

        this.addActionListener(e -> doUndo());

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!parent.notes.isEmpty())
                    backButton.this.setVisible(true);
            }
        });
    }

    // =========================================================
    // ⭐ 回到上一步（完整 Undo）
    // =========================================================
    public void doUndo() {

        Toolbar tb = parent.getToolbar();
        if (tb == null || tb.inputtype != inputType.Cursor)
            return;

        if (parent.notes.size() == 0)
            return;

        // =========================================================
        // 1. 取出最後音符
        // =========================================================
        NoteData last = parent.notes.lastElement();
        parent.notes.remove(last);
        parent.trash_notes.add(last);

        // =========================================================
        // 2. 從小節的 list 移除
        // =========================================================
        int mi = last.getMeasureIndex();
        int si = last.getStaffIndex();
        Measure m = parent.getMeasures()[mi][si];

        m.getNotes().remove(last);

        // =========================================================
        // ⭐⭐ 3. 重算該小節的拍子（VERY IMPORTANT）
        // =========================================================
        m.recalcBeats();


        // =========================================================
        // ⭐⭐ 4. 全曲重新計算拍子（避免 globalBeat 出錯）
        // =========================================================
        for (Measure[] row : parent.getMeasures()) {
            for (Measure mm : row) {
                if (mm != null) mm.recalcBeats();
            }
        }

        // =========================================================
        // 5. 從畫面移除 label
        // =========================================================
        parent.panel.remove(last.getLabel());
        parent.panel.repaint();
    }
}
