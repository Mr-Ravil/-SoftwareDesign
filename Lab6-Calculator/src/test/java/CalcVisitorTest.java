import org.junit.Assert;
import org.junit.Test;
import token.Brace;
import token.NumberToken;
import token.Operation;
import visitor.CalcVisitor;

import java.util.List;

public class CalcVisitorTest {
    private final CalcVisitor calcVisitor;

    public CalcVisitorTest() {
        this.calcVisitor = new CalcVisitor();
    }

    @Test
    public void simpleTest() {
        long answer = calcVisitor.evaluate(List.of(
                new NumberToken(30), new NumberToken(2), Operation.ADD,
                new NumberToken(8), Operation.DIV));

        Assert.assertEquals(4L, answer);

    }

}
