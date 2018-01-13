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
import Shared.Serializer;

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
}

class ParseIntegerHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        String input = Serializer.readBodyAsString(reqBody);

        Results results;

        try {
            String result = StringProcessor.getInstance().parseInteger(input);
            results = new Results(true, result, null);

        } catch (NumberFormatException e) {
            results = new Results(false, null, Results.NumberFormatException);
        }

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

        OutputStream respBody = exchange.getResponseBody();
        Gson gson = new Gson();
        String json = gson.toJson(results);
        Serializer.writeBodyWithString(json, respBody);
        respBody.close();
    }
}

class TrimHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        String input = Serializer.readBodyAsString(reqBody);

        String result = StringProcessor.getInstance().trim(input);
        Results results = new Results(true, result, null);

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

        OutputStream respBody = exchange.getResponseBody();
        Gson gson = new Gson();
        String json = gson.toJson(results);
        Serializer.writeBodyWithString(json, respBody);
        respBody.close();
    }
}

class LowercaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        String input = Serializer.readBodyAsString(reqBody);

        String result = StringProcessor.getInstance().toLowercase(input);
        Results results = new Results(true, result, null);

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

        OutputStream respBody = exchange.getResponseBody();
        Gson gson = new Gson();
        String json = gson.toJson(results);
        Serializer.writeBodyWithString(json, respBody);
        respBody.close();
    }
}

class ExecCommandHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

    }
}
