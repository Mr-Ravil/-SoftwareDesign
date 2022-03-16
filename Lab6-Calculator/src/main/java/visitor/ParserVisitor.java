package visitor;

import token.Brace;
import token.NumberToken;
import token.Operation;
import token.Token;

import java.util.*;

public class ParserVisitor implements TokenVisitor {
    private static final String MISSING_OPEN_BRACE_ERROR_MESSAGE = "Parse error: Missing open bracket";
    private static final String MISSING_CLOSE_BRACE_ERROR_MESSAGE = "Parse error: Missing close bracket";

    private List<Token> rpn;
    private Stack<Token> stack;

    public List<Token> parse(List<Token> tokens) {
        rpn = new ArrayList<>();
        stack = new Stack<>();

        for (Token token : tokens) {
            token.accept(this);
        }

        while (!stack.isEmpty()) {
            if (!(stack.peek() instanceof Operation)) {
                throw new IllegalStateException(MISSING_CLOSE_BRACE_ERROR_MESSAGE);
            }

            rpn.add(stack.pop());
        }

        return rpn;
    }

    @Override
    public void visit(NumberToken token) {
        rpn.add(token);
    }

    @Override
    public void visit(Brace token) {
        switch (token) {
            case OPEN -> stack.push(token);
            case CLOSE -> {
                while (!stack.isEmpty() && stack.peek() != Brace.OPEN) {
                    rpn.add(stack.pop());
                }

                if (stack.isEmpty()) {
                    throw new IllegalStateException(MISSING_OPEN_BRACE_ERROR_MESSAGE);
                }
                stack.pop();
            }
        }
    }

    @Override
    public void visit(Operation token) {
        while (!stack.isEmpty()) {
            Token top = stack.peek();
            if (top instanceof Operation
                    && ((Operation) top).getPriority() <= token.getPriority()) {
                rpn.add(stack.pop());
            } else {
                break;
            }
        }

        stack.push(token);
    }
}
