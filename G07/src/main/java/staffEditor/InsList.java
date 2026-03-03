package staffEditor;

import javax.swing.*;
import javax.sound.midi.*;
import java.awt.*;
import java.awt.event.*;

public class InsList extends JPanel {

    // 🎵 MIDI 樂器 Program Number（General MIDI 標準）
    int[] instrumentProgramNumbers = { 
        0,   // 鋼琴
        40,  // 小提琴
        73,  // 長笛
        65,  // 薩克斯風
        71   // 豎笛
    };

    // UI 顯示名稱 & 圖片
    String[] instrumentNames = {"鋼琴", "小提琴", "長笛", "薩克斯風", "豎笛"};
    String[] instrumentIcons = {
        "images/paino.png",
        "images/violin.png",
        "images/flute.png",
        "images/saxophone.png",
        "images/clarinet.png"
    };

    JButton[] instruments;
    JButton selectedButton = null;
    int selectedIndex = -1;  // 目前選中的音色

    // Java MIDI 裝置
    Synthesizer synthesizer;
    MidiChannel channel;

    InsList(InsMenu p) {

        // 初始化 MIDI Synth
        initMidi();

        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(180, 347));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        instruments = new JButton[instrumentNames.length];

        this.add(Box.createVerticalGlue());

        for (int i = 0; i < instrumentNames.length; i++) {

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
            buttonPanel.setBackground(Color.BLACK);

            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(instrumentIcons[i]));
            Image scaled = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaled);

            JButton btn = new JButton(instrumentNames[i], scaledIcon);
            btn.setForeground(Color.BLACK);
            btn.setBackground(Color.WHITE);
            btn.setOpaque(true);
            btn.setMaximumSize(new Dimension(160, 50));
            btn.setPreferredSize(new Dimension(160, 50));
            btn.setHorizontalAlignment(SwingConstants.LEFT);

            final int index = i;

            btn.addActionListener(e -> {
                // UI 更新
                if (selectedButton != null) {
                    selectedButton.setBackground(Color.WHITE);
                    selectedButton.setForeground(Color.BLACK);
                }

                btn.setBackground(new Color(168, 168, 168));
                btn.setForeground(Color.WHITE);

                selectedButton = btn;
                selectedIndex = index;

                // ▶ 撥放示範音（Java MIDI)
                playPreviewSound(instrumentProgramNumbers[index]);
            });

            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(btn);
            buttonPanel.add(Box.createHorizontalGlue());

            this.add(buttonPanel);
            this.add(Box.createVerticalStrut(10));

            instruments[i] = btn;
        }

        this.add(Box.createVerticalGlue());
    }

    // =====================================================
    // 初始化 Java MIDI Synthesizer
    // =====================================================
    private void initMidi() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();

            MidiChannel[] channels = synthesizer.getChannels();
            channel = channels[0]; // 使用 channel 0

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // 播放示範音色（用 MIDI，不用 JFugue）
    // =====================================================
    private void playPreviewSound(int programNumber) {
        try {
            channel.programChange(programNumber);

            int pitch = 60; // C4 (示範)
            channel.noteOn(pitch, 90);

            new Thread(() -> {
                try { Thread.sleep(300); } catch (Exception ignored) {}
                channel.noteOff(pitch);
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // 播放旋律時由 PlayButton → MidiPlayer 使用
    // =====================================================
    public int getSelectedProgramNumber() {
        if (selectedIndex >= 0) {
            return instrumentProgramNumbers[selectedIndex];
        }
        return 0; // 預設鋼琴
    }
}
