package ru.ifmo.rain.khusainov.behavioral.token;

import ru.ifmo.rain.khusainov.behavioral.visitor.TokenVisitor;

public class NumberToken implements Token {

    private int value;

    public NumberToken(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "NUMBER(" + value + ")";
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }
}
