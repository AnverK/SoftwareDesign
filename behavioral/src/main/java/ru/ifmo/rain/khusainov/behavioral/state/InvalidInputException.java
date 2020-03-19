package ru.ifmo.rain.khusainov.behavioral.state;

public class InvalidInputException extends Exception {
    private int errorPos;
    private int actualSymbol;

    public InvalidInputException(int errorPos, int actualSymbol) {
        super();
        this.errorPos = errorPos;
        this.actualSymbol = actualSymbol;
    }

    @Override
    public String getMessage() {
        return "Parse error on position: " + errorPos + " (symbol: " + (char) actualSymbol + ")";
    }
}
