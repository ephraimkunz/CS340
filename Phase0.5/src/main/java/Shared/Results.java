package Shared;

/**
 * Created by ephraimkunz on 1/12/18.
 */

public class Results {
    private boolean success;
    private String data;
    public static final String NumberFormatException = "NumberFormatException";

    public boolean isSuccess() {
        return success;
    }

    public String getData() {
        return data;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    private String errorInfo;

    public Results(boolean success, String data, String errorInfo) {
        this.success = success;
        this.data = data;
        this.errorInfo = errorInfo;
    }
}
