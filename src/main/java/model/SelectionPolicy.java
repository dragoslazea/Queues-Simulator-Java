package model;

// Enumerare ce contine tipurile de strategii ce pot fi alese pentru politica de procesare
// SHORTEST_QUEUE - clientul este adaugat la coada cea mai scurta
// SHORTEST_TIME - clientul este adaugat la coada unde are cel mai putin de asteptat
public enum SelectionPolicy {
    SHORTEST_QUEUE, SHORTEST_TIME
}
