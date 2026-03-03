package staffEditor;

import javax.swing.*;
import java.awt.event.*;
import java.net.URL;

public class forwardButton extends JButton {

    StaffPage parent;

    public forwardButton(StaffPage p) {
        parent = p;

        this.setFocusable(false);
        this.setBorderPainted(false);
        this.setBorder(null);

        ClassLoader cldr = this.getClass().getClassLoader();
        URL imageURL = cldr.getResource("images/arrow.png");
        this.setIcon(new ImageIcon(imageURL));

        this.setToolTipText("復原刪除的音符");

        this.addActionListener(e -> redo());
    }

    // =====================================================
    // ⭐ Redo：恢復上一個被刪除的 NoteData
    // =====================================================
    public void redo() {

    	Toolbar tb = parent.getToolbar();
    	if (tb == null || tb.inputtype != inputType.Cursor)
    	    return;

        if (parent.trash_notes.size() == 0)
            return;

        // 1. 復原最後刪除的 NoteData
        NoteData restored = parent.trash_notes.lastElement();
        parent.trash_notes.remove(restored);
        parent.notes.add(restored);

        // 2. 找回原本的小節（使用 measureIndex + staffIndex）
        int mi = restored.getMeasureIndex();
        int si = restored.getStaffIndex();   // ⭐ 你 NoteData 必須有這個欄位

        Measure m = parent.getMeasures()[mi][si];
        m.getNotes().add(restored);

        // 3. 加回畫面
        parent.panel.add(restored.getLabel());

        // 4. 刷畫
        parent.panel.repaint();
    }

}
