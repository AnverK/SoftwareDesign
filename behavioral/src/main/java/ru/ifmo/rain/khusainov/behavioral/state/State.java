package ru.ifmo.rain.khusainov.behavioral.state;

import java.io.IOException;

public interface State {
    void handle() throws IOException, InvalidInputException;
}
