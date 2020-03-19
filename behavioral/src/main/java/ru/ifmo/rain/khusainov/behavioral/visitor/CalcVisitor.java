package ru.ifmo.rain.khusainov.behavioral.visitor;

import ru.ifmo.rain.khusainov.behavioral.token.Brace;
import ru.ifmo.rain.khusainov.behavioral.token.NumberToken;
import ru.ifmo.rain.khusainov.behavioral.token.Operation;

import java.util.Stack;

public class CalcVisitor implements TokenVisitor {
    private Stack<Integer> cachedNumbers = new Stack<>();

    public int calculate() {
        return cachedNumbers.peek();
    }

    @Override
    public void visit(NumberToken token) {
        cachedNumbers.push(token.getValue());
    }

    @Override
    public void visit(Brace token) {

    }

    @Override
    public void visit(Operation token) {
        assert cachedNumbers.size() >= 2;
        int num2 = cachedNumbers.pop();
        int num1 = cachedNumbers.pop();
        switch (token.getOperationType()) {
            case PLUS:
                cachedNumbers.push(num1 + num2);
                return;
            case MINUS:
                cachedNumbers.push(num1 - num2);
                return;
            case MUL:
                cachedNumbers.push(num1 * num2);
                return;
            case DIV:
                cachedNumbers.push(num1 / num2);
        }
    }
}
