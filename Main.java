import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Img2CSS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        ImageLoader loader = new ImageLoader(frame);
        loader.createAndShowGUI();
        JLabel colorsLabel = new JLabel("Choose number of colours");
        frame.add(colorsLabel);
        JTextField inputColors = new JTextField(5);
        frame.add(inputColors);
        ColorPickerMenu colorMenu = new ColorPickerMenu(frame, loader, inputColors);
        colorMenu.createAndShowGUI();
        JLabel heightLabel = new JLabel("Choose height of final image");
        frame.add(heightLabel);
        JTextField y = new JTextField(5);
        frame.add(y);
        JLabel widthLabel = new JLabel("Choose width of final image");
        frame.add(widthLabel);
        JTextField x = new JTextField(5);
        frame.add(x);
        JButton submit = new JButton("Submit");
        frame.add(submit);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BufferedImage image = loader.getImage();
                    int width = Integer.parseInt(x.getText());
                    int height = Integer.parseInt(y.getText());
                    List<ColorCluster> colors = colorMenu.getColours();
                    BufferedImage compressedImage = compressImage(image, width, height, colors);
                    int response = JOptionPane.showConfirmDialog(frame, "Do you want to save the image?", "Confirm", JOptionPane.OK_CANCEL_OPTION);
                    switch (response) {
                        case JOptionPane.OK_OPTION:
                            JFileChooser fileChooser = new JFileChooser();
                            fileChooser.setDialogTitle("Save File");
                            fileChooser.setSelectedFile(new File("image.jpg"));
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
                                ImageIO.write(compressedImage, "jpg", selectedDirectory);
                                JOptionPane.showMessageDialog(frame, "Compressed image saved as " + selectedDirectory.getName());
                            }
                            break;
                    
                        default:
                            break;
                    }
                    JFrame HTMLFrame = new JFrame("Generate Matrix: " + loader.getFile().getName());
                    HTMLFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    HTMLFrame.setSize(500,500);
                    HTMLFrame.setVisible(true);
                    HTMLFrame.setLayout(new BoxLayout(HTMLFrame.getContentPane(), BoxLayout.Y_AXIS));
                    ElementMenu elementMenu = new ElementMenu(HTMLFrame, colors);
                    elementMenu.createAndShowGUI();

                } catch (Exception f) {
                    JOptionPane.showMessageDialog(frame, "Error saving image: " + f.getMessage());
                }
            }
        });

    }
    private static BufferedImage compressImage(BufferedImage image, int width, int height, List<ColorCluster> colors) {
        // Resize the image
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedResizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedResizedImage.createGraphics();
        g2d.drawImage(resizedImage, 0, 0, null);
        g2d.dispose();

        // K-Means clustering
        assignPixelsToClusters(bufferedResizedImage, colors);
        //updateClusterColors(colors);

        // Create a new image with the clustered colors
        BufferedImage compressedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color closestColor = findClosestClusterColor(colors, bufferedResizedImage.getRGB(x, y));
                compressedImage.setRGB(x, y, closestColor.getRGB());
            }
        }

        return compressedImage;
    }


    private static void assignPixelsToClusters(BufferedImage image, List<ColorCluster> clusters) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                ColorCluster closestCluster = findClosestCluster(clusters, pixelColor);
                closestCluster.addPixel(new Pixel(pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue()));
            }
        }
    }

    private static double calculateColorDistance(Color c1, Color c2) {
        return Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2) +
                         Math.pow(c1.getGreen() - c2.getGreen(), 2) +
                         Math.pow(c1.getBlue() - c2.getBlue(), 2));
    }

    private static ColorCluster findClosestCluster(List<ColorCluster> colors, Color pixelColor) {
        ColorCluster closestCluster = null;
        double minDistance = Double.MAX_VALUE;

        for (ColorCluster cluster : colors) {
            double distance = calculateColorDistance(cluster.color, pixelColor);
            if (distance < minDistance) {
                minDistance = distance;
                closestCluster = cluster;
            }
        }
        return closestCluster;
    }

    //color correction step not useful in the current application
    //private static void updateClusterColors(List<ColorCluster> clusters) {
    //    for (ColorCluster cluster : clusters) {
    //        if (!cluster.pixels.isEmpty()) {
    //            cluster.color = cluster.calculateAverageColor();
    //        }
    //        cluster.clearPixels();
    //    }
    //}

    private static Color findClosestClusterColor(List<ColorCluster> clusters, int rgb) {
        Color pixelColor = new Color(rgb);
        ColorCluster closestCluster = findClosestCluster(clusters, pixelColor);
        return closestCluster.color;
    }
}