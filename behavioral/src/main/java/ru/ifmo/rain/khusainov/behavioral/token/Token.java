package ru.ifmo.rain.khusainov.behavioral.token;

import ru.ifmo.rain.khusainov.behavioral.visitor.TokenVisitor;

public interface Token {
    void accept(TokenVisitor visitor);
}
