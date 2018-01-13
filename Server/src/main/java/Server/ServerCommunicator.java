package Server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import Shared.Results;

/**
 * Created by ephraimkunz on 1/12/18.
 */

public class ServerCommunicator {
    private static final int MAX_WAITING_CONN = 12;
    private HttpServer server;

    public void run() {
        InetSocketAddress address = new InetSocketAddress(8080);
        try {
            server = HttpServer.create(address, MAX_WAITING_CONN);
            server.setExecutor(null);

            server.createContext("/parseinteger", new ParseIntegerHandler());
            server.createContext("/trim", new TrimHandler());
            server.createContext("/lowercase", new LowercaseHandler());
            server.createContext("/execCommand", new ExecCommandHandler());

            server.start();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello, World!");
        new ServerCommunicator().run();
    }

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

class ParseIntegerHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        String input = ServerCommunicator.readBodyAsString(reqBody);

        Results results;

        try {
            String result = StringProcessor.getInstance().parseInteger(input);
            results = new Results(true, result, null);

        } catch (NumberFormatException e) {
            results = new Results(false, null, "NumberFormatException");
        }

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

        OutputStream respBody = exchange.getResponseBody();
        Gson gson = new Gson();
        String json = gson.toJson(results);
        ServerCommunicator.writeBodyWithString(json, respBody);
        respBody.close();
    }
}

class TrimHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        String input = ServerCommunicator.readBodyAsString(reqBody);

        String result = StringProcessor.getInstance().trim(input);
        Results results = new Results(true, result, null);

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

        OutputStream respBody = exchange.getResponseBody();
        Gson gson = new Gson();
        String json = gson.toJson(results);
        ServerCommunicator.writeBodyWithString(json, respBody);
        respBody.close();
    }
}

class LowercaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        String input = ServerCommunicator.readBodyAsString(reqBody);

        String result = StringProcessor.getInstance().toLowercase(input);
        Results results = new Results(true, result, null);

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

        OutputStream respBody = exchange.getResponseBody();
        Gson gson = new Gson();
        String json = gson.toJson(results);
        ServerCommunicator.writeBodyWithString(json, respBody);
        respBody.close();
    }
}

class ExecCommandHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

    }
}
