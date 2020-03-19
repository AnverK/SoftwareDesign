package ru.ifmo.rain.khusainov.behavioral.state;

import ru.ifmo.rain.khusainov.behavioral.token.Tokenizer;

public class Error implements State {
    private Tokenizer tokenizer;

    public Error(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public void handle() throws InvalidInputException {
        throw new InvalidInputException(tokenizer.getPos(), tokenizer.getChar());
    }
}
