import org.junit.Assert;
import org.junit.Test;
import token.Brace;
import token.NumberToken;
import token.Operation;
import token.Token;
import visitor.ParserVisitor;

import java.util.List;

public class ParserVisitorTest {
    private final ParserVisitor parserVisitor;

    public ParserVisitorTest() {
        this.parserVisitor = new ParserVisitor();
    }

    @Test
    public void simpleTest() {
        List<Token> tokens = parserVisitor.parse(List.of(
                Brace.OPEN, new NumberToken(30), Operation.ADD, new NumberToken(2), Brace.CLOSE,
                Operation.DIV, new NumberToken(8)));

        Assert.assertEquals(List.of(
                new NumberToken(30), new NumberToken(2), Operation.ADD,
                new NumberToken(8), Operation.DIV
        ).toString(), tokens.toString());

    }

}
