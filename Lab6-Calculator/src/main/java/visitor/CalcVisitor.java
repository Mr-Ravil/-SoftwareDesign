package visitor;

import token.Brace;
import token.NumberToken;
import token.Operation;
import token.Token;

import java.util.List;
import java.util.Stack;

public class CalcVisitor implements TokenVisitor {
    private final String ODD_TOKEN_ERROR_MESSAGE = "Reverse Polish notation does not support this token: ";
    private final String CALCULATION_ERROR_MESSAGE = "Calculation error";

    private Stack<Long> stack;

    public long evaluate(List<Token> tokens) {
        stack = new Stack<>();

        for (Token token : tokens) {
            token.accept(this);
        }

        if (stack.size() != 1) {
            throw new IllegalStateException(CALCULATION_ERROR_MESSAGE);
        }

        return stack.peek();
    }

    @Override
    public void visit(NumberToken token) {
        stack.push(token.getValue());
    }

    @Override
    public void visit(Brace token) {
        throw new IllegalStateException(ODD_TOKEN_ERROR_MESSAGE + token.toString());
    }

    @Override
    public void visit(Operation token) {
        if (stack.size() < 2) {
            throw new IllegalStateException(CALCULATION_ERROR_MESSAGE);
        }

        Long b = stack.pop();
        Long a = stack.pop();

        stack.push(
                switch (token) {
                    case ADD -> a + b;
                    case SUB -> a - b;
                    case MUL -> a * b;
                    case DIV -> a / b;
                }
        );
    }
}
