package hanmi.framework.debugger.bci.agent.stack.accepter;

import hanmi.framework.debugger.bci.agent.stack.accepter.AbstractStackInfoAccepter;
import hanmi.framework.debugger.bci.agent.stack.info.StackInfo;
import hanmi.framework.debugger.bci.agent.stack.info.PreparedStatementStackInfo;

import java.util.Stack;

/**
 * Created by poets11 on 15. 5. 20..
 */
public class PreparedStatementAccecpter extends AbstractStackInfoAccepter {
    @Override
    public boolean acceptPush(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object[] arguments) {
        if (isAcceptable(targetClass)) {
            PreparedStatementStackInfo stackInfo = (PreparedStatementStackInfo) initStackInfo(new PreparedStatementStackInfo(), targetClass, targetMethod, arguments);
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
            Class<?> aClass = Class.forName("java.sql.PreparedStatement");
            return aClass.isAssignableFrom(targetClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
