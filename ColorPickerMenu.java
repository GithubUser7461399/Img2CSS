import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class ColorPickerMenu {
    JFrame frame;
    ImageLoader loader;
    JTextField inputColors;
    BufferedImage image;
    int numColors;
    private List<ColorCluster> colors;
    private List<ColorPicker> colorPickers = new ArrayList<>();
    private List<ColorCluster> finalColors = new ArrayList<>();
    public ColorPickerMenu(JFrame frame, ImageLoader loader, JTextField inputColors) {
        this.frame = frame;
        this.loader = loader;
        this.inputColors = inputColors;
    }
    public void createAndShowGUI() {
        JPopupMenu colorMenu = new JPopupMenu();
        frame.add(colorMenu);
        JButton dropdownButton = new JButton("Generate Colours");
        dropdownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!loader.getImage().equals(image) || Integer.parseInt(inputColors.getText())!=numColors) {
                        image = loader.getImage();
                        numColors = Integer.parseInt(inputColors.getText());
                        colors = initializeClustersKMeansPlusPlus(image, numColors);
                        colorPickers.clear();
                        colorMenu.removeAll();
                        for (ColorCluster color : colors) {
                            colorPickers.add(new ColorPicker(frame, color.color));
                        }
                        for (ColorPicker colorPicker : colorPickers) {
                            colorMenu.add(colorPicker.createAndShowGUI());
                        }
                    }
                    colorMenu.show(dropdownButton, 0, dropdownButton.getHeight());
                } catch (Exception f) {
                    JOptionPane.showMessageDialog(frame, "Error generating colours: " + f.getMessage());
                    System.out.println(f.getMessage());
                }
            }
        });
        frame.add(dropdownButton);
    }
    public List<ColorCluster> getColours() {
        finalColors.clear();
        for (ColorPicker colorPicker : colorPickers) {
            finalColors.add(new ColorCluster(colorPicker.getColor()));
        }
        return finalColors;
    }
    private static List<ColorCluster> initializeClustersKMeansPlusPlus(BufferedImage image, int numColors) {
        List<ColorCluster> clusters = new ArrayList<>();
        Random random = new Random();
    
        // Step 1: Randomly select the first cluster center
        int randomX = random.nextInt(image.getWidth());
        int randomY = random.nextInt(image.getHeight());
        Color firstCenterColor = new Color(image.getRGB(randomX, randomY));
        clusters.add(new ColorCluster(firstCenterColor));
    
        // Step 2: Select remaining cluster centers
        for (int i = 1; i < numColors; i++) {
            double[] distances = new double[image.getWidth() * image.getHeight()];
            double totalDistance = 0;
    
            // Calculate distances from the nearest cluster center
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color pixelColor = new Color(image.getRGB(x, y));
                    double minDistance = Double.MAX_VALUE;
    
                    for (ColorCluster cluster : clusters) {
                        double distance = calculateColorDistance(cluster.color, pixelColor);
                        if (distance < minDistance) {
                            minDistance = distance;
                        }
                    }
                    distances[y * image.getWidth() + x] = minDistance;
                    totalDistance += minDistance;
                }
            }
    
            // Select the next cluster center based on the distance probabilities
            double randomValue = random.nextDouble() * totalDistance;
            double cumulativeDistance = 0;
    
            for (int j = 0; j < distances.length; j++) {
                cumulativeDistance += distances[j];
                if (cumulativeDistance >= randomValue) {
                    int selectedX = j % image.getWidth();
                    int selectedY = j / image.getWidth();
                    Color newCenterColor = new Color(image.getRGB(selectedX, selectedY));
                    clusters.add(new ColorCluster(newCenterColor));
                    break;
                }
            }
        }
    
        return clusters;
    }    
    private static double calculateColorDistance(Color c1, Color c2) {
        return Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2) +
                         Math.pow(c1.getGreen() - c2.getGreen(), 2) +
                         Math.pow(c1.getBlue() - c2.getBlue(), 2));
    }
}
