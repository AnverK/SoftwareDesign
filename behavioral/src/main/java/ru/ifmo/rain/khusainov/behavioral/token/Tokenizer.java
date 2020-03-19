package ru.ifmo.rain.khusainov.behavioral.token;

import ru.ifmo.rain.khusainov.behavioral.state.InvalidInputException;
import ru.ifmo.rain.khusainov.behavioral.state.Start;
import ru.ifmo.rain.khusainov.behavioral.state.State;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    private InputStream is;
    private List<Token> tokens;
    private State state;
    private int curPos = 0;
    private int curChar = -1;
    private boolean endOfParse = false;

    public Tokenizer(InputStream is) {
        this.is = is;
        this.state = new Start(this);
        this.tokens = new ArrayList<>();
    }

    public void nextChar() throws IOException {
        curPos++;
        curChar = is.read();
    }

    public int getChar() {
        return curChar;
    }

    public int getPos() {
        return curPos;
    }

    public void addToken(Token token) {
        tokens.add(token);
    }

    public void skipWhitespaces() throws IOException {
        while (Character.isWhitespace(curChar)) {
            nextChar();
        }
    }

    public void setEndOfParse(boolean endOfParse) {
        this.endOfParse = endOfParse;
    }

    public void changeState(State newState) {
        this.state = newState;
    }

    public List<Token> tokenize() throws IOException, InvalidInputException {
        nextChar();
        while (!endOfParse) {
            state.handle();
        }
        return tokens;
    }

}