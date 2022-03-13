package model;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// Clasa Scheduler in care se realizeaza planificarea procesarii clientilor intr-o lista de servere
public class Scheduler {
    // Variabile instanta (private)
    private List<Server> servers; // lista de servere (cozi)
    private int maxNoServers; // numarul maxim de servere
    private int maxClientsPerServer; // numarul maxim de clienti dintr-un server
    private Strategy strategy; // staregia adoptata in ceea ce priveste politica de selectare a cozii in care se adauga clientul
    private List<Thread> threads; // lista de thread-uri lansate in executie - pentru fiecare server se lanseaza un thread
    private AtomicInteger overallWaitingTime; // timpul total de asteptare (suma timpilor de asteptare asociati fiecarei cozi)
    private AtomicInteger overallServiceTime; // timpul total de procesare a tuturor clientilor

    // Constructor
    public Scheduler(int maxNoServers, int maxClientsPerServer) {
        this.overallServiceTime = new AtomicInteger(0);
        this.overallWaitingTime = new AtomicInteger(0);
        this.maxNoServers = maxNoServers;
        this.maxClientsPerServer = maxClientsPerServer;
        this.servers = new CopyOnWriteArrayList<Server>();
        this.threads = new CopyOnWriteArrayList<Thread>();
        for (int i = 0; i < maxNoServers; i++) {
            Server s = new Server();
            servers.add(s);
            Thread t = new Thread(s); // pentru fiecare server creez un nou thread si il lansez in executie
            t.setName("Queue " + (i + 1));
            threads.add(t);
            t.start();
        }
    }

    // Setters si Getters
    public List<Server> getServers() {
        return servers;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public int getOverallServiceTime() {
        return overallServiceTime.get();
    }

    public int getOverallWaitingTime() {
        return overallWaitingTime.get();
    }

    // Metoda de selectare a strategiei urmate in fucntie de politica de selectare a clientilor
    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ConcreteStrategyTime();
        }
    }

    // Adaugarea efectiva a clientului in functie de politica de selectie adoptata
    public void dispatchClient(Client c) {
        Server s = strategy.addClient(servers, c);
        this.overallWaitingTime.addAndGet(s.getWaitingPeriod().get() - c.getTService());
        this.overallServiceTime.addAndGet(c.getTService());
    }
    // Metoda de afisare a serverelor - folosita pentru testare
    public void displayServers() {
        for (int i = 0; i < servers.size(); i++) {
            if (!servers.get(i).getClients().isEmpty()) {
                System.out.print(threads.get(i).getName() + ": ");
                for (Client c:
                        servers.get(i).getClients()) {
                    System.out.print(c + "; ");
                }
                System.out.println();
            } else {
                System.out.println(threads.get(i).getName() + ": closed");
            }
        }
    }

    // Metoda care returneaza numarul total de clienti care se afla intr-unul din servere
    public int nbClientsInQueue() {
        int nb = 0;
        for (Server s:
             this.servers) {
            nb += s.getClients().size();
        }
        return nb;
    }
}
