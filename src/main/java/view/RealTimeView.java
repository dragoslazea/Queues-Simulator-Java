package view;

import javax.swing.*;
import java.awt.*;

// Clasa efectiva pentru interfata grafica ce permite vizualizarea simularii in timp real
public class RealTimeView extends JFrame {
    // Variabile instanta (private)
    private PaintPanel paintPanel; // panoul in care se va face desenarea

    // Constructor
    public RealTimeView() {
        this.setTitle("Real Time Simulation");
        this.setSize(new Dimension(1100, 800));
        this.setLocation(180, 120);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.paintPanel = new PaintPanel();
        this.setBackground(Color.BLACK);
        this.setContentPane(this.paintPanel);
    }

    // Getter pentru panoul de desenare
    public PaintPanel getPaintPanel() {
        return paintPanel;
    }
}
