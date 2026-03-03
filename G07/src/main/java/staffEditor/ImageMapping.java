package staffEditor;

import java.net.URL;

public class ImageMapping {

    public static URL getImageURL(longType type) {
        ClassLoader cl = ImageMapping.class.getClassLoader();

        switch (type) {
            case quarter:    return cl.getResource("images/quarter_note.png");
            case eighth:     return cl.getResource("images/eighth_note.png");
            case sixteenth:  return cl.getResource("images/sixteenth-note.png");
            case half:       return cl.getResource("images/half_note.png");
            case whole:      return cl.getResource("images/whole.png");

            case quarterR:   return cl.getResource("images/quarter-note-rest.png");
            case eighthR:    return cl.getResource("images/eight-note-rest.png");
            case sixteenthR: return cl.getResource("images/sixteenth_rest.png");
            case halfR:      return cl.getResource("images/half-rest.png");
            case wholeR:     return cl.getResource("images/whole_rest.png");

            default: return null;
        }
    }
}
