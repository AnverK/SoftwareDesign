package ru.ifmo.rain.khusainov.behavioral.state;

import ru.ifmo.rain.khusainov.behavioral.token.*;

import java.io.IOException;

public class Start implements State {
    private Tokenizer tokenizer;

    public Start(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public void handle() throws IOException {
        tokenizer.skipWhitespaces();
        if (tokenizer.getChar() == -1) {
            tokenizer.changeState(new End(tokenizer));
            return;
        }
        switch (tokenizer.getChar()) {
            case '(':
                tokenizer.nextChar();
                tokenizer.addToken(new Brace(BraceType.OPEN));
                break;
            case ')':
                tokenizer.nextChar();
                tokenizer.addToken(new Brace(BraceType.CLOSE));
                break;
            case '+':
                tokenizer.nextChar();
                tokenizer.addToken(new Operation(OperationType.PLUS));
                break;
            case '-':
                tokenizer.nextChar();
                tokenizer.addToken(new Operation(OperationType.MINUS));
                break;
            case '*':
                tokenizer.nextChar();
                tokenizer.addToken(new Operation(OperationType.MUL));
                break;
            case '/':
                tokenizer.nextChar();
                tokenizer.addToken(new Operation(OperationType.DIV));
                break;
            default:
                if (Character.isDigit(tokenizer.getChar())) {
                    tokenizer.changeState(new Number(tokenizer));
                } else {
                    tokenizer.changeState(new Error(tokenizer));
                }
        }
    }
}
