import javax.swing.*;
import java.awt.*;
import java.util.List;
public class ElementMenu {
    JFrame frame;
    List<JTextField> elements;
    List<ColorCluster> colors;
    public ElementMenu(JFrame frame, List<ColorCluster> colors) {
        this.frame = frame;
        this.colors = colors;
    }
    public void createAndShowGUI() {
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
            elements.add(new JTextField("<div style=\"background-colour=#" + cluster.color.getRGB() + "\"></div>"));
            panel.add(elements.get(elements.size()-1));
            frame.add(panel);
        }
    }
}