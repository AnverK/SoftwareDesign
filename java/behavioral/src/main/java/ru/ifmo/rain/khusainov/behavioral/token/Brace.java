package ru.ifmo.rain.khusainov.behavioral.token;

import ru.ifmo.rain.khusainov.behavioral.visitor.TokenVisitor;

public class Brace implements Token {

    private BraceType braceType;

    public Brace(BraceType parenthesisType) {
        this.braceType = parenthesisType;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    public BraceType getBraceType() {
        return braceType;
    }
}
