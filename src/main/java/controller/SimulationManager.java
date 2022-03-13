package controller;

import model.Client;
import model.Scheduler;
import model.SelectionPolicy;
import model.Server;
import view.GUI;
import view.RealTimeView;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// Clasa in care se face simularea distribuirii clientilor in cozi
public class SimulationManager implements Runnable {
    // Variabile instanta (private)
    private GUI graphicalUserInterface; // interfata grafica in care utilizatorul introduce datele necesare simularii
    private RealTimeView realTimeView; // interfata grafica ce surprinde simularea in timp real
    private int timeLimit; // timpul de simulare
    private int maxProcessingTime; // timpul maxim de procesare a unui client
    private int minProcessingTime; // timpul minim de procesare a unui client
    private int maxArrivalTime; // timpul minim de sosire
    private int minArrivalTime; // timpul maxim de sosire
    private int numberOfServers; // numarul de servere
    private int numberOfClients; // numarul de clienti
    private int maxClientsInQueues; // numarul maxim de clienti din cozi
    private SelectionPolicy selectionPolicy; // politica de selectare
    AtomicInteger currentTime; // timpul curent
    private double avgWaitingTime; // timpul mediu de asteptare
    private double avgServiceTime; // timpul mediu de procesare pentru un client
    private int peakHour; // ora de varf - timpul curent cand in cozi se afla numarul maxim de clienti
    private FileWriter logFile; // fisierul ce va contine rezultatele simularii si log-ul evenimentelor
    private BufferedWriter buf; // buffer pentru scriere

    private Scheduler scheduler; // planificator
    private List<Client> generatedClients; // lista clientilor generati

