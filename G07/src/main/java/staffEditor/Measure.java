package staffEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Measure {

    // 小節座標
    public int startX, startY, endX, endY;

    // 小節編號（全曲）
    public int measureIndex;

    // staff (0 = 高音譜, 1 = 低音譜)
    public int staffIndex;

    // 小節內音符
    private List<NoteData> notes;

    // 判斷和弦的最小 X 差距
    private static final int CHORD_THRESHOLD = 12;

    // 本小節目前累計拍子
    private double nextBeatPosition = 0.0;


    // constructor
    public Measure(int measureIndex, int staffIndex,
                   int startX, int startY, int endX, int endY) {

        this.measureIndex = measureIndex;
        this.staffIndex = staffIndex;

        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        this.notes = new ArrayList<>();
    }

    public int getMeasureIndex() { return measureIndex; }
    public int getStaffIndex() { return staffIndex; }
    public List<NoteData> getNotes() { return notes; }

    // =============================
    // 長度轉拍子
    // =============================
    private double longTypeToBeats(longType type) {
        switch (type) {
            case whole: return 4.0;
            case half: return 2.0;
            case quarter: return 1.0;
            case eighth: return 0.5;
            case sixteenth: return 0.25;

            case wholeR: return 4.0;
            case halfR: return 2.0;
            case quarterR: return 1.0;
            case eighthR: return 0.5;
            case sixteenthR: return 0.25;
        }
        return 0;
    }

    // =============================
    // ⭐ 加入音符（唯一入口）
    // =============================
    public boolean addNoteData(NoteData note) {

        // 先加進 list
        notes.add(note);

        // 按 X 排序 & 重算拍子
        recalcBeats();

        // 超過一小節 4 拍就移除
        if (nextBeatPosition > 4.0 + 1e-9) {

            notes.remove(note);
            recalcBeats(); // 回復原狀

            System.out.println("⚠ 小節已滿，拒絕加入音符");
            return false;
        }

        return true;
    }


    // =============================
    // ⭐ 核心演算法：重新計算拍子
    // =============================
    public void recalcBeats() {

        nextBeatPosition = 0;

        // 按 X 排序
        notes.sort(Comparator.comparingInt(NoteData::getX));

        // 用來記錄同一拍的和弦群組
        List<List<NoteData>> groups = new ArrayList<>();
        List<NoteData> currentGroup = new ArrayList<>();

        NoteData prev = null;

        for (NoteData n : notes) {

            if (prev == null) {
                currentGroup.add(n);
            } else {
                boolean isChord =
                    n.getStaffIndex() == prev.getStaffIndex() &&
                    Math.abs(n.getX() - prev.getX()) < CHORD_THRESHOLD;

                if (isChord) {
                    currentGroup.add(n);
                } else {
                    groups.add(currentGroup);
                    currentGroup = new ArrayList<>();
                    currentGroup.add(n);
                }
            }
            prev = n;
        }

        // 最後一組加入
        if (!currentGroup.isEmpty()) {
            groups.add(currentGroup);
        }

        // 分組完成後，再分配拍子
        nextBeatPosition = 0;

        for (List<NoteData> group : groups) {

            int beatIndex = (int)(nextBeatPosition * 100);
            double beatValue = longTypeToBeats(group.get(0).getType());

            for (NoteData n : group) {
                n.setBeatIndex(beatIndex);
                n.setGlobalBeat(measureIndex * 4 + nextBeatPosition);
            }

            nextBeatPosition += beatValue;
        }
    }



    // 判斷某座標是否在此小節內
    public boolean contains(int x, int y) {
        return x >= startX && x <= endX &&
               y >= startY && y <= endY;
    }

    // 畫小節框
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(startX, startY, endX - startX, endY - startY);
    }

}
