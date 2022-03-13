package model;

import java.util.List;

// Clasa ce implementeaza interfata Strategy conform politicii de adaugare in coada cea mai scurta
public class ConcreteStrategyQueue implements Strategy {

    // Implementarea metodei addClient din interfata Strategy conform politicii de adaugare in coada cea mai scurta
    @Override
    public Server addClient(List<Server> serversList, Client c) {
        Server minQueueServer = new Server();
        int minNbClients = Integer.MAX_VALUE;
        for (Server s:
             serversList) {
            // se cauta serverul cu cei mai putini clienti
            if (s.getClients().size() < minNbClients) {
                minQueueServer = s;
                minNbClients = s.getClients().size();
            }
        }
        minQueueServer.addClient(c); // se adauga clientul in serverul cu coada cea mai scurta
        return minQueueServer;
    }
}
