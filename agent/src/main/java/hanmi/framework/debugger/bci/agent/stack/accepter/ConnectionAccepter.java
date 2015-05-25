package hanmi.framework.debugger.bci.agent.stack.accepter;

import hanmi.framework.debugger.bci.agent.stack.info.StackInfo;
import hanmi.framework.debugger.bci.agent.stack.info.ConnectionStackInfo;

import java.util.Stack;

/**
 * Created by poets11 on 15. 5. 19..
 */
public class ConnectionAccepter extends AbstractStackInfoAccepter {
    @Override
    public boolean acceptPush(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object[] arguments) {
        if (isAcceptable(targetClass)) {
            ConnectionStackInfo stackInfo = (ConnectionStackInfo) initStackInfo(new ConnectionStackInfo(), targetClass, targetMethod, arguments);
            appendAndPush(currentStack, stackInfo);
            return true;
        }

        return false;
    }

    @Override
    public boolean acceptPop(Stack<StackInfo> currentStack, Class targetClass, String targetMethod) {
        if (isAcceptable(targetClass)) {
            StackInfo currentStackInfo = findCurrentStackInfo(currentStack, targetClass, targetMethod);
            popUntilCurrentStackInfo(currentStack, currentStackInfo);
            return true;
        }

        return false;
    }

    private boolean isAcceptable(Class targetClass) {
        try {
            Class<?> aClass = Class.forName("java.sql.Connection");
            if (aClass.isAssignableFrom(targetClass)) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }
}
