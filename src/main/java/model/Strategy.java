package model;

import java.util.List;

// Interfata pentru adaugarea a clientului la un server
public interface Strategy {
    public Server addClient(List<Server> serversList, Client c);
}
