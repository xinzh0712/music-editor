package staffEditor;

import javax.sound.midi.*;
import java.util.*;

public class MidiPlayer {

    private Sequencer sequencer;

    public MidiPlayer() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===============================================================
    // ⭐ 主播放方法（支援延音、和弦、多音多拍）
    // ===============================================================
    public void playNotes(List<NoteData> notes, int program) {
        try {
            Sequence seq = new Sequence(Sequence.PPQ, 480);
            Track track = seq.createTrack();

            // 設定樂器
            ShortMessage ci = new ShortMessage();
            ci.setMessage(ShortMessage.PROGRAM_CHANGE, 0, program, 0);
            track.add(new MidiEvent(ci, 0));

            // ⭐依 globalBeat 群組（同拍子的音要一起發生）
            Map<Double, List<NoteData>> map = new TreeMap<>();

            for (NoteData n : notes) {
                map.computeIfAbsent(n.getGlobalBeat(), k -> new ArrayList<>()).add(n);
            }

            // ⭐逐拍建立 NoteOn / NoteOff
            for (double gb : map.keySet()) {

                long tick = (long)(gb * 480);
                List<NoteData> group = map.get(gb);

                for (NoteData n : group) {

                    // ⭐ 休止符（全部 R 類型）不要播放
                    switch (n.getType()) {
                        case wholeR:
                        case halfR:
                        case quarterR:
                        case eighthR:
                        case sixteenthR:
                            continue; // 不播放
                    }

                    int midi = midiNumber(n.getPitch());
                    if (midi < 0) continue;

                    long noteOnTick = tick;
                    long noteOffTick = tick + durationToTick(n.getType());

                    track.add(createNoteOn(0, midi, 90, noteOnTick));
                    track.add(createNoteOff(0, midi, 90, noteOffTick));
                }

            }

            sequencer.setSequence(seq);
            sequencer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MidiEvent createNoteOn(int ch, int pitch, int vel, long tick) throws Exception {
        return createMidiEvent(ShortMessage.NOTE_ON, ch, pitch, vel, tick);
    }

    private MidiEvent createNoteOff(int ch, int pitch, int vel, long tick) throws Exception {
        return createMidiEvent(ShortMessage.NOTE_OFF, ch, pitch, vel, tick);
    }

    private MidiEvent createMidiEvent(int type, int ch, int pitch, int vel, long tick) throws Exception {
        ShortMessage msg = new ShortMessage();
        msg.setMessage(type, ch, pitch, vel);
        return new MidiEvent(msg, tick);
    }

    // ===============================================================
    // ⭐ 音名轉 MIDI（支援 #、b）
    // ===============================================================
    private int midiNumber(String pitch) {

        if (pitch == null) return -1;

        pitch = pitch.trim();

        // ⭐休止符跳過
        if (pitch.equals("R") || pitch.equals("REST"))
            return -1;

        // ※ 統一大小寫，避免 Db → DB 失效
        pitch = pitch.substring(0, pitch.length()-1).toUpperCase()
                        + pitch.substring(pitch.length()-1);

        String notePart = pitch.substring(0, pitch.length()-1);
        int octave;

        try {
            octave = Integer.parseInt(pitch.substring(pitch.length()-1));
        } catch (Exception e) {
            System.out.println("⚠ 無效八度格式: " + pitch);
            return -1;
        }

        // ⭐ 支援升降記號的 mapping（用大寫統一）
        Map<String, Integer> base = new HashMap<>();
        base.put("C", 0);   base.put("C#", 1);  base.put("DB", 1);
        base.put("D", 2);   base.put("D#", 3);  base.put("EB", 3);
        base.put("E", 4);
        base.put("F", 5);   base.put("F#", 6);  base.put("GB", 6);
        base.put("G", 7);   base.put("G#", 8);  base.put("AB", 8);
        base.put("A", 9);   base.put("A#", 10); base.put("BB", 10);
        base.put("B", 11);  base.put("CB", 11);

        if (!base.containsKey(notePart)) {
            System.out.println("⚠ 無效音名: " + notePart);
            return -1;
        }

        return (octave + 1) * 12 + base.get(notePart);
    }

    public int durationToTick(longType type) {
        switch (type) {
            case whole:      return 1920;
            case half:       return 960;
            case quarter:    return 480;
            case eighth:     return 240;
            case sixteenth:  return 120;

            case wholeR:     return 1920;
            case halfR:      return 960;
            case quarterR:   return 480;
            case eighthR:    return 240;
            case sixteenthR: return 120;
        }
        return 480;
    }
}
