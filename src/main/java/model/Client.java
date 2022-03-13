package model;

// Clasa pentru client
public class Client implements Comparable<Client> {
    // Variabile instanta (private)
    private int id; // ID-ul clientului
    private int tArrival; // timpul de sosire
    private int tService; // timpul necesar procesarii clientului

    // Constructor
    public Client(int id, int tArrival, int tService) {
        this.id = id;
        this.tArrival = tArrival;
        this.tService = tService;
    }

    // Setters si Getters
    public int getTArrival() {
        return tArrival;
    }

    public int getTService() {
        return tService;
    }

    public void setTService(int tService) {
        this.tService = tService;
    }

    // Suprascrierea metodei toString, necesara afisarii informatiilor despre client
    @Override
    public String toString() {
        return "(" + id + ", " + tArrival + ", " + tService + ")";
    }

    // Suprascrierea metodei compareTo din interfata Comparable, necesara sortarii clientilor in functie de timpul de sosire
    @Override
    public int compareTo(Client o) {
        if (this.tArrival < o.tArrival)
            return -1;
        else if(this.tArrival > o.tArrival)
            return 1;
        else return 0;
    }
}
