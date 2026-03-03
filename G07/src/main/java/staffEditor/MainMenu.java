package staffEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("音樂共鳴編曲系統");
        setSize(500, 400);
        setLocationRelativeTo(null);  // 視窗置中
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // 標題
        JLabel title = new JLabel("音樂共鳴 編曲系統", SwingConstants.CENTER);
        title.setFont(new Font("微軟正黑體", Font.BOLD, 28));
        title.setBounds(50, 50, 400, 60);
        add(title);

        // 開始編輯按鈕
        JButton startBtn = new JButton("開始編輯");
        startBtn.setFont(new Font("微軟正黑體", Font.PLAIN, 22));
        startBtn.setBounds(150, 200, 200, 60);
        add(startBtn);

        // 點擊進入 SECreate()
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SECreate();  // 開啟你的編輯器
                dispose();       // 關閉主畫面
            }
        });

        setVisible(true);
    }
}
