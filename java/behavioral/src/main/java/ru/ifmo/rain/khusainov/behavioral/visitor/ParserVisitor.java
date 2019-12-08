package ru.ifmo.rain.khusainov.behavioral.visitor;

import ru.ifmo.rain.khusainov.behavioral.token.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ParserVisitor implements TokenVisitor {
    private Stack<Token> stack;
    private List<Token> parsedTokens;

    public ParserVisitor() {
        stack = new Stack<>();
        parsedTokens = new ArrayList<>();
    }

    public void popRest() throws WrongParenthesisConsequenceException {
        while (stack.size() != 0) {
            Token token = stack.pop();
            if (token instanceof Operation) {
                parsedTokens.add(token);
            } else {
                throw new WrongParenthesisConsequenceException();
            }
        }
    }

    public List<Token> getParsedTokens() {
        return parsedTokens;
    }

    @Override
    public void visit(NumberToken token) {
        parsedTokens.add(token);
    }

    @Override
    public void visit(Brace token) throws WrongParenthesisConsequenceException {
        if (token.getBraceType() == BraceType.OPEN) {
            stack.push(token);
        } else {
            boolean nonemptySeq = false;
            while (stack.size() != 0) {
                Token curToken = stack.pop();
                if (curToken instanceof Brace && ((Brace) curToken).getBraceType() == BraceType.OPEN) {
                    if(!nonemptySeq) {
                        throw new EmptyParenthesisBlock();
                    }
                    return;
                } else {
                    nonemptySeq =  true;
                    parsedTokens.add(curToken);
                }
            }
            throw new WrongParenthesisConsequenceException();
        }
    }

    @Override
    public void visit(Operation token) {
        int curPriority = token.getPriority();
        while (stack.size() != 0) {
            Token prevToken = stack.peek();
            if (prevToken instanceof Operation) {
                int prevPriority = ((Operation) prevToken).getPriority();
                if (prevPriority >= curPriority) {
                    parsedTokens.add(stack.pop());
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        stack.push(token);
    }
}
