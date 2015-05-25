package hanmi.framework.debugger.bci.agent.stack;

import hanmi.framework.debugger.bci.agent.stack.info.StackInfo;
import hanmi.framework.debugger.bci.agent.stack.accepter.StackInfoAccepter;
import hanmi.framework.debugger.bci.agent.stack.accepter.ExceptionAccepter;
import hanmi.framework.debugger.bci.agent.stack.accepter.PackageAccepter;
import hanmi.framework.debugger.bci.agent.stack.accepter.HandlerAdapterAccepter;
import hanmi.framework.debugger.bci.agent.stack.accepter.ConnectionAccepter;
import hanmi.framework.debugger.bci.agent.stack.accepter.PreparedStatementAccecpter;
import hanmi.framework.debugger.bci.agent.stack.accepter.ResultSetAccepter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by poets11 on 15. 5. 19..
 */
public class StackInfoProcessor {
    public static Stack<StackInfo> processPushCallStack(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object[] arguments) {
        List<StackInfoAccepter> stackInfoAccepters = getStackInfoAccepters();

        for (int i = 0; i < stackInfoAccepters.size(); i++) {
            StackInfoAccepter stackInfoAccepter = stackInfoAccepters.get(i);
            if (stackInfoAccepter.acceptPush(currentStack, targetClass, targetMethod, arguments)) {

                break;
            }
        }

        return currentStack;
    }

    public static Stack<StackInfo> processPopCallStack(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object returnValue) {
        List<StackInfoAccepter> stackInfoAccepters = getStackInfoAccepters();

        for (int i = 0; i < stackInfoAccepters.size(); i++) {
            StackInfoAccepter stackInfoAccepter = stackInfoAccepters.get(i);
            if (stackInfoAccepter.acceptPop(currentStack, targetClass, targetMethod, returnValue)) {
                break;
            }
        }

        return currentStack;
    }

    public static Stack<StackInfo> processPopCallStack(Stack<StackInfo> currentStack, Class targetClass, String targetMethod) {
        List<StackInfoAccepter> stackInfoAccepters = getStackInfoAccepters();

        for (int i = 0; i < stackInfoAccepters.size(); i++) {
            StackInfoAccepter stackInfoAccepter = stackInfoAccepters.get(i);
            if (stackInfoAccepter.acceptPop(currentStack, targetClass, targetMethod)) {
                break;
            }
        }

        return currentStack;
    }

    public static Stack<StackInfo> processExceptionCatch(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object[] arguments) {
        List<StackInfoAccepter> stackInfoAccepters = new ArrayList<StackInfoAccepter>();
        stackInfoAccepters.add(new ExceptionAccepter());

        for (int i = 0; i < stackInfoAccepters.size(); i++) {
            StackInfoAccepter stackInfoAccepter = stackInfoAccepters.get(i);
            if (stackInfoAccepter.acceptPush(currentStack, targetClass, targetMethod, arguments)) {
                break;
            }
        }

        return currentStack;
    }

    private static List<StackInfoAccepter> getStackInfoAccepters() {
        List<StackInfoAccepter> stackInfoAccepters = new ArrayList<StackInfoAccepter>();
        stackInfoAccepters.add(new PackageAccepter());
        stackInfoAccepters.add(new HandlerAdapterAccepter());
        stackInfoAccepters.add(new ConnectionAccepter());
        stackInfoAccepters.add(new PreparedStatementAccecpter());
        stackInfoAccepters.add(new ResultSetAccepter());
        return stackInfoAccepters;
    }
}
