package controller;

// Clasa pentru Exceptie in cazul in care input-ul introdus de utilizator este invalid
public class InvalidInputException extends Exception {
    // Constructor
    public InvalidInputException(String msg){
        super(msg);
    }
}
