package hanmi.framework.debugger.bci.agent.stack.accepter;

import hanmi.framework.debugger.bci.agent.stack.accepter.AbstractStackInfoAccepter;
import hanmi.framework.debugger.bci.agent.stack.info.StackInfo;
import hanmi.framework.debugger.bci.agent.stack.info.ResultSetStackInfo;

import java.util.Stack;

/**
 * Created by poets11 on 15. 5. 20..
 */
public class ResultSetAccepter extends AbstractStackInfoAccepter {
    @Override
    public boolean acceptPush(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object[] arguments) {
        if (isAcceptable(targetClass)) {
            ResultSetStackInfo stackInfo = (ResultSetStackInfo) initStackInfo(new ResultSetStackInfo(), targetClass, targetMethod, arguments);
            appendAndPush(currentStack, stackInfo);
            return true;
        }

        return false;
    }

    @Override
    public boolean acceptPop(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object returnValue) {
        if (isAcceptable(targetClass)) {
            StackInfo currentStackInfo = findCurrentStackInfo(currentStack, targetClass, targetMethod);
            currentStackInfo.setResultValue(returnValue);
            popUntilCurrentStackInfo(currentStack, currentStackInfo);
            return true;
        }

        return false;
    }

    private boolean isAcceptable(Class targetClass) {
        try {
            Class<?> aClass = Class.forName("java.sql.ResultSet");
            return aClass.isAssignableFrom(targetClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
