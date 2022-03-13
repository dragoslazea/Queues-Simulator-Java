package view;

import model.SelectionPolicy;
import controller.SimulationManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Clasa pentru interfata grafica in care utilizatorul introduce datele simularii - JFrame
public class GUI extends JFrame {
    // Panoul de continut
    private JPanel contentPanel = new JPanel();

    // Etichete
    private JLabel titleLabel = new JLabel("Queues Simulator");
    private JLabel nbClients = new JLabel("Number of Clients N: ");
    private JLabel nbQueues = new JLabel("Number of Queues Q: ");
    private JLabel simulationInterval = new JLabel("Simulation Interval: ");
    private JLabel minArrivalTime = new JLabel("Minimum Arrival Time: ");
    private JLabel maxArrivalTime = new JLabel("Maximum Arrival Time: ");
    private JLabel minServiceTime = new JLabel("Minimum Service Time: ");
    private JLabel maxServiceTime = new JLabel("Maximum Service Time: ");
    private JLabel selectionPolicy = new JLabel("Selection Policy: ");

    // Campuri de text
    private JTextField nTxtField = new JTextField();
    private JTextField qTxtField = new JTextField();
    private JTextField tSimTxtField = new JTextField();
    private JTextField tMinArrTxtField = new JTextField();
    private JTextField tMaxArrTxtField = new JTextField();
    private JTextField tMinSerTxtField = new JTextField();
    private JTextField tMaxSerTxtField = new JTextField();

    // Combo box
    private JComboBox policy;

    // Butoane
    private JButton btnStart = new JButton("Start Simulation");
    private JButton btnReset = new JButton("Reset Simulator");

    // Componenta de view care permite vizualizarea in timp real a simularii
    private RealTimeView realTimeView;

