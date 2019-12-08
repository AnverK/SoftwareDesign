package ru.ifmo.rain.khusainov.behavioral.visitor;

public class WrongParenthesisConsequenceException extends RuntimeException {
    public WrongParenthesisConsequenceException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Invalid consequence of braces in the expression";
    }
}
