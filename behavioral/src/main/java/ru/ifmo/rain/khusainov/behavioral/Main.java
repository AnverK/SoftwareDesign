package ru.ifmo.rain.khusainov.behavioral;

import ru.ifmo.rain.khusainov.behavioral.token.Token;
import ru.ifmo.rain.khusainov.behavioral.token.Tokenizer;
import ru.ifmo.rain.khusainov.behavioral.visitor.CalcVisitor;
import ru.ifmo.rain.khusainov.behavioral.visitor.ParserVisitor;
import ru.ifmo.rain.khusainov.behavioral.visitor.PrintVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            InputStream is = System.in;
            Tokenizer tokenizer = new Tokenizer(is);
            List<Token> tokens = tokenizer.tokenize();
            if (tokens.isEmpty()) {
                System.out.println("Expression is not defined for an empty input");
                return;
            }
            ParserVisitor parserVisitor = new ParserVisitor();
            PrintVisitor printVisitor = new PrintVisitor();
            CalcVisitor calcVisitor = new CalcVisitor();
            for (Token token : tokens) {
                token.accept(parserVisitor);
            }
            parserVisitor.popRest();

            for (Token token : parserVisitor.getParsedTokens()) {
                token.accept(printVisitor);
            }
            for (Token token : parserVisitor.getParsedTokens()) {
                token.accept(calcVisitor);
            }
            System.out.println();
            System.out.println(calcVisitor.calculate());
        } catch (IOException ioe) {
            System.out.println("Exception while reading input " + ioe);
        }
    }
}
