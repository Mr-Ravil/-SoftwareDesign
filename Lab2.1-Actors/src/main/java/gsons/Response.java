package gsons;

import java.util.List;
import java.util.Objects;

public class Response {
    public String host;
    public List<String> result;

    public Response(String host, List<String> result) {
        this.host = host;
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return Objects.equals(result, response.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result);
    }
}
