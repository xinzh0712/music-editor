package staffEditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.swing.*;



public class StaffPage extends JScrollPane {
	
    TabbedPane parent; 
    public Toolbar getToolbar() {
        if (parent == null) return null;

        MainWindow win = parent.getMainWindow();
        if (win == null) return null;

        return win.toolbar;
    }

    private final int STAFF_X_START = 100;
    private final int STAFF_Y_START = 120;
    private final int STAFF_X_END = 1050;

    static int count = 0;
    int id = 1;

    JLabel note;
    
    public Vector<NoteData> notes = new Vector<>();
    public Vector<NoteData> trash_notes = new Vector<>();
    public List<NoteData> notesDataList = new ArrayList<>();


    public StaffDrawer staffDrawer; 
    private Measure[][] measures; 

    JButton backButton, forwardButton; 
    JComponent panel;

    StaffPage page;
    String title="曲名" ; 
    String composer="作曲家" ;

    backButton back;
    forwardButton forward;

    public ClassLoader cldr;
    public URL imageURL;
    public ImageIcon icon ,imageIcon;

    StaffLabel staffTitle,authorTitle,instrumentTitle,pageCount,measure[];

    Map<String, String> labelsData = new HashMap<>();
    List<StaffPage> allPages;

    String m[]={"1","5","9","13","17","21","25","29","33","37"};

    MouseButton Mouse;   
    
    public Measure[][] getMeasures() {
        return this.measures;
    }
    public void sortNotesForPlayback() {
        notes.sort((a, b) -> {
            if (a.getGlobalBeat() != b.getGlobalBeat())
                return Double.compare(a.getGlobalBeat(), b.getGlobalBeat());

            return Integer.compare(a.getPitchY(), b.getPitchY());
        });
    }

    public StaffPage(TabbedPane p) {
        parent = p;
        count++;
        id = count;

        back = new backButton(this);
        forward = new forwardButton(this);
        staffDrawer = new StaffDrawer();

        notes = new Vector<>();
        trash_notes = new Vector<>();

        allPages = parent.getAllStaffPages();

        initPanel();
        setupMeasures();
        initButtons();
        initMouseListeners();

        this.getVerticalScrollBar().setUnitIncrement(10);
        // ⭐ 啟用水平捲動條
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // 滑動速度
        this.getHorizontalScrollBar().setUnitIncrement(16);
        this.getVerticalScrollBar().setUnitIncrement(10);
    }

    public Vector<NoteData> getNotes() {
        return notes;
    }

    public List<NoteData> getNotesForPlayback() {

        List<NoteData> allNotes = new ArrayList<>();

        if (measures != null) {

            for (Measure[] measureGroup : measures) {
                for (Measure m : measureGroup) {

                    if (m == null) continue;

                    for (NoteData n : m.getNotes()) {

                        if (trash_notes.contains(n.getLabel())) continue;
                        if (n.getLabel() == null || !notes.contains(n)) continue;

                        allNotes.add(n);
                    }
                }
            }
        }

        allNotes.sort(Comparator.comparingDouble(NoteData::getGlobalBeat));

        return allNotes;
    }


    public void setPageNumber(int n) {
        pageCount.setText("-" + n + "-");
    }

