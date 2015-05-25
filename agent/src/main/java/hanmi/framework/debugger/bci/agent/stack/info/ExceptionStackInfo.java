package hanmi.framework.debugger.bci.agent.stack.info;

import hanmi.framework.debugger.bci.agent.stack.info.StackInfo;

/**
 * Created by poets11 on 15. 5. 21..
 */
public class ExceptionStackInfo extends StackInfo {
    private Exception exception;

    @Override
    public void setArguments(Object[] arguments) {
        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];
                exception = (Exception) argument;
            }
        }
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public void setResultValue(Object resultValue) {

    }
}
