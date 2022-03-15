package token;

import visitor.TokenVisitor;

public class NumberToken implements Token {
    private final Long value;

    public NumberToken(long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}
