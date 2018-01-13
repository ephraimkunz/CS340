package Client;

import com.google.gson.Gson;

import Shared.GenericCommand;
import Shared.ICommand;
import Shared.IStringProcessor;
import Shared.Results;

/**
 * Created by ephraimkunz on 1/12/18.
 */

public class StringProcessorProxy_Commands implements IStringProcessor {
    private static StringProcessorProxy_Commands instance = new StringProcessorProxy_Commands();
    private static final String url = "http://localhost:8080/execCommand";
    private static final String recieverClass = "Server.StringProcessor";
    private static final String stringClass = "java.lang.String";
    private static final String lowercaseMethod = "toLowercase";
    private static final String trimMethod = "trim";
    private static final String parseIntMethod = "parseInteger";

    private StringProcessorProxy_Commands(){}

    public static StringProcessorProxy_Commands getInstance() {
        return instance;
    }

    private static Results getResults(ICommand command) {
        Gson gson = new Gson();
        String json = gson.toJson(command);
        return ClientCommunicator.getInstance().send(url, json);
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
        ICommand command = new GenericCommand(
                recieverClass,
                lowercaseMethod,
                new String[]{stringClass},
                new Object[]{s});

        Results results = getResults(command);
        return response(results);
    }

    @Override
    public String trim(String s) {
        ICommand command = new GenericCommand(
                recieverClass,
                trimMethod,
                new String[]{stringClass},
                new Object[]{s});

        Results results = getResults(command);
        return response(results);
    }

    @Override
    public String parseInteger(String s) {
        ICommand command = new GenericCommand(
                recieverClass,
                parseIntMethod,
                new String[]{stringClass},
                new Object[]{s});

        Results results = getResults(command);

        if (results.isSuccess()) {
            return results.getData();
        } else if (results.getErrorInfo().equals(Results.NumberFormatException)) {
            throw new NumberFormatException();
        }

        return results.getErrorInfo();
    }
}
