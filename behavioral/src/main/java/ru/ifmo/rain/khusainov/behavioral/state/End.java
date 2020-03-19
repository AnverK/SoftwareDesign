package ru.ifmo.rain.khusainov.behavioral.state;

import ru.ifmo.rain.khusainov.behavioral.token.Tokenizer;

public class End implements State {

    private Tokenizer tokenizer;

    public End(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public void handle() {
        tokenizer.setEndOfParse(true);
    }
}
