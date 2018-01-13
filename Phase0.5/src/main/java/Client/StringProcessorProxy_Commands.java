package Client;

import com.google.gson.Gson;

import Shared.ICommand;
import Shared.IStringProcessor;
import Shared.Results;

/**
 * Created by ephraimkunz on 1/12/18.
 */

public class StringProcessorProxy_Commands implements IStringProcessor {
    private static StringProcessorProxy_Commands instance = new StringProcessorProxy_Commands();

    private StringProcessorProxy_Commands(){}

    public static StringProcessorProxy_Commands getInstance() {
        return instance;
    }

    @Override
    public String toLowercase(String s) {
        ICommand command = new GenericCommand(
                "Server.StringProcessor",
                "toLowercase",
                new String[]{"java.lang.String"},
                new Object[]{s});

        Gson gson = new Gson();
        String json = gson.toJson(command);
        Results results = ClientCommunicator.getInstance().send("http://localhost:8080/execCommand", json);

        if (results.isSuccess()) {
            return results.getData();
        } else {
            return results.getErrorInfo();
        }
    }

    @Override
    public String trim(String s) {
        ICommand command = new GenericCommand(
                "Server.StringProcessor",
                "trim",
                new String[]{"java.lang.String"},
                new Object[]{s});

        Gson gson = new Gson();
        String json = gson.toJson(command);
        Results results = ClientCommunicator.getInstance().send("http://localhost:8080/execCommand", json);

        if (results.isSuccess()) {
            return results.getData();
        } else {
            return results.getErrorInfo();
        }
    }

    @Override
    public String parseInteger(String s) {
        ICommand command = new GenericCommand(
                "Server.StringProcessor",
                "parseInteger",
                new String[]{"java.lang.String"},
                new Object[]{s});

        Gson gson = new Gson();
        String json = gson.toJson(command);
        Results results = ClientCommunicator.getInstance().send("http://localhost:8080/execCommand", json);

        if (results.isSuccess()) {
            return results.getData();
        } else if (results.getErrorInfo().equals(Results.NumberFormatException)) {
            throw new NumberFormatException();
        }

        throw new RuntimeException(results.getErrorInfo());
    }
}
