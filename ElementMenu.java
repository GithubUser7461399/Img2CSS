import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
public class ElementMenu {
    JFrame frame;
    List<JTextField> elements = new ArrayList<>();
    List<ColorCluster> colors = new ArrayList<>();
    public ElementMenu(JFrame frame, List<ColorCluster> colors) {
        this.frame = frame;
        this.colors = colors;
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
                elements.add(new JTextField("<div style=\"background-colour: #" + cluster.color.getRGB() + ";\"></div>"));
                panel.add(elements.get(elements.size()-1));
                frame.add(panel);
                JButton submit = new JButton("Generate Matrix");
                submit.addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                    
                   } 
                });
            }
        } catch (Exception f) {
            JOptionPane.showMessageDialog(frame, "Error loading Matrix generator: " + f.getMessage());
        }
    }
}