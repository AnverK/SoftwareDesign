package ru.ifmo.rain.khusainov.behavioral.visitor;

import ru.ifmo.rain.khusainov.behavioral.token.Brace;
import ru.ifmo.rain.khusainov.behavioral.token.NumberToken;
import ru.ifmo.rain.khusainov.behavioral.token.Operation;

public interface TokenVisitor {
    void visit(NumberToken token);

    void visit(Operation token);

    void visit(Brace token);
}