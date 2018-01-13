package Shared;

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
    // Write a string to the body
    public static void writeBodyWithString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
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
