import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
public class ElementMenu {
    JFrame frame;
    BufferedImage image;
    private List<JTextField> elements = new ArrayList<>();
    private List<ColorCluster> colors = new ArrayList<>();
    public ElementMenu(JFrame frame, BufferedImage image, List<ColorCluster> colors) {
        this.frame = frame;
        this.colors = colors;
        this.image = image;
        this.elements = new ArrayList<>();
    }
    public void createAndShowGUI() {
        try {
            for (ColorCluster cluster : colors) {
                JLabel colorLabel = new JLabel("          ");
                colorLabel.setOpaque(true);
                colorLabel.setPreferredSize(new Dimension(30, 30));
                if (cluster.color != null) {
                    colorLabel.setBackground(cluster.color);
                }
                JPanel panel = new JPanel();
                panel.setLayout(new FlowLayout());
                panel.add(colorLabel);
                elements.add(new JTextField("<div style=\"background-color: " + String.format("#%06x", cluster.color.getRGB() & 0x00FFFFFF) + ";\"></div>"));
                panel.add(elements.get(elements.size()-1));
                frame.add(panel);
            }
            JButton submit = new JButton("Confirm Elements");
                submit.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                    String matrix = "<div style=\"width:" + image.getWidth()*10 + "px; height:" + image.getHeight()*10 + "px; display: grid; grid-template-columns: repeat(" + image.getWidth() + ", 10px); grid-template-rows: repeat(" + image.getHeight() + ", 10px);\">";
                    for (int i = 0; i < image.getWidth(); i++) {
                        for (int j = 0; j < image.getHeight(); j++) {
                            for (int k = 0; k < elements.size(); k++) {
                                if (image.getRGB(i, j)==colors.get(k).color.getRGB()) {
                                    matrix = matrix + elements.get(k).getText();
                                }
                            }
                        }
                        matrix = matrix + "\n";
                    }
                    matrix = matrix + "</div>";
                    Object[] options = { "Save as HTML", "Copy HTML", "Cancel" };
                    int saveMatrix = JOptionPane.showOptionDialog(null, "How would you like to save your Matrix?", "Save Matrix", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                    switch (saveMatrix) {
                        case JOptionPane.YES_OPTION:
                            JFileChooser fileChooser = new JFileChooser();
                            fileChooser.setDialogTitle("Save File");
                            fileChooser.setSelectedFile(new File("matrix.html"));
                            int returnValue = fileChooser.showSaveDialog(frame);
                            if (returnValue == JFileChooser.APPROVE_OPTION) {
                                File selectedDirectory = fileChooser.getSelectedFile();
                                if (selectedDirectory.exists()) {
                                    int overwrite = JOptionPane.showConfirmDialog(frame,
                                    "File already exists. Do you want to overwrite it?",
                                    "Confirm Overwrite",
                                    JOptionPane.YES_NO_OPTION);
                                    if (overwrite != JOptionPane.YES_OPTION) {
                                        return; // User chose not to overwrite
                                    }
                                }
                                try {
                                    FileWriter filewriter = new FileWriter(selectedDirectory);
                                    filewriter.write(matrix);
                                    filewriter.close();
                                } catch (IOException f) {
                                    System.out.println(f);
                                }
                                JOptionPane.showMessageDialog(frame, "Compressed image saved as " + selectedDirectory.getName());
                            }
                            return;
                        case JOptionPane.NO_OPTION:
                            JOptionPane.showMessageDialog(frame, matrix);
                            System.out.print(matrix);
                        return;
                    }
                   } 
                });
                frame.add(submit);
        } catch (Exception f) {
            JOptionPane.showMessageDialog(frame, "Error loading Matrix generator: " + f.getMessage());
        }
    }
}