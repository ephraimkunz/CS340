package Client;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Shared.Results;
import Shared.Serializer;

/**
 * Created by ephraimkunz on 1/12/18.
 */

public class ClientCommunicator {
    private static ClientCommunicator instance = new ClientCommunicator();

    private ClientCommunicator(){}

    public static ClientCommunicator getInstance() {
        return instance;
    }

    public Results send(String urlPath, String input) {
        try {
            URL url = new URL(urlPath);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            byte[] outputInBytes = input.getBytes("UTF-8");
            OutputStream os = http.getOutputStream();
            os.write( outputInBytes );
            os.close();

            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return Serializer.read(http.getInputStream());
            }
            else {
                return new Results(false, null, "Bad status code in ClientCommunicator");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new Results(false, null, "IOException in ClientCommunicator");
        }
    }
}
