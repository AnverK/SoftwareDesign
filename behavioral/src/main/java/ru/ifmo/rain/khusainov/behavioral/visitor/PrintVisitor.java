package ru.ifmo.rain.khusainov.behavioral.visitor;

import ru.ifmo.rain.khusainov.behavioral.token.Brace;
import ru.ifmo.rain.khusainov.behavioral.token.NumberToken;
import ru.ifmo.rain.khusainov.behavioral.token.Operation;

public class PrintVisitor implements TokenVisitor {

    @Override
    public void visit(NumberToken token) {
        System.out.print(token + " ");
    }

    @Override
    public void visit(Brace token) {
        System.out.print(token + " ");
    }

    @Override
    public void visit(Operation token) {
        System.out.print(token + " ");
    }
}