    public void initPanel() {
        panel = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                staffDrawer.drawStaff(g, 10, new int[]{400, 630, 860, 1090}, 100, 155, 1090);

                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3));

                // ✔ 正確拍號位置
                g2.setFont(new Font("Serif", Font.BOLD, 32));
                int sigX = 160;     // 拍號的 X（稍微往左）
                int topY = 175;     // 高音譜拍號 top
                int bottomY = topY + 28; // 下方那個 4

                g2.drawString("4", sigX, topY); 
                g2.drawString("4", sigX, bottomY);


                // ⭐⭐ 1. 找出全部 beam 群組
                List<List<NoteData>> allBeamGroups = new ArrayList<>();

                for (int i = 0; i < measures.length; i++) {
                    for (int s = 0; s < 2; s++) {
                        Measure m = measures[i][s];
                    }
                }
            }

        };

        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(0, 1400));
        this.setViewportView(panel);
            
        staffTitle = new StaffLabel(labelsData.getOrDefault("staffTitle", "Title"),SwingConstants.CENTER, this, "staffTitle");    
        staffTitle.setLocation(340,33);
        staffTitle.setFont(new Font("標楷體",0,30));
        staffTitle.setSize(new Dimension(500,75));
        labelsData.put("staffTitle", staffTitle.getText());
        panel.add(staffTitle);

        authorTitle = new StaffLabel(labelsData.getOrDefault("authorTitle","author"),SwingConstants.RIGHT,this,"authorTitle");
        authorTitle.setLocation(750,120);
        authorTitle.setFont(new Font("標楷體",0,17));
        authorTitle.setSize(new Dimension(300,30));
        panel.add(authorTitle);

        instrumentTitle = new StaffLabel(labelsData.getOrDefault("instrumentTitle","Instrument"),SwingConstants.LEFT,this,"instrumentTitle");
        instrumentTitle.setLocation(100,100);
        instrumentTitle.setFont(new Font("標楷體",0,20));
        instrumentTitle.setSize(new Dimension(150,30));
        panel.add(instrumentTitle);

        pageCount = new StaffLabel("", SwingConstants.CENTER, this, "pageCount");
        pageCount.setLocation(570,1350);
        pageCount.setFont(new Font("標楷體",0,17));
        pageCount.setSize(new Dimension(60,30));
        panel.add(pageCount);

        measure = new StaffLabel[10];
        int g = 0;
        for (int i=0; i<10; i++) {
            measure[i] = new StaffLabel(m[i],SwingConstants.CENTER,this,"measure");
            measure[i].setLocation(95,135+ i*10 + g);
            measure[i].setFont(new Font("標楷體",0,12));
            measure[i].setSize(new Dimension(25,20));
            panel.add(measure[i]);
            g += 115;
        }

        this.panel.setBackground(Color.white);
        this.panel.setPreferredSize(new Dimension(2500,1400));
        this.parent.setVisible(true);
        this.setViewportView(panel);

        back.setLocation(20,20);
        back.setVisible(false);
        back.setSize(new Dimension(45,45));
        panel.add(back);

        forward.setLocation(70,20);
        forward.setVisible(false);
        forward.setSize(new Dimension(45,45));
        panel.add(forward);

        panel.revalidate();
        panel.repaint();
    }
 
    private void setupMeasures() {

        int measureWidth = 230;

        int trebleStartY = 140;  // 第一行 Treble 的起點
        int bassOffset = 125;    // Bass 距離 Treble 的距離
        int lineSpacing = 250;   // ⭐ 一整組五線譜的高度（Treble + Bass）
        int measuresPerLine = 4; // 每組四個小節

        measures = new Measure[40][2];

        for (int i = 0; i < 40; i++) {

            int lineIndex = i / measuresPerLine;   // 第幾組（0 = 第一組，1 = 第二組）
            int measureIdxInLine = i % measuresPerLine;

            int startX = 170 + measureIdxInLine * measureWidth;
            int trebleY = trebleStartY + lineIndex * lineSpacing;
            int bassY = trebleY + bassOffset;

            // ⭐ 第一組、第二組、第三組 Treble / Bass 完整分開
            measures[i][0] = new Measure(
                    i, 0,
                    startX,
                    trebleY,
                    startX + measureWidth,
                    trebleY + 80
            );

            measures[i][1] = new Measure(
                    i, 1,
                    startX,
                    bassY,
                    startX + measureWidth,
                    bassY + 80
            );
        }
    }
    public Measure getMeasureForPoint(int x, int y) {
        if (measures == null) return null;

        for (Measure[] group : measures) {
            for (Measure m : group) {
                if (m == null) continue;

                // ⭐ 正確比較 X/Y 是否落在小節框內
                if (x >= m.startX && x <= m.endX &&
                    y >= m.startY && y <= m.endY) {

                    return m;
                }
            }
        }

        return null;
    }

    private String getPitchFromYCoordinate(int y) {

        int step = 5;
        int half = 2;

        // 量測的基準（第一行）
        int trebleTop = 150;
        int trebleBottom = 195;

        int bassTop = 280;
        int bassBottom = bassTop + 80;

        int lineSpacing = 250;   // ⭐ 一組 treble+bass 的距離

        // natural notes mapping
        String[] treble = {
            "G5","F5","E5","D5","C5",
            "B4","A4","G4","F4","E4"
        };

        String[] bass = {
            "A3","G3","F3","E3","D3",
            "C3","B2","A2","G2","F2",
            "E2","D2","C2","B1","A1",
            "G1","F1"
        };

        // ======================================================
        // ⭐ 找出屬於第幾組五線譜（lineIndex）
        // ======================================================
        for (int line = 0; line < 10; line++) {

            int tTop = trebleTop + line * lineSpacing;
            int tBottom = trebleBottom + line * lineSpacing;

            int bTop = bassTop + line * lineSpacing;
            int bBottom = bassBottom + line * lineSpacing;

            // ======== 判斷 Treble 區 ========
            if (y >= tTop - half && y <= tBottom + half) {

                int index = Math.round((y - tTop) / (float)step);
                index = Math.max(0, Math.min(index, treble.length - 1));
                return treble[index];
            }

            // ======== 判斷 Bass 區 ========
            if (y >= bTop - half && y <= bBottom + half) {

                int index = Math.round((y - bTop) / (float)step);
                index = Math.max(0, Math.min(index, bass.length - 1));
                return bass[index];
            }
        }

        // 落在行間 → 空白，不是音符
        return null;
    }
    public void initButtons() {
        back.setBounds(20, 20, 45, 45);
        forward.setBounds(70, 20, 45, 45);

        back.setVisible(false);
        forward.setVisible(false);

        panel.add(back);
        panel.add(forward);
    }

    public JLabel findNoteLabel(NoteData note) {
        return note.getLabel();   // 直接回傳 NoteData 綁定的 JLabel
    }
    

    public void initMouseListeners() {

        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                int x = e.getX();
                int y = e.getY();

                System.out.println("滑鼠點擊座標: X=" + x + ", Y=" + y);

                MainWindow win = parent.getMainWindow();

                // 光標模式 OR 點在五線譜外面
                if (win.toolbar.inputtype == inputType.Cursor ||
                    x < STAFF_X_START || x > STAFF_X_END || y < STAFF_Y_START) {
                    return;
                }

                longType lt = win.toolbar.longtype;
                cldr = getClass().getClassLoader();

                // ① 先判斷 measure（不能反）
                Measure measure = getMeasureForPoint(x, y);

                if (measure == null) {
                    System.out.println("⚠ 無法判斷此音符的小節！（超出五線譜範圍） x=" + x + " y=" + y);
                    return;  // ⛔ 必須 return
                }

                // ② 然後根據 Y 算音高
                String pitch = getPitchFromYCoordinate(y);
                System.out.println("🟦 此音符音高 = " + pitch);

                // ③ 休止符處理
                String duration = "";
                URL imageURL = null;

                switch (lt) {

                    case whole:
                        duration = "whole";
                        imageURL = cldr.getResource("images/whole.png");
                        break;

                    case half:
                        duration = "half";
                        imageURL = cldr.getResource("images/half_note.png");
                        break;

                    case quarter:
                        duration = "quarter";
                        imageURL = cldr.getResource("images/quarter_note.png");
                        break;

                    case eighth:
                        duration = "eighth";
                        imageURL = cldr.getResource("images/eighth_note.png");
                        break;

                    case sixteenth:
                        duration = "sixteenth";
                        imageURL = cldr.getResource("images/sixteenth-note.png");
                        break;

                    case wholeR:
                        pitch = "R";
                        duration = "wholeR";
                        imageURL = cldr.getResource("images/whole_rest.png");
                        break;

                    case halfR:
                        pitch = "R";
                        duration = "halfR";
                        imageURL = cldr.getResource("images/half-rest.png");
                        break;

                    case quarterR:
                        pitch = "R";
                        duration = "quarterR";
                        imageURL = cldr.getResource("images/quarter-note-rest.png");
                        break;

                    case eighthR:
                        pitch = "R";
                        duration = "eighthR";
                        imageURL = cldr.getResource("images/eight-note-rest.png");
                        break;

                    case sixteenthR:
                        pitch = "R";
                        duration = "sixteenthR";
                        imageURL = cldr.getResource("images/sixteenth_rest.png");
                        break;

                    default:
                        return;
                }

                if (imageURL == null) {
                    System.out.println("❌ 找不到圖片：" + duration);
                    return;
                }

                // ④ 圖片 + 單位偏移
                ImageIcon icon = new ImageIcon(imageURL);
                int w = 30, h = (lt == longType.whole ? 20 : 40);

                if (lt == longType.halfR || lt == longType.wholeR ||
                    lt == longType.eighthR || lt == longType.quarterR) {
                    w = 18; h = 20;
                }

                Image scaledImg = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
                ImageIcon img = new ImageIcon(scaledImg);

                Point offset = getNoteOffset(lt);
                int finalX = x + offset.x;
                int finalY = y + offset.y;

                // ⑤ 建立 NoteData
                NoteData added = new NoteData(pitch, duration, finalX, finalY);
                added.setDisplayY(finalY);

                // 建 label
                JLabel noteLabel = new JLabel(img);
                noteLabel.setLocation(finalX, finalY);
                noteLabel.setSize(30, 40);
                noteLabel.setVisible(true);
                added.setLabel(noteLabel);

                // ⑥ 放入小節（這裡會自動算拍子）
                // 依 pitch 決定譜號（最準確）
             // 根據滑鼠點擊時的 y 來判斷譜號
                if (y < 250) {      // 250 是 Treble 與 Bass 分界中線
                    added.setStaffIndex(0);   // 高音譜
                } else {
                    added.setStaffIndex(1);   // 低音譜
                }


                if (!measure.addNoteData(added)) {
                    System.out.println("⚠ 小節已滿，拒絕加入音符（不畫）");
                    return; // ⛔ 不能繼續加進 notes
                }

                // ⑦ 放入全曲 notes
                notes.add(added);
                notesDataList.add(added);

                panel.add(noteLabel);
                panel.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                back.setVisible(!notes.isEmpty());
                forward.setVisible(!trash_notes.isEmpty());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                back.setVisible(false);
                forward.setVisible(false);
            }
        });

        this.addMouseWheelListener(e -> {
            int offset = this.getVerticalScrollBar().getValue();
            back.setLocation(20, 20 + offset);
            forward.setLocation(70, 20 + offset);
        });
    }

    private Point getNoteOffset(longType noteType) {

        switch (noteType) {
            case whole:
                return new Point(-12, -20);
            case half:
            case quarter:
            case eighth:
            case sixteenth:
                return new Point(-16, -33);

            case wholeR:
            case halfR:
            case quarterR:
            case eighthR:
            case sixteenthR:
                return new Point(-12, -25);

            default:
                return new Point(0, 0);
        }
    }

}
