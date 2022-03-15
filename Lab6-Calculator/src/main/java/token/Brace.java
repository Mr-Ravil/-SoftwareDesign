package token;

import visitor.TokenVisitor;

public enum Brace implements Token {
    OPEN("("), CLOSE(")");

    String sign;

    Brace(String sign) {
        this.sign = sign;
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
