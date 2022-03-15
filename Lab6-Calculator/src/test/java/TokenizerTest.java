import org.junit.Assert;
import org.junit.Test;
import token.Brace;
import token.NumberToken;
import token.Operation;
import token.Token;
import tokenizer.Tokenizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

public class TokenizerTest {
    private final String projectPath = new File("").getCanonicalPath();
    private final String resourcesPackagePath = projectPath + "\\src\\test\\resources\\";


    public TokenizerTest() throws IOException {
    }

    @Test
    public void simpleTest() throws ParseException, IOException {
        String fileName = "expression1.txt";
        Tokenizer tokenizer = new Tokenizer(Files.newInputStream(Paths.get(resourcesPackagePath + fileName)));

        List<Token> tokens = tokenizer.tokenize();
        Assert.assertEquals(List.of(
                Brace.OPEN, new NumberToken(30), Operation.ADD, new NumberToken(2), Brace.CLOSE,
                Operation.DIV, new NumberToken(8)
        ).toString(), tokens.toString());
    }

}
