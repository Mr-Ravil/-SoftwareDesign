package visitor;

import token.Brace;
import token.NumberToken;
import token.Operation;
import token.Token;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PrintVisitor implements TokenVisitor {
    private final OutputStream outputStream;
    private StringBuilder expression;

    public PrintVisitor(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    public void print(List<Token> tokens) throws IOException {
        expression = new StringBuilder();
        for (Token token : tokens) {
            token.accept(this);
            expression.append(" ");
        }

        outputStream.write(expression.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void visit(NumberToken token) {
        expression.append(token.toString());
    }

    @Override
    public void visit(Brace token) {
        expression.append(token.toString());
    }

    @Override
    public void visit(Operation token) {
        expression.append(token.toString());
    }
}
