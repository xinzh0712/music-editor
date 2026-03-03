package staffEditor;

import javax.swing.JLabel;

public class NoteData {
    private String pitch;       // C4, D#5, R = 休止符
    private String duration;    // quarter, eighth, ...
    private int x;              // 畫面 X (判斷同拍)
    
    private int pitchY;         // 音高基準位置
    private int displayY;       // 目前視覺位置（堆疊會改）

    private longType type;      // 原始音符類型
    private JLabel label;       // JLabel for drawing

    private int measureIndex;   // 小節序號（0~39）
    public void setMeasureIndex(int idx) { this.measureIndex = idx; }
    public int getMeasureIndex() { return measureIndex; }

    private int beatIndex;      // 小節內第幾個拍子(和弦共用)
    public int getBeatIndex() { return beatIndex; }
    public void setBeatIndex(int b) { beatIndex = b; }

    private double globalBeat;  // ★ 撥放順序排序專用
    public double getGlobalBeat() { return globalBeat; }
    public void setGlobalBeat(double g) { globalBeat = g; }

    //＝＝＝＝ Getter/Setter ＝＝＝＝
    private int staffIndex;     // 0 = 高音譜, 1 = 低音譜
    public int getStaffIndex() { return staffIndex; }
    public void setStaffIndex(int s) { staffIndex = s; }
    
    public void setLabel(JLabel l) { this.label = l; }
    public JLabel getLabel() { return this.label; }

    public int getPitchY() { return pitchY; }
    public int getDisplayY() { return displayY; }
    public void setDisplayY(int y) { this.displayY = y; }

    public int getX() { return x; }
    public int getY() { return displayY; }

    public String getPitch() { return pitch; }
    public String getDuration() { return duration; }
    public longType getType() { return type; }

    public void setType(longType type) {
        this.type = type;
    }
    public void setPitch(String p) {
        this.pitch = p;
    }


    //＝＝＝＝ Constructor ① pitch + duration ＝＝＝＝
    public NoteData(String pitch, String duration, int x, int y) {
        this.pitch = pitch;
        this.duration = duration;
        this.x = x;

        this.pitchY = y;
        this.displayY = y;

        this.type = convertDurationToType(duration);
    }

    //＝＝＝＝ Constructor ② longType ＝＝＝＝
    public NoteData(int x, int y, longType type) {
        this.x = x;
        this.pitchY = y;
        this.displayY = y;

        this.type = type;
        convertTypeToPitchDuration(type);
    }

    //＝＝＝＝ Constructor ③ pitch + longType ＝＝＝＝
    public NoteData(String pitch, longType type, int x, int y) {
        this.pitch = pitch;
        this.x = x;

        this.pitchY = y;
        this.displayY = y;

        this.type = type;
        convertTypeToPitchDuration(type);
    }

    //＝＝＝＝ duration → enum ＝＝＝＝
    private longType convertDurationToType(String duration) {
        switch (duration) {
            case "whole":     return longType.whole;
            case "half":      return longType.half;
            case "quarter":   return longType.quarter;
            case "eighth":    return longType.eighth;
            case "sixteenth": return longType.sixteenth;

            case "wholeR":    return longType.wholeR;
            case "halfR":     return longType.halfR;
            case "quarterR":  return longType.quarterR;
            case "eighthR":   return longType.eighthR;
            case "sixteenthR":return longType.sixteenthR;
        }
        System.out.println("⚠ 未知 duration：" + duration);
        return longType.quarter;
    }

    //＝＝＝＝ enum → pitch + duration ＝＝＝＝
    private void convertTypeToPitchDuration(longType type) {
        switch (type) {
            case whole:      this.duration = "whole"; break;
            case half:       this.duration = "half"; break;
            case quarter:    this.duration = "quarter"; break;
            case eighth:     this.duration = "eighth"; break;
            case sixteenth:  this.duration = "sixteenth"; break;

            case wholeR:     this.pitch = "R"; this.duration = "wholeR"; break;
            case halfR:      this.pitch = "R"; this.duration = "halfR"; break;
            case quarterR:   this.pitch = "R"; this.duration = "quarterR"; break;
            case eighthR:    this.pitch = "R"; this.duration = "eighthR"; break;
            case sixteenthR: this.pitch = "R"; this.duration = "sixteenthR"; break;
        }
    }

    @Override
    public String toString() {
        return pitch + duration 
            + " (x=" + x 
            + ", pitchY=" + pitchY 
            + ", displayY=" + displayY 
            + ", measure=" + measureIndex
            + ", beat=" + beatIndex
            + ", globalBeat=" + globalBeat 
            + ")";
    }
}
