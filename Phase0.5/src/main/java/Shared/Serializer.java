package Shared;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by ephraimkunz on 1/12/18.
 */

public class Serializer {
    // Write a Results object to the OutputStream
    public static void write(Results r, OutputStream os) throws IOException {
        Gson gson = new Gson();

        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(gson.toJson(r));
        sw.flush();
    }

    // Read a Results object from the InputStream
    public static Results read(InputStream is) throws IOException {
        String json = Serializer.readBodyAsString(is);
        Gson gson = new Gson();
        return gson.fromJson(json, Results.class);
    }

    // Read the body as a string
    public static String readBodyAsString(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);


        int b;
        StringBuilder buf = new StringBuilder(512);
        try {
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }
            br.close();
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buf.toString();
    }
}
