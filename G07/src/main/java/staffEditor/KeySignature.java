package staffEditor;

import java.util.*;

public enum KeySignature {

    C_MAJOR(new String[]{}),

    G_MAJOR(new String[]{"F#"}),
    D_MAJOR(new String[]{"F#", "C#"}),
    A_MAJOR(new String[]{"F#", "C#", "G#"}),
    E_MAJOR(new String[]{"F#", "C#", "G#", "D#"}),
    B_MAJOR(new String[]{"F#", "C#", "G#", "D#", "A#"}),
    F_SHARP_MAJOR(new String[]{"F#", "C#", "G#", "D#", "A#", "E#"}),
    C_SHARP_MAJOR(new String[]{"F#", "C#", "G#", "D#", "A#", "E#", "B#"}),

    F_MAJOR(new String[]{"Bb"}),
    Bb_MAJOR(new String[]{"Bb", "Eb"}),
    Eb_MAJOR(new String[]{"Bb", "Eb", "Ab"}),
    Ab_MAJOR(new String[]{"Bb", "Eb", "Ab", "Db"}),
    Db_MAJOR(new String[]{"Bb", "Eb", "Ab", "Db", "Gb"}),
    Gb_MAJOR(new String[]{"Bb", "Eb", "Ab", "Db", "Gb", "Cb"});


    private Set<String> accidentals;

    KeySignature(String[] list) {
        accidentals = new HashSet<>(Arrays.asList(list));
    }

    public boolean has(String accidental) {
        return accidentals.contains(accidental);
    }
    public Set<String> getAccidentals() {
        return accidentals;
    }

    // 按固定順序取得第 i 個升降記號
    public String getAccidental(int i) {

        String[] sharpOrder = {"F#", "C#", "G#", "D#", "A#", "E#", "B#"};
        String[] flatOrder  = {"Bb", "Eb", "Ab", "Db", "Gb", "Cb", "Fb"};

        for (String s : sharpOrder) {
            if (accidentals.contains(s)) {
                if (i == 0) return s;
                i--;
            }
        }

        for (String s : flatOrder) {
            if (accidentals.contains(s)) {
                if (i == 0) return s;
                i--;
            }
        }

        return null;
    }

}
