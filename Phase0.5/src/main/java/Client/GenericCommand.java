package Client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import Shared.ICommand;
import Shared.Results;

/**
 * Created by ephraimkunz on 1/12/18.
 */

public class GenericCommand implements ICommand {

    private String _className;
    private String _methodName;
    private Class<?>[] _paramTypes;
    private Object[] _paramValues;

    public GenericCommand(String className, String methodName,
                          Class<?>[] paramTypes, Object[] paramValues) {
        _className = className;
        _methodName = methodName;
        _paramTypes = paramTypes;
        _paramValues = paramValues;
    }

    @Override
    public Results execute() {
        try {
            Class<?> receiverClass = Class.forName(_className);

            // Get singleton
            Method getInstanceMethod = receiverClass.getMethod("getInstance", null);
            Object instanceObject = getInstanceMethod.invoke(null, null);

            // Call method on it
            Method method = receiverClass.getMethod(_methodName, _paramTypes);
            Object result = method.invoke(instanceObject, _paramValues);
            String formatted = (String)result;
            return new Results(true, formatted, null);

        } catch (InvocationTargetException e) { // Invocation threw an exception, so check for number format exception
            if (e.getCause().getClass().equals(NumberFormatException.class)) {
                return new Results(false, null, Results.NumberFormatException);
            }
            return new Results(false, null, e.getCause().toString()); // Threw and exception that we didn't know about
        } catch (Exception e) {
            return new Results(false, null, e.toString()); // Bad class name?
        }
    }
}
