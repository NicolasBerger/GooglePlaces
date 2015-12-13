

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import se.walkercrou.places.DefaultRequestHandler;

public class MyRequestHandler extends DefaultRequestHandler {
    private final HttpClient client = HttpClients.createMinimal();
    @Override
    public String get(String uri) throws IOException {
        try {
            HttpGet get = new HttpGet(uri);
            HttpResponse response = client.execute(get);
            return readString(response);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
    private String readString(HttpResponse response) throws IOException {
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return str.trim();
    }
}