    // Constructor
    public SimulationManager(GUI gui) {
        this.graphicalUserInterface = gui;
        this.realTimeView = gui.getRealTimeView();
        currentTime = new AtomicInteger(0);
        try {
            setInputs();
        } catch (InvalidInputException e) {
            this.graphicalUserInterface.showError(e.getMessage());
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (NumberFormatException f) {
            this.graphicalUserInterface.showError(f.getMessage());
            System.out.println(f.getMessage());
            System.exit(2);
        }
        scheduler = new Scheduler(numberOfServers, numberOfClients);
        scheduler.changeStrategy(selectionPolicy);
        maxClientsInQueues = 0;
        avgServiceTime = 0;
        avgWaitingTime = 0;
        peakHour = 0;
        try {
            this.logFile = createLogFile();
        } catch (IOException e) {
            System.out.println("Couldn't create log file: " + e.getMessage());
            System.exit(1);
        }
        this.buf = new BufferedWriter(logFile);
        generateRandomClients();
    }

    // Getters
    public Scheduler getScheduler() {
        return scheduler;
    }

    public List<Client> getGeneratedClients() {
        return generatedClients;
    }

    // Metoda ce preia informatiile din interfata grafica si le seteaza in vederea pregatirii simularii
    public void setInputs() throws InvalidInputException, NumberFormatException {
        numberOfClients = graphicalUserInterface.getN();
        numberOfServers = graphicalUserInterface.getQ();
        timeLimit = graphicalUserInterface.getSimulationTime();
        minArrivalTime = graphicalUserInterface.getMinTArrival();
        maxArrivalTime = graphicalUserInterface.getMaxTArrival();
        minProcessingTime = graphicalUserInterface.getMinTService();
        maxProcessingTime = graphicalUserInterface.getMaxTService();
        selectionPolicy = graphicalUserInterface.getPolicy();
        // se verifica validitatea datelor introduse si se arunca exceptii corespunzatoare situatiilor exceptionale
        if (numberOfClients < 0 || numberOfServers < 0 || timeLimit < 0 || minArrivalTime < 0 || maxArrivalTime < 0 || minProcessingTime <0
        || maxProcessingTime < 0) {
            throw new InvalidInputException("Inputs must be positive integers!");
        }
        if (minArrivalTime > maxArrivalTime) {
            throw new InvalidInputException("Min Arrival Time must be less than Max Arrival Time");
        }
        if (minProcessingTime > maxProcessingTime) {
            throw new InvalidInputException("Min Service Time must be less than Max Service Time");
        }
    }

    // Metoda pentru generarea aleatoare a listei de N clienti
    public void generateRandomClients() {
        int serviceTimeRange = maxProcessingTime - minProcessingTime + 1;
        int arrivalTimeRange = maxArrivalTime - minArrivalTime + 1;
        generatedClients = new CopyOnWriteArrayList<>(); // lista de clienti este de tip CopyOnWriteArrayList pentru a asigura thread safety
        for (int i = 0; i < numberOfClients; i++) {
            int aTime = (int)(Math.random() * arrivalTimeRange) + minArrivalTime; // un numar natural din intervalul [minArrivalTime, maxArrivalTime]
            int sTime = (int)(Math.random() * serviceTimeRange) + minProcessingTime; // un numar natural din intervalul [minProcessingTime, maxProcessingTime]
            Client c = new Client(i + 1, aTime, sTime);
            generatedClients.add(c);
        }
        Collections.sort(generatedClients); // sortarea clientilor in ordinea crescatoare a timpului de sosire
    }

    // Metoda pentru crearea fisierului in care se vor scrie rezultatele
    public FileWriter createLogFile() throws IOException {
        FileWriter logF = new FileWriter("Log-of-events.txt");
        return logF;
    }

    // Metoda pentru scrierea in fisier
    public void writeInLogFile() {
        String result = "Time: " + currentTime + "\nWaiting clients: ";
        if (generatedClients.isEmpty()) {
            result += "-\n";
        } else {
            for (Client cl: generatedClients) {
                result = result + cl + "; ";
            }
            result += "\n";
        }
        for (int i = 0; i < scheduler.getServers().size(); i++) {
            if (!scheduler.getServers().get(i).getClients().isEmpty()) {
                result = result + scheduler.getThreads().get(i).getName() + ": ";
                for (Client c : scheduler.getServers().get(i).getClients()) {
                    result = result + c + "; ";
                }
                result += "\n";
            } else { result = result + scheduler.getThreads().get(i).getName() + ": closed\n"; }
        }
        try { buf.write(result); }
        catch (IOException e){
            System.out.println("Couldn't write in the log file: " + e.getMessage());
            System.exit(1);
        }
        result = "";
        try { buf.write("-----------------------------------------------------------------------------\n"); }
        catch (IOException e){
            System.out.println("Couldn't write in the log file: " + e.getMessage());
            System.exit(1);
        }
    }

    // Metoda pentru inchiderea bufferului de scriere
    public void bufferClose(){
        try {
            this.buf.close();
        } catch (IOException e) {
            System.out.println("Couldn't close buffer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Suprascrierea metodei run din interfata Runnable - aici are loc simularea efectiva
    @Override
    public void run() {
        currentTime = new AtomicInteger(0); // initializez timpul curent
        while (currentTime.get() < timeLimit) {
            for (Client c: generatedClients) { // iau fiecare client din lista de asteptare, iar cand timpul sau de sosire coincide cu timpul curent al simularii, il adaug intr-o coada, conform politicii de slectare
                if (c.getTArrival() == currentTime.get()) {
                    scheduler.dispatchClient(c);
                    if (scheduler.nbClientsInQueue() > maxClientsInQueues) {
                        peakHour = currentTime.get(); // ora de varf este momentul cand cei mai multi clienti stau la coada
                        maxClientsInQueues = scheduler.nbClientsInQueue();
                    }
                    generatedClients.remove(c);
                }
            }
            this.realTimeView.getPaintPanel().doRepaint(this.getGeneratedClients(), this.getScheduler().getServers()); // actualizez interfata grafica
            writeInLogFile(); // scriu informatiile in fisier
            currentTime.getAndIncrement(); // incrementez timpul curent
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                interruptAllThreads();
                computeAverageWaitingTime(); // determin si scriu in fisier timpul mediu de asteptare
                computeAverageServiceTimeAndPeakHour(); // determin si scriu in fisier timpul mediu de procesare si ora de varf
                bufferClose();
                return;
            }
        }
        interruptAllThreads();
        computeAverageWaitingTime(); // determin si scriu in fisier timpul mediu de asteptare
        computeAverageServiceTimeAndPeakHour(); // determin si scriu in fisier timpul mediu de procesare si ora de varf
        bufferClose();
    }

    // Metoda pentru oprirea thread-urilor
    public void interruptAllThreads() {
        for (Thread t:
                scheduler.getThreads()) {
            t.interrupt();
        }
    }

    // Metoda pentru calcularea si scrierea in fisier a timpului mediu de asteptare
    public void computeAverageWaitingTime() {
        int waitingTime = scheduler.getOverallWaitingTime();
        this.avgWaitingTime = (double) waitingTime / numberOfClients;
        this.avgWaitingTime = Math.round(avgWaitingTime * 100) / 100.0; // rotunjire cu 2 zecimale exacte
        try {
            this.buf.write("END OF SIMULATION\nAverage Waiting Time: " + this.avgWaitingTime);
        } catch (IOException e) {
            System.out.println("Couldn't write in the log file: " + e.getMessage());
        }
    }

    // Metoda pentru calcularea si scrierea in fisier a timpului mediu de procesare si a orei de varf
    public void computeAverageServiceTimeAndPeakHour() {
        int serviceTime = scheduler.getOverallServiceTime();
        // pentru timup mediu de procesare iau in considerare doar clientii care au fost deja procesati sau cei in curs de procesare
        for (Server s:
             this.getScheduler().getServers()) {
            for (Client c:
                 s.getClients()) {
                serviceTime -= c.getTService(); // pentru clientii care inca se afla in coada, scad timpul de procesare necesar ramas la finalul simularii
            }
        }
        this.avgServiceTime = (double) serviceTime / (numberOfClients - scheduler.nbClientsInQueue() - this.generatedClients.size()); // consider doar clientii deja procesati
        this.avgServiceTime = Math.round(avgServiceTime * 100) / 100.0; // rotunjire cu 2 zecimale exacte
        try {
            this.buf.write("\nAverage Service Time: " + this.avgServiceTime + "\nPeak Hour: " + this.peakHour);
        } catch (IOException e) {
            System.out.println("Couldn't write in the log file: " + e.getMessage());
        }
    }
}
