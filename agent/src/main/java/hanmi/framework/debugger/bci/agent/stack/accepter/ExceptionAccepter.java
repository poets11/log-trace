package hanmi.framework.debugger.bci.agent.stack.accepter;

import hanmi.framework.debugger.bci.agent.stack.info.StackInfo;
import hanmi.framework.debugger.bci.agent.stack.info.ExceptionStackInfo;

import java.util.Stack;

/**
 * Created by poets11 on 15. 5. 21..
 */
public class ExceptionAccepter extends AbstractStackInfoAccepter {
    @Override
    public boolean acceptPush(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object[] arguments) {
        if (arguments != null && arguments.length == 1) {
            Class<?> exceptionClass = arguments[0].getClass();
            String exceptionClassName = exceptionClass.getName();
            String methodName = exceptionClassName.substring(exceptionClassName.lastIndexOf(".") + 1);
            if (Exception.class.isAssignableFrom(exceptionClass)) {
                StackInfo stackInfo = initStackInfo(new ExceptionStackInfo(), exceptionClass, methodName, arguments);
                appendAndPush(currentStack, stackInfo);

                StackInfo root = currentStack.get(0);
                popUntilCurrentStackInfo(currentStack, root);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean acceptPop(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object returnValue) {
        return false;
    }
}
