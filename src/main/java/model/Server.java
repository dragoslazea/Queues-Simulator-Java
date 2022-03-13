package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

// Clasa Server (corespunzatoare cozii)
public class Server implements Runnable {
    // Variabile instanta (private)
    private BlockingQueue<Client> clients; // lista de clienti din coada - declarata BlockingQueue pentru a asigura threads safety
    private AtomicInteger waitingPeriod; // perioada de asteptare asociata cozii - declarata AtomicInteger pentru a asigura threads safety

    // Constructor
    public Server() {
        clients = new LinkedBlockingDeque<Client>();
        waitingPeriod = new AtomicInteger(0);
    }

    // Setters si Getters
    public BlockingQueue<Client> getClients() {
        return clients;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setClients(BlockingQueue<Client> clients) {
        this.clients = clients;
    }

    // Metoda de adaugare a unui client in lista de clienti asociata cozii
    public void addClient(Client c) {
        clients.add(c);
        waitingPeriod.addAndGet(c.getTService());
    }

    // Suprascrierea metodei run din interfata Runnable
    @Override
    public void run() {
        while (true) {
            if (!clients.isEmpty()) { // cat timp inca mai exista clienti in coada
                Client c = clients.peek(); // extrag primul client din coada de asteptare
                if (c != null) {
                    try {
                        while(c.getTService() != 0) {
                            c.setTService(c.getTService() - 1); // decrementez timpul de procesare
                            waitingPeriod.getAndDecrement(); // decrementez timpul total de asteptare al cozii
                            if (c.getTService() == 0) {
                                clients.remove(c); // daca timpul de procesare este 0, sterg clientul din coada
                            }
                            Thread.sleep(1000); // intrerup firul de executie pentru o secunda
                        }
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
            else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    // Suprascrierea metodei toString, necesara pentru afisarea clientilor asociati fiecarui server
    @Override
    public String toString(){
        String serverString = Thread.currentThread().getName() + ": ";
        if (clients.isEmpty()) {
            serverString = serverString + "closed";
        } else {
            for (Client c:
                 clients) {
                serverString = serverString + c + " ";
            }
        }
        return serverString;
    }
}
