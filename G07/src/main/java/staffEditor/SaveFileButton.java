//package staffEditor;
//
//import javax.swing.*;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.imageio.ImageIO;
//
//import com.itextpdf.io.image.ImageDataFactory;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.util.List;
//
//public class SaveFileButton extends IconButton {
//
//    private final StaffPage page;
//    private final JFileChooser fileChooser;
//
//    public SaveFileButton(Toolbar toolbar, StaffPage page) {
//        super(toolbar);
//        this.page = page;
//
//        imageURL = cldr.getResource("images/save.png");
//        this.setIcon(new ImageIcon(imageURL));
//        this.setToolTipText("儲存檔案");
//
//        // File chooser 設定
//        fileChooser = new JFileChooser();
//        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
//        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPG Image", "jpg"));
//        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Document", "pdf"));
//        fileChooser.setAcceptAllFileFilterUsed(false);
//
//        this.addActionListener(e -> saveFile());
//    }
//
//    // ============================================================
//    // ⭐ 主要儲存流程
//    // ============================================================
//    private void saveFile() {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                TabbedPane tabbedPane = parent.getTabbedPane();
//                List<StaffPage> allPages = tabbedPane.getAllStaffPages();
//
//                int result = fileChooser.showSaveDialog(this);
//                if (result != JFileChooser.APPROVE_OPTION) return;
//
//                File selected = fileChooser.getSelectedFile();
//                String ext = getSelectedExtension();
//
//                if (ext == null) {
//                    JOptionPane.showMessageDialog(this, "請選擇輸出格式");
//                    return;
//                }
//
//                // 加副檔名
//                if (!selected.getName().toLowerCase().endsWith("." + ext)) {
//                    selected = new File(selected.getAbsolutePath() + "." + ext);
//                }
//
//                switch (ext) {
//                    case "png":
//                    case "jpg":
//                        saveImage(selected, ext, allPages);
//                        break;
//
//                    case "pdf":
//                        savePDF(selected, allPages);
//                        break;
//                }
//
//                // XML 同步存檔
//                File xmlFile = new File(selected.getAbsolutePath().replace("." + ext, ".xml"));
//                saveXML(xmlFile, allPages);
//
//                JOptionPane.showMessageDialog(this,
//                    "已成功儲存：\n" + selected.getAbsolutePath() +
//                    "\n並同步輸出 XML：" + xmlFile.getAbsolutePath()
//                );
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                JOptionPane.showMessageDialog(this, "儲存過程發生錯誤：" + ex.getMessage());
//            }
//        });
//    }
//
//
//    // ============================================================
//    // ⭐ PNG / JPG 單檔儲存
//    // ============================================================
//    private void saveImage(File file, String ext, List<StaffPage> allPages) throws Exception {
//
//        BufferedImage img = page.AllPagesToImage();
//        if (img == null) throw new IOException("AllPagesToImage() 回傳 null");
//
//        // JPG 不支援透明 → 轉成 RGB
//        if (img.getType() != BufferedImage.TYPE_INT_RGB) {
//            BufferedImage rgb = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
//            Graphics2D g = rgb.createGraphics();
//            g.drawImage(img, 0, 0, null);
//            g.dispose();
//            img = rgb;
//        }
//
//        ImageIO.write(img, ext, file);
//    }
//
//
//    // ============================================================
//    // ⭐ PDF 多頁輸出
//    // ============================================================
//    private void savePDF(File file, List<StaffPage> allPages) throws Exception {
//
//        try (PdfWriter writer = new PdfWriter(file);
//             PdfDocument pdf = new PdfDocument(writer);
//             Document document = new Document(pdf)) {
//
//            for (StaffPage p : allPages) {
//                BufferedImage image = p.renderToImage();
//                if (image == null) throw new IOException("渲染頁面失敗");
//
//                byte[] imgBytes = writeToByteArray(image, "png");
//                com.itextpdf.io.image.ImageData imgData =
//                        ImageDataFactory.create(imgBytes);
//
//                // ⭐ 用完整類別名稱
//                com.itextpdf.layout.element.Image pdfImage =
//                        new com.itextpdf.layout.element.Image(imgData);
//
//                pdfImage.setAutoScale(true);
//
//                document.add(pdfImage);
//            }
//        }
//    }
//
//
//    // ============================================================
//    // ⭐ XML 輸出樂譜資料（音符座標 + 音高 + 時值）
//    // ============================================================
//    private void saveXML(File file, List<StaffPage> allPages) throws Exception {
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//
//            writer.write("<music>\n");
//
//            for (int i = 0; i < allPages.size(); i++) {
//
//                StaffPage p = allPages.get(i);
//                writer.write("  <page index=\"" + (i + 1) + "\">\n");
//
//                for (NoteData note : p.getNotesForPlayback()) {
//
//                    // 統一休止符 pitch 格式
//                    String pitch = note.getPitch();
//                    if (pitch.equals("R")) pitch = "rest";
//
//                    writer.write(String.format(
//                        "    <note x=\"%d\" y=\"%d\" pitch=\"%s\" duration=\"%s\" measure=\"%d\" />\n",
//                        note.getX(),
//                        note.getY(),
//                        pitch,
//                        note.getDuration(),
//                        note.getMeasureIndex()
//                    ));
//                }
//
//                writer.write("  </page>\n");
//            }
//
//            writer.write("</music>\n");
//        }
//    }
//
//
//    // ============================================================
//    // ⭐ 工具：取得使用者選擇的副檔名
//    // ============================================================
//    private String getSelectedExtension() {
//        String desc = fileChooser.getFileFilter().getDescription();
//        if (desc.contains("PNG")) return "png";
//        if (desc.contains("JPG")) return "jpg";
//        if (desc.contains("PDF")) return "pdf";
//        return null;
//    }
//
//    // ============================================================
//    // ⭐ 工具：BufferedImage → byte[]
//    // ============================================================
//    private byte[] writeToByteArray(BufferedImage img, String format) throws Exception {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(img, format, baos);
//        return baos.toByteArray();
//    }
//
//}
