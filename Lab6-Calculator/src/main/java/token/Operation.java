package token;

import visitor.TokenVisitor;

public enum Operation implements Token {
    ADD("+", 2), SUB("-", 2), MUL("*", 1), DIV("/", 1);

    private final String sign;
    private final int priority;

    Operation(String sign, int priority) {
        this.sign = sign;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return sign;
    }
}
