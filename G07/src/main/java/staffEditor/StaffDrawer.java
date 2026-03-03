package staffEditor;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class StaffDrawer 
{
    private Font bassClefFont; // 低音譜號字體

    public StaffDrawer() 
    {
        // 初始化低音譜號字體
        try (InputStream is = getClass().getResourceAsStream("/fonts/Bravura.otf")) 
        {
            if (is != null) 
            {
                bassClefFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(50f);
            } else 
            {
                System.out.println("字體檔案未找到");
            }
        } catch (FontFormatException | IOException e) 
        {
            e.printStackTrace();
        }
    }

    /**
     * 繪製五線譜
     *
     * @param g                繪圖物件
     * @param staffLines       五線譜行數
     * @param measurePositions 每行小節的 X 座標
     * @param startX           五線譜起始 X 座標
     * @param startY           第一行五線譜起始 Y 座標
     * @param endX             五線譜結束 X 座標
     */
    public void drawStaff(Graphics g, int staffLines, int[] measurePositions, int startX, int startY, int endX) 
    {
        int offset = 0;
        g.setColor(Color.BLACK);
        
        // 繪製五線譜和小節
        for (int i = 0; i < staffLines; i++) 
        {
            for (int j = 0; j < 5; j++) 
            {
                g.drawLine(startX, startY + j * 10 + offset, endX, startY + j * 10 + offset);
            }

            // 繪製譜號
            if (i % 2 == 0) 
            { // 偶數行為高音譜號
                g.setFont(new Font("Default", Font.PLAIN, 90));
                g.drawString("\uD834\uDD1E", startX + 10, startY + 47 + offset);
            } else 
            { // 奇數行為低音譜號
                if (bassClefFont != null) 
                {
                    g.setFont(bassClefFont);
                }
                g.drawString("\uD834\uDD22", startX + 25, startY + 20 + offset);
            }

            // 繪製小節線
            for (int pos : measurePositions) 
            {
                g.drawLine(pos, startY + offset, pos, startY + 40 + offset);
            }

            offset += 125; // 下一行的起始偏移
        }
    }
    public void drawSelectionBoxes(Graphics g, boolean selectionMode, List<Measure> selectedCopyMeasures, List<Measure> selectedPasteMeasures) 
    {
		if (selectionMode) 
		{
		// 繪製複製選取框
		if (selectedCopyMeasures != null) 
		{
				for (Measure measure : selectedCopyMeasures) 
				{
					g.setColor(new Color(255, 255, 0, 128)); // 半透明黃色背景
					g.fillRect(
					measure.startX,
					measure.startY,
					measure.endX - measure.startX,
					measure.endY - measure.startY
					);
					g.setColor(Color.RED); // 紅色邊框
					g.drawRect(
					measure.startX,
					measure.startY,
					measure.endX - measure.startX,
					measure.endY - measure.startY
					);
				}
			}
			
			// 繪製貼上選取框
			if (selectedPasteMeasures != null) 
			{
				for (Measure measure : selectedPasteMeasures) 
				{
					g.setColor(new Color(0, 255, 0, 128)); // 半透明綠色背景
					g.fillRect(
					measure.startX,
					measure.startY,
					measure.endX - measure.startX,
					measure.endY - measure.startY
					);
					g.setColor(Color.BLUE); // 藍色邊框
					g.drawRect(
					measure.startX,
					measure.startY,
					measure.endX - measure.startX,
					measure.endY - measure.startY
					);
				}
			}
		}
    }
}

