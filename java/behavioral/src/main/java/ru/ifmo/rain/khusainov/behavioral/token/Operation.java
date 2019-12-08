package ru.ifmo.rain.khusainov.behavioral.token;

import ru.ifmo.rain.khusainov.behavioral.visitor.TokenVisitor;

public class Operation implements Token {

    private OperationType operationType;
    private int priority;

    public Operation(OperationType operationType) {
        this.operationType = operationType;
        if (operationType == OperationType.PLUS || operationType == OperationType.MINUS) {
            priority = 0;
        } else {
            priority = 1;
        }
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return operationType.toString();
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }
}
