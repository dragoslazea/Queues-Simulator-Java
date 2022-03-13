package view;

import model.Client;
import model.Server;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

// Clasa pentru panoul in care se va face desenarea in timp real a simularii
public class PaintPanel extends JPanel {
    //Variabile instanta (private)
    private List<Client> clients; // lista de clienti aflati in asteptare (inca nu au fost atribuiti unui server)
    private List<Server> servers; // lista de servere

    // Constructor
    public PaintPanel() {
        this.setBackground(Color.BLACK);
        this.setForeground(new Color(128, 57, 30));
        this.setFont(new Font("Arial", Font.BOLD, 12));
        this.clients = new CopyOnWriteArrayList<Client>();
        this.servers = new CopyOnWriteArrayList<Server>();
    }

    // Suprascrierea metodei paintComponent pentru desenare
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(new Color(118, 181, 197));
        super.paintComponents(g);
        this.getGraphics().setColor(new Color(118, 181, 197));
        int xOffset = 1;
        if (clients.isEmpty() == false) { // daca exista clienti in lista de asteptare
            g.drawString("Waiting Clients: ", 30, 30);
            // pentru fiecare client din lista de asteptare desenez un cerc deasupra caruia scriu informatiile legate de acesta (ID, timpSosire, timpProcesare)
            for (Client c:
                    clients) {
                g.fillOval(80 + xOffset * 70, 20, 40, 40);
                g.drawString(c.toString(), 80 + xOffset * 70, 20);
                xOffset++; // ma deplasez pentru a desena urmatorul client
            }
        }

        // daca exista servere
        if (servers.isEmpty() == false) {
            int yStep = 80;
            // pentru fiecare server desenez un patrat deasupra caruia scriu numarul serverului
            for (Server s:
                    servers) {
                g.fillRect(10, yStep, 40, 40);
                g.drawString("Server" + (yStep / 80), 10, yStep - 5);
                int xStep = 70;
                for (Client c:
                        s.getClients()) {
                    if (c.getTService() > 0) { // pentru fiecare client din lista de asteptare a serverului desenez un cerc, daca timpul sau de procesare nu s-a scurs (daca clientul inca se afla in coada)
                        g.fillOval(10 + xStep, yStep, 40, 40);
                        g.drawString(c.toString(), xStep + 10, yStep);
                        xStep += 70; // ma deplasez pentru a desena urmatorul client
                    }
                }
                yStep += 80; // ma deplasez pentru a desena urmatorul server
            }
        }
    }

    // Metoda pentru actualizarea interfetei grafice la fiecare pas al simularii
    public void doRepaint(List<Client> cl, List<Server> sr) {
        this.clients = cl;
        this.servers = sr;
        this.getGraphics().fillRect(0, 0, this.getWidth(), this.getHeight());
        this.update(this.getGraphics());
    }
}
