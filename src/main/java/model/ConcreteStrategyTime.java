package model;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// Clasa ce implementeaza interfata Strategy conform politicii de adaugare in coada cu cel mai scurt timp de asteptare
public class ConcreteStrategyTime implements Strategy {

    // Implementarea metodei addClient din interfata Strategy conform politicii de adaugare in coada cu cel mai scurt timp de asteptare
    @Override
    public Server addClient(List<Server> serversList, Client c) {
        Server minTimeServer = new Server();
        AtomicInteger minWaitingTime = new AtomicInteger(Integer.MAX_VALUE);
        for (Server s:
             serversList) {
            // se cauta serverul cu cel mai mic timp de asteptare
            if (Integer.compare(minWaitingTime.get(), s.getWaitingPeriod().get()) > 0) {
                minTimeServer = s;
                minWaitingTime.set(s.getWaitingPeriod().get());
            }
        }
        minTimeServer.addClient(c); // se adauga clientul in serverul cu cel mai scurt timp de asteptare
        return minTimeServer;
    }

}
