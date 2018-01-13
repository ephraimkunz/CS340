package Client;

import Shared.IStringProcessor;
import Shared.Results;

/**
 * Created by ephraimkunz on 1/12/18.
 */

public class StringProcessorProxy_NoCommands implements IStringProcessor {
    private static StringProcessorProxy_NoCommands instance = new StringProcessorProxy_NoCommands();
    private static final String baseURL = "http://localhost:8080/";

    private StringProcessorProxy_NoCommands(){}

    public static StringProcessorProxy_NoCommands getInstance() {
        return instance;
    }

    private static String response(Results results) {
        if (results.isSuccess()) {
            return results.getData();
        } else {
            return results.getErrorInfo(); // For debugging. We don't expect to ever get this.
        }
    }

    @Override
    public String toLowercase(String s) {
        ClientCommunicator comm = ClientCommunicator.getInstance();
        String url = baseURL + "lowercase";
        Results results = comm.send(url, s);
        return response(results);
    }

    @Override
    public String trim(String s) {
        ClientCommunicator comm = ClientCommunicator.getInstance();
        String url = baseURL + "trim";
        Results results = comm.send(url, s);
        return response(results);
    }

    @Override
    public String parseInteger(String s) {
        ClientCommunicator comm = ClientCommunicator.getInstance();
        String url = baseURL + "parseinteger";
        Results results = comm.send(url, s);

        if (results.isSuccess()) {
            return results.getData();
        } else if (results.getErrorInfo().equals(Results.NumberFormatException)) {
            throw new NumberFormatException();
        }

        return results.getErrorInfo(); // For debugging. Should never reach here normally.
    }
}
