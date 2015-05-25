package hanmi.framework.debugger.bci.agent.stack.accepter;

import hanmi.framework.debugger.bci.agent.stack.info.StackInfo;

import java.util.Stack;

/**
 * Created by poets11 on 15. 5. 19..
 */
public interface StackInfoAccepter {
    boolean acceptPush(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object[] arguments);

    boolean acceptPop(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object returnValue);

    boolean acceptPop(Stack<StackInfo> currentStack, Class targetClass, String targetMethod);
}
