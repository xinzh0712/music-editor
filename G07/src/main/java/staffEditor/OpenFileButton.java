//package staffEditor;
//
//import javax.swing.*;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import java.awt.event.ActionEvent;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.xml.parsers.*;
//import org.w3c.dom.*;
//
//public class OpenFileButton extends IconButton {
//
//    StaffPage page;
//    Toolbar toolbar;
//
//    public OpenFileButton(Toolbar toolbar, StaffPage page) {
//        super(toolbar);
//        this.page = page;
//        this.toolbar = toolbar;
//
//        imageURL = cldr.getResource("images/open-folder.png");
//        icon = new ImageIcon(imageURL);
//        this.setIcon(icon);
//        this.setToolTipText("開啟檔案");
//
//        this.addActionListener((ActionEvent e) -> openFile());
//    }
//
//    private void openFile() {
//    	
//    	page.rebuildMeasures();   // ⭐ 必須：重建 measures
//        page.layoutMeasures();    // ⭐ 必須：重新計算小節位置
//
//        JFileChooser chooser = new JFileChooser();
//        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Music XML", "xml"));
//        chooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG image", "png"));
//        chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG image", "jpg"));
//        chooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF document", "pdf"));
//        chooser.setAcceptAllFileFilterUsed(true);
//
//        int ret = chooser.showOpenDialog(this);
//
//        if (ret != JFileChooser.APPROVE_OPTION) return;
//
//        File selected = chooser.getSelectedFile();
//        String path = selected.getAbsolutePath().toLowerCase();
//
//        // 尋找 XML 資料檔
//        File xmlFile;
//
//        if (path.endsWith(".xml")) {
//            xmlFile = selected;
//        } else {
//            xmlFile = new File(selected.getParent(),
//                    selected.getName().replaceFirst("[.][^.]+$", "") + ".xml");
//        }
//
//        if (!xmlFile.exists()) {
//            JOptionPane.showMessageDialog(this,
//                    "找不到對應的資料檔 (.xml)，無法還原音符。\n" + xmlFile.getAbsolutePath(),
//                    "錯誤", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        try {
//            loadXML(xmlFile);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this,
//                    "讀取 XML 失敗：" + ex.getMessage(),
//                    "錯誤", JOptionPane.ERROR_MESSAGE);
//            ex.printStackTrace();
//        }
//    }
//
//    private void loadXML(File xmlFile) throws Exception {
//
//        TabbedPane tabs = toolbar.parent.getTabbedPane();
//        tabs.clearAllPages();
//
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.parse(xmlFile);
//
//        Element root = doc.getDocumentElement();
//        NodeList pageNodes = root.getElementsByTagName("page");
//
//        for (int i = 0; i < pageNodes.getLength(); i++) {
//
//            StaffPage p;
//
//            if (i == 0)
//                p = page;
//            else
//                p = tabs.createNewPage();
//
//            // ⭐⭐ 重建 measures（正確時機） ⭐⭐
//            p.rebuildMeasures();
//
//            Element pageEl = (Element) pageNodes.item(i);
//
//            if (i == 0) {
//                p.staffTitle.setText(getText(root, "title"));
//                p.authorTitle.setText(getText(root, "author"));
//                p.instrumentTitle.setText(getText(root, "instrument"));
//            }
//
//            NodeList noteNodes = pageEl.getElementsByTagName("note");
//
//            for (int j = 0; j < noteNodes.getLength(); j++) {
//                Element nEl = (Element) noteNodes.item(j);
//
//                int x = Integer.parseInt(nEl.getAttribute("x"));
//                int y = Integer.parseInt(nEl.getAttribute("y"));
//                String pitch = nEl.getAttribute("pitch");
//                String dur = nEl.getAttribute("duration");
//
//                longType type = convertDurationToEnum(dur);
//
//                p.addNoteToPage(x, y, type, pitch);
//            }
//
//            p.repaint();
//        }
//
//        JOptionPane.showMessageDialog(this,
//                "成功載入：" + xmlFile.getName(),
//                "Done", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    private longType convertDurationToEnum(String d) {
//        switch (d) {
//            case "whole": return longType.whole;
//            case "half": return longType.half;
//            case "quarter": return longType.quarter;
//            case "eighth": return longType.eighth;
//            case "sixteenth": return longType.sixteenth;
//
//            case "wholeR": return longType.wholeR;
//            case "halfR": return longType.halfR;
//            case "quarterR": return longType.quarterR;
//            case "eighthR": return longType.eighthR;
//            case "sixteenthR": return longType.sixteenthR;
//
//            default:
//                System.out.println("Unknown duration: " + d);
//                return longType.quarter;
//        }
//    }
//
//
//    private String getText(Element parent, String tag) {
//        NodeList list = parent.getElementsByTagName(tag);
//        if (list.getLength() == 0) return "";
//        return list.item(0).getTextContent();
//    }
//}
