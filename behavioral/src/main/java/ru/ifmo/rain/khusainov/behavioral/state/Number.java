package ru.ifmo.rain.khusainov.behavioral.state;

import ru.ifmo.rain.khusainov.behavioral.token.NumberToken;
import ru.ifmo.rain.khusainov.behavioral.token.Tokenizer;

import java.io.IOException;

public class Number implements State {
    private Tokenizer tokenizer;

    public Number(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public void handle() throws IOException {
        int number = 0;
        while (Character.isDigit(tokenizer.getChar())) {
            int digit = tokenizer.getChar() - '0';
            number *= 10;
            number += digit;
            tokenizer.nextChar();
        }
        tokenizer.addToken(new NumberToken(number));
        tokenizer.changeState(new Start(tokenizer));
    }
}
