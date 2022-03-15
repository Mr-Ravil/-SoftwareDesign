package tokenizer;

import token.Brace;
import token.NumberToken;
import token.Operation;
import token.Token;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final int EOF = -1;

    private final InputStream inputStream;
    private final List<Token> tokens;
    private int position;
    private int currentChar;

    public Tokenizer(InputStream inputStream) {
        this.inputStream = inputStream;
        this.tokens = new ArrayList<>();
        this.position = 0;
    }

    private void nextChar() throws ParseException {
        ++position;
        try {
            currentChar = inputStream.read();
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), position);
        }
    }

    private void skipWhitespace() throws ParseException {
        while (Character.isWhitespace(currentChar)) nextChar();
    }

    private void addToken(Token token) throws ParseException {
        tokens.add(token);
        nextChar();
    }

    private void addNumber() throws ParseException {
        StringBuilder number = new StringBuilder();
        while (Character.isDigit(currentChar)) {
            number.append((char) currentChar);
            nextChar();
        }
        tokens.add(new NumberToken(Long.parseLong(number.toString())));
    }

    public List<Token> tokenize() throws ParseException {
        nextChar();
        skipWhitespace();
        while (currentChar != EOF) {
            switch (currentChar) {
                case '+' -> addToken(Operation.ADD);
                case '-' -> addToken(Operation.SUB);
                case '*' -> addToken(Operation.MUL);
                case '/' -> addToken(Operation.DIV);
                case '(' -> addToken(Brace.OPEN);
                case ')' -> addToken(Brace.CLOSE);
                default -> {
                    if (Character.isDigit(currentChar)) {
                        addNumber();
                    } else {
                        throw new IllegalStateException("Unexpected value: " + currentChar);
                    }
                }
            }
            skipWhitespace();
        }

        return tokens;
    }

}
