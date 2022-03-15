import org.junit.Assert;
import org.junit.Test;
import token.NumberToken;
import token.Operation;
import visitor.PrintVisitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PrintVisitorTest {
    private final String projectPath = new File("").getCanonicalPath();
    private final String resourcesPackagePath = projectPath + "\\src\\test\\resources\\";

    public PrintVisitorTest() throws IOException {
    }

    @Test
    public void simpleTest() throws IOException {
        String fileName = "expression1_in_rpn.txt";
        Path filePath = Paths.get(resourcesPackagePath + fileName);
        OutputStream outputStream = Files.newOutputStream(filePath);

        PrintVisitor tokenizer = new PrintVisitor(outputStream);

        tokenizer.print(List.of(
                new NumberToken(30), new NumberToken(2), Operation.ADD,
                new NumberToken(8), Operation.DIV));

        outputStream.close();

        BufferedReader bufferedReader = Files.newBufferedReader(filePath);
        String result = bufferedReader.readLine().trim();
        bufferedReader.close();

        Assert.assertEquals("30 2 + 8 /", result);
    }

}

