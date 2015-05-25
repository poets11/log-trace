package hanmi.framework.debugger.bci.agent.stack.accepter;

import hanmi.framework.debugger.bci.agent.stack.accepter.StackInfoAccepter;
import hanmi.framework.debugger.bci.agent.stack.info.StackInfo;

import java.util.Stack;

/**
 * Created by poets11 on 15. 5. 20..
 */
public abstract class AbstractStackInfoAccepter implements StackInfoAccepter {
    protected StackInfo findCurrentStackInfo(Stack<StackInfo> currentStack, Class targetClass, String targetMethod) {
        String className = targetClass.getName();

        int initVal = currentStack.size() - 1;
        for (int i = initVal; i >= 0; i--) {
            StackInfo stackInfo = currentStack.get(i);
            if (className.equals(stackInfo.getClassName()) && targetMethod.equals(stackInfo.getMethodName())) {
                return stackInfo;
            }
        }

        return null;
    }

    protected void appendAndPush(Stack<StackInfo> currentStack, StackInfo stackInfo) {
        if (currentStack.isEmpty() == false) {
            StackInfo parentStackInfo = currentStack.peek();
            parentStackInfo.appendInnerStack(stackInfo);
        }

        currentStack.push(stackInfo);
    }

    protected void popUntilCurrentStackInfo(Stack<StackInfo> currentStack, StackInfo currentStackInfo) {
        int index = currentStack.indexOf(currentStackInfo);

        int initVal = currentStack.size() - 1;
        for (int i = initVal; i >= index && currentStack.size() > 1; i--) {
            StackInfo pop = currentStack.pop();
            pop.setEndTime(System.currentTimeMillis());
        }
    }

    protected StackInfo initStackInfo(StackInfo stackInfo, Class targetClass, String targetMethod) {
        stackInfo.setClassName(targetClass.getName());
        stackInfo.setMethodName(targetMethod);
        stackInfo.setStartTime(System.currentTimeMillis());

        return stackInfo;
    }

    protected StackInfo initStackInfo(StackInfo stackInfo, Class targetClass, String targetMethod, Object[] arguments) {
        stackInfo.setClassName(targetClass.getName());
        stackInfo.setMethodName(targetMethod);
        stackInfo.setArguments(arguments);
        stackInfo.setStartTime(System.currentTimeMillis());

        return stackInfo;
    }

    @Override
    public boolean acceptPop(Stack<StackInfo> currentStack, Class targetClass, String targetMethod) {
        return false;
    }

    @Override
    public boolean acceptPop(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object returnValue) {
        return false;
    }
}