    // Constructor
    public GUI() {
        this.realTimeView = new RealTimeView();
        this.realTimeView.setVisible(true);
        this.setTitle("Queues Simulator");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(450, 580));
        this.setLocation(this.realTimeView.getX() + this.realTimeView.getWidth() + 20, 230);
        this.setContentPane(contentPanel);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(118, 181, 197));


        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(128, 57, 30));
        titleLabel.setAlignmentX(0.5f);
        contentPanel.add(titleLabel);

        JPanel clientsPanel = new JPanel();
        clientsPanel.setBackground(new Color(118, 181, 197));
        this.nbClients.setFont(new Font("Arial", Font.BOLD, 20));
        this.nbClients.setForeground(new Color(128, 57, 30));
        this.nTxtField.setBackground(new Color(128, 57, 30));
        this.nTxtField.setForeground(new Color(118, 181, 197));
        this.nTxtField.setFont(new Font("Arial", Font.BOLD, 20));
        this.nTxtField.setPreferredSize(new Dimension(this.getWidth() / 3 - 50, 30));
        clientsPanel.add(nbClients);
        clientsPanel.add(nTxtField);
        contentPanel.add(clientsPanel);

        JPanel queuesPanel = new JPanel();
        queuesPanel.setBackground(new Color(118, 181, 197));
        this.nbQueues.setFont(new Font("Arial", Font.BOLD, 20));
        this.nbQueues.setForeground(new Color(128, 57, 30));
        this.qTxtField.setBackground(new Color(128, 57, 30));
        this.qTxtField.setForeground(new Color(118, 181, 197));
        this.qTxtField.setFont(new Font("Arial", Font.BOLD, 20));
        this.qTxtField.setPreferredSize(new Dimension(this.getWidth() / 3 - 50, 30));
        queuesPanel.add(nbQueues);
        queuesPanel.add(qTxtField);
        contentPanel.add(queuesPanel);

        JPanel simPanel = new JPanel();
        simPanel.setBackground(new Color(118, 181, 197));
        this.simulationInterval.setFont(new Font("Arial", Font.BOLD, 20));
        this.simulationInterval.setForeground(new Color(128, 57, 30));
        this.tSimTxtField.setBackground(new Color(128, 57, 30));
        this.tSimTxtField.setForeground(new Color(118, 181, 197));
        this.tSimTxtField.setFont(new Font("Arial", Font.BOLD, 20));
        this.tSimTxtField.setPreferredSize(new Dimension(this.getWidth() / 3 - 50, 30));
        simPanel.add(simulationInterval);
        simPanel.add(tSimTxtField);
        contentPanel.add(simPanel);

        JPanel minArrPanel = new JPanel();
        minArrPanel.setBackground(new Color(118, 181, 197));
        this.minArrivalTime.setFont(new Font("Arial", Font.BOLD, 20));
        this.minArrivalTime.setForeground(new Color(128, 57, 30));
        this.tMinArrTxtField.setBackground(new Color(128, 57, 30));
        this.tMinArrTxtField.setForeground(new Color(118, 181, 197));
        this.tMinArrTxtField.setFont(new Font("Arial", Font.BOLD, 20));
        this.tMinArrTxtField.setPreferredSize(new Dimension(this.getWidth() / 3 - 50, 30));
        minArrPanel.add(minArrivalTime);
        minArrPanel.add(tMinArrTxtField);
        contentPanel.add(minArrPanel);

        JPanel maxArrPanel = new JPanel();
        maxArrPanel.setBackground(new Color(118, 181, 197));
        this.maxArrivalTime.setFont(new Font("Arial", Font.BOLD, 20));
        this.maxArrivalTime.setForeground(new Color(128, 57, 30));
        this.tMaxArrTxtField.setBackground(new Color(128, 57, 30));
        this.tMaxArrTxtField.setForeground(new Color(118, 181, 197));
        this.tMaxArrTxtField.setFont(new Font("Arial", Font.BOLD, 20));
        this.tMaxArrTxtField.setPreferredSize(new Dimension(this.getWidth() / 3 - 50, 30));
        maxArrPanel.add(maxArrivalTime);
        maxArrPanel.add(tMaxArrTxtField);
        contentPanel.add(maxArrPanel);

        JPanel minServPanel = new JPanel();
        minServPanel.setBackground(new Color(118, 181, 197));
        this.minServiceTime.setFont(new Font("Arial", Font.BOLD, 20));
        this.minServiceTime.setForeground(new Color(128, 57, 30));
        this.tMinSerTxtField.setBackground(new Color(128, 57, 30));
        this.tMinSerTxtField.setForeground(new Color(118, 181, 197));
        this.tMinSerTxtField.setFont(new Font("Arial", Font.BOLD, 20));
        this.tMinSerTxtField.setPreferredSize(new Dimension(this.getWidth() / 3 - 50, 30));
        minServPanel.add(minServiceTime);
        minServPanel.add(tMinSerTxtField);
        contentPanel.add(minServPanel);

        JPanel maxServPanel = new JPanel();
        maxServPanel.setBackground(new Color(118, 181, 197));
        this.maxServiceTime.setFont(new Font("Arial", Font.BOLD, 20));
        this.maxServiceTime.setForeground(new Color(128, 57, 30));
        this.tMaxSerTxtField.setBackground(new Color(128, 57, 30));
        this.tMaxSerTxtField.setForeground(new Color(118, 181, 197));
        this.tMaxSerTxtField.setFont(new Font("Arial", Font.BOLD, 20));
        this.tMaxSerTxtField.setPreferredSize(new Dimension(this.getWidth() / 3 - 50, 30));
        maxServPanel.add(maxServiceTime);
        maxServPanel.add(tMaxSerTxtField);
        contentPanel.add(maxServPanel);

        JPanel policyPanel = new JPanel();
        policyPanel.setBackground(new Color(118, 181, 197));
        this.selectionPolicy.setFont(new Font("Arial", Font.BOLD, 20));
        this.selectionPolicy.setForeground(new Color(128, 57, 30));
        String[] choices = {"Time", "Queue"};
        this.policy = new JComboBox(choices);
        this.policy.setForeground(new Color(118, 181, 197));
        this.policy.setFont(new Font("Arial", Font.BOLD, 20));
        this.policy.setBackground(new Color(128, 57, 30));
        policyPanel.add(selectionPolicy);
        policyPanel.add(policy);
        contentPanel.add(policyPanel);

        btnStart.setBackground(new Color(128, 57, 30));
        btnStart.setForeground(new Color(118, 181, 197));
        btnStart.setFont(new Font("Arial", Font.BOLD, 20));
        btnStart.setAlignmentX(0.5f);
        GUI gui = this;
        // Action Listener pentru butonul care porneste simularea
        btnStart.addActionListener(new ActionListener() {
            // Suprascrierea metodei actionPerformed
            @Override
            public void actionPerformed(ActionEvent e) {
                SimulationManager simulationManager = new SimulationManager(gui); // la apasarea butonului de start se creeaza si se pune in executie un nou manager de simulare
                Thread t = new Thread(simulationManager);
                t.start();
                realTimeView.getPaintPanel().doRepaint(simulationManager.getGeneratedClients(), simulationManager.getScheduler().getServers());
            }
        });
        contentPanel.add(btnStart);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(new Color(118, 181, 197));
        JLabel emptyLabel = new JLabel("");
        emptyLabel.setFont(new Font("Arial", Font.BOLD, 5));
        emptyLabel.setForeground(new Color(118, 181, 197));
        emptyPanel.add(emptyLabel);
        contentPanel.add(emptyPanel);

        btnReset.setBackground(new Color(128, 57, 30));
        btnReset.setForeground(new Color(118, 181, 197));
        btnReset.setFont(new Font("Arial", Font.BOLD, 20));
        btnReset.setAlignmentX(0.5f);
        // Action Listener pentru butonul de Reset - goleste campurile de text in vederea introducerii unui nou set de date pentru simulare
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.reset();
            }
        });
        contentPanel.add(btnReset);
    }

    // Getter
    public RealTimeView getRealTimeView() {
        return realTimeView;
    }

    // Metoda pentru afisarea unei ferestre cu un mesaj de eroare
    public void showError(String errMessage) {
        JOptionPane.showMessageDialog(this, errMessage);
    }

    // Metoda pentru resetarea campurilor de text la apasarea butonului Reset
    public void reset() {
        nTxtField.setText("");
        qTxtField.setText("");
        tSimTxtField.setText("");
        tMinArrTxtField.setText("");
        tMaxArrTxtField.setText("");
        tMinSerTxtField.setText("");
        tMaxSerTxtField.setText("");
    }

    // Metode pentru preluarea informatiilor din interfata grafica si transpunerea acestora in formatele suportate de model
    public int getN() {
        int n = 0;
        n = Integer.parseInt(this.nTxtField.getText());
        return n;
    }

    public int getQ() {
        int q = 0;
        q = Integer.parseInt(this.qTxtField.getText());
        return q;
    }

    public int getSimulationTime() {
        int t = 0;
        t = Integer.parseInt(this.tSimTxtField.getText());
        return t;
    }

    public int getMinTArrival() {
        int t = 0;
        t = Integer.parseInt(this.tMinArrTxtField.getText());
        return t;
    }

    public int getMaxTArrival() {
        int t = 0;
        t = Integer.parseInt(this.tMaxArrTxtField.getText());
        return t;
    }

    public int getMinTService() {
        int t = 0;
        t = Integer.parseInt(this.tMinSerTxtField.getText());
        return t;
    }

    public int getMaxTService() {
        int t = 0;
        t = Integer.parseInt(this.tMaxSerTxtField.getText());
        return t;
    }

    public SelectionPolicy getPolicy() {
        if (this.policy.getSelectedItem().toString().equals("Time"))
            return SelectionPolicy.SHORTEST_TIME;
        else
            return SelectionPolicy.SHORTEST_QUEUE;
    }

}
