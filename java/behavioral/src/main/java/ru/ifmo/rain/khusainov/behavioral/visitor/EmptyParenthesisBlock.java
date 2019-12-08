package ru.ifmo.rain.khusainov.behavioral.visitor;

public class EmptyParenthesisBlock extends RuntimeException {
    public EmptyParenthesisBlock() {
        super();
    }

    @Override
    public String getMessage() {
        return "Parenthesis block should not be empty";
    }
}
