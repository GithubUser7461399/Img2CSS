import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.BorderLayout;

public class ImageLoader {
    private BufferedImage image; 
    private JFrame frame;
    private File file;
    public ImageLoader(JFrame frame) {
        this.frame = frame;
    }
    public void createAndShowGUI() {
        JLabel label = new JLabel("Current image: ");
        JButton button = new JButton("Load Image");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select Media");
                int userSelection = fileChooser.showOpenDialog(null);
                if (userSelection != JFileChooser.APPROVE_OPTION) {
                    System.out.println("No file selected.");
                    return;
                }
                file = fileChooser.getSelectedFile();
                try {
                    image = ImageIO.read(file);
                } catch (IOException f) {
                    JOptionPane.showMessageDialog(frame, "Error loading image: " + f.getMessage());
                    return;
                }
                label.setText("Current file: " + file.getName() + ", Dimensions: " + image.getWidth() + "px, " + image.getHeight() + "px.");
            }});
        frame.add(label);
        frame.add(button, BorderLayout.NORTH);
    }
    public BufferedImage getImage() {
        return image;
    }
    public File getFile() {
    return file;
    }
}