import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class ColorPicker {
    JFrame frame;
    private Color color;
    public ColorPicker(JFrame frame, Color color) {
        this.frame = frame;
        this.color = color;
    }
    public JPanel createAndShowGUI() {
        JButton colorPickerButton = new JButton("Pick a Color");
        JLabel colorLabel = new JLabel("          ");
        colorLabel.setOpaque(true); // Make the label opaque to show background color
        colorLabel.setPreferredSize(new Dimension(30, 30));
        if (color != null) {
            // Set the background color of the label to the selected color
            colorLabel.setBackground(color);
        }
        colorPickerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the color picker dialog
                Color newcolor = JColorChooser.showDialog(frame, "Choose a Color", Color.BLACK);
                color = newcolor;
                if (color != null) {
                    // Set the background color of the label to the selected color
                    colorLabel.setBackground(color);
                }
            }
        });
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(colorPickerButton);
        panel.add(colorLabel);
        return panel;
    }
    public Color getColor() {
        return color;
    }
}