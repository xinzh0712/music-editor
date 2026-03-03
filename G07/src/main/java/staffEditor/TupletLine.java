package staffEditor;

import javax.swing.JLabel;

public class TupletLine {
    private JLabel note1, note2; // 關聯的兩個音符標籤
    private int whatType = 0;
    public TupletLine(JLabel note1, JLabel note2,int WT) {
        this.note1 = note1;
        this.note2 = note2;
        whatType = WT;
    }

    public JLabel getNote1() {
        return note1;
    }

    public JLabel getNote2() {
        return note2;
    }

    public int getX1() {
        return note1.getX() + note1.getWidth() / 2; // 計算音符中心點的 X 座標
    }

    public int getY1() {
        return note1.getY() - 10; // 符槓上移的 Y 座標
    }

    public int getX2() {
        return note2.getX() + note2.getWidth() / 2;
    }

    public int getY2() {
        return note2.getY() - 10;
    }
    
    public int getType()
    {
    	return whatType;
    }
}
