package Shared;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ephraimkunz on 1/12/18.
 */

public class GenericCommand implements ICommand {

    // All these must be primitive objects so we can ship them over the network.
    private String _className;
    private String _methodName;
    private String[] _paramTypes;
    private Object[] _paramValues;

    public GenericCommand(String className, String methodName,
                          String[] paramTypes, Object[] paramValues) {
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

            // Get _paramTypes as actual types rather than strings
            Class<?>[] classParamTypes = new Class<?>[_paramTypes.length];
            for(int i = 0; i < _paramTypes.length; ++i) {
                classParamTypes[i] = Class.forName(_paramTypes[i]);
            }

            Method method = receiverClass.getMethod(_methodName, classParamTypes);
            Object result = method.invoke(instanceObject, _paramValues);
            String formatted = (String)result;
            return new Results(true, formatted, null);

        } catch (InvocationTargetException e) { // Invocation threw an exception, so check for number format exception
            if (e.getCause().getClass().equals(NumberFormatException.class)) {
                return new Results(false, null, Results.NumberFormatException);
            }
            return new Results(false, null, e.getCause().toString()); // Threw an exception that we didn't know about
        } catch (Exception e) {
            return new Results(false, null, e.toString()); // Bad class or type name?
        }
    }
}
