package Server;

import Shared.IStringProcessor;

/**
 * Created by ephraimkunz on 1/12/18.
 */

public class StringProcessor implements IStringProcessor {
    private static StringProcessor instance = new StringProcessor();

    public static StringProcessor getInstance() {
        return instance;
    }

    private StringProcessor(){}

    @Override
    public String toLowercase(String s) {
        return s.toLowerCase();
    }

    @Override
    public String trim(String s) {
        return s.trim();
    }

    @Override
    public String parseInteger(String s) {
        Integer i = Integer.parseInt(s);
        return i.toString();
    }
}
