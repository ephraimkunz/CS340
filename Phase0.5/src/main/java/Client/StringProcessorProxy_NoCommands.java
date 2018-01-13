package Client;

import Shared.IStringProcessor;
import Shared.Results;

/**
 * Created by ephraimkunz on 1/12/18.
 */

public class StringProcessorProxy_NoCommands implements IStringProcessor {
    private static StringProcessorProxy_NoCommands instance = new StringProcessorProxy_NoCommands();

    private StringProcessorProxy_NoCommands(){}

    public static StringProcessorProxy_NoCommands getInstance() {
        return instance;
    }

    @Override
    public String toLowercase(String s) {
        ClientCommunicator comm = ClientCommunicator.getInstance();
        String url = "http://localhost:8080/lowercase";
        Results results = comm.send(url, s);
        if (results.isSuccess()) {
            return results.getData();
        } else {
            return results.getErrorInfo();
        }
    }

    @Override
    public String trim(String s) {
        ClientCommunicator comm = ClientCommunicator.getInstance();
        String url = "http://localhost:8080/trim";
        Results results = comm.send(url, s);
        if (results.isSuccess()) {
            return results.getData();
        } else {
            return results.getErrorInfo();
        }
    }

    @Override
    public String parseInteger(String s) {
        ClientCommunicator comm = ClientCommunicator.getInstance();
        String url = "http://localhost:8080/parseinteger";
        Results results = comm.send(url, s);

        if (results.isSuccess()) {
            return results.getData();
        } else if (results.getErrorInfo().equals(Results.NumberFormatException)) {
            throw new NumberFormatException();
        }

        throw new RuntimeException(results.getErrorInfo());
    }
}
