package staffEditor;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class PlayButton extends JButton {
    MainWindow parent;

    public PlayButton(MainWindow parent) {
        this.parent = parent;

        this.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/play-button-arrowhead.png")));
        this.setToolTipText("播放音樂");

        this.addActionListener(e -> {

            System.out.println("Play button clicked");

            TabbedPane tabbedPane = parent.getTabbedPane();
            StaffPage currentPage = tabbedPane.getCurrentPage();

            if (currentPage == null) {
                System.out.println("⚠ 無法取得 StaffPage");
                return;
            }

            playMusic(currentPage);
        });
    }

    // ============================================================
    // 播放音樂（新版：支援高音 + 低音譜「同步播放」）
    // ============================================================
    private void playMusic(StaffPage staffPage) {

        List<NoteData> notes = staffPage.getNotesForPlayback();

        // ⭐ 在這裡加偵錯輸出（這裡才找得到 notes）
        System.out.println("🎵 播放前 notes.size = " + notes.size());
        for (NoteData n : notes) {
            System.out.println("  → pitch=" + n.getPitch() 
                             + "  globalBeat=" + n.getGlobalBeat() 
                             + "  measure=" + n.getMeasureIndex()
                             + "  beatIndex=" + n.getBeatIndex());
        }
        System.out.println("🎵 播放前 totalNotesInMeasures = " + staffPage.getNotesForPlayback().size());

        
        MidiPlayer player = new MidiPlayer();
        int program = parent.getInstrumentList().getSelectedProgramNumber();

        new Thread(() -> player.playNotes(notes, program)).start();
    }
    

    // ======================================================
    // ⭐ 正確分組：同 measure + x 接近 → 同一個 beat
    // ======================================================
 // ======================================================
 // 正確分組：同 measure + 同 staff + 同 beatIndex 才能成和弦
 // ======================================================
 private List<List<NoteData>> groupChords(List<NoteData> notes) {
     List<List<NoteData>> result = new ArrayList<>();

     notes.sort((a, b) -> {
         // 1️⃣ 先依 globalBeat 排序（優先）
         int cmp = Double.compare(a.getGlobalBeat(), b.getGlobalBeat());
         if (cmp != 0) return cmp;

         // 2️⃣ 相同 globalBeat 時 → 高音譜優先
         cmp = Integer.compare(a.getStaffIndex(), b.getStaffIndex());
         if (cmp != 0) return cmp;

         // 3️⃣ 音高排序（不重要）
         return Integer.compare(a.getPitchY(), b.getPitchY());
     });

     int i = 0;
     while (i < notes.size()) {

         NoteData first = notes.get(i);

         double gb = first.getGlobalBeat();
         int staff = first.getStaffIndex();
         int measure = first.getMeasureIndex();
         int beat = first.getBeatIndex();

         List<NoteData> chord = new ArrayList<>();
         chord.add(first);
         i++;

         while (i < notes.size()) {
             NoteData n = notes.get(i);

             if (n.getGlobalBeat() != gb) break;
             if (n.getStaffIndex() != staff) break;
             if (n.getMeasureIndex() != measure) break;
             if (n.getBeatIndex() != beat) break;

             chord.add(n);
             i++;
         }

         result.add(chord);
     }

     return result;
 }

}


