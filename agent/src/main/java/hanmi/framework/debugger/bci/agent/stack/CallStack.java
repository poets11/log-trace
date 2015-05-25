package hanmi.framework.debugger.bci.agent.stack;


import hanmi.framework.debugger.bci.agent.config.AgentConfig;
import hanmi.framework.debugger.bci.agent.stack.info.StackInfo;
import hanmi.framework.debugger.bci.agent.stack.info.writer.LogWriter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by poets11 on 15. 5. 15..
 */
public class CallStack {
    private static Map<Long, Stack<StackInfo>> threadStack = new HashMap<Long, Stack<StackInfo>>();

    public static void init() {
        try {
            long id = Thread.currentThread().getId();
            if (threadStack.containsKey(id) == false) {
                Stack<StackInfo> stack = new Stack<StackInfo>();
                threadStack.put(id, stack);

                AgentConfig.log("init call stack : " + id);
            }
        } catch (Exception e) {
            AgentConfig.error(e);
        }
    }

    public static void clear(boolean hasException) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        try {
            long id = Thread.currentThread().getId();
            Stack<StackInfo> currentStack = threadStack.get(id);

            if (currentStack != null) {
                LogWriter logWriter = new LogWriter();
                logWriter.write(currentStack, hasException);

                currentStack.clear();
                threadStack.remove(id);
                AgentConfig.log("clear call stack : " + id);
            }
        } catch (Exception e) {
            AgentConfig.error(e);
        }
    }

    private static Stack<StackInfo> getCurrentStack() {
        long id = Thread.currentThread().getId();
        return threadStack.get(id);
    }

    private static void forceClearCurrentStack() {
        long id = Thread.currentThread().getId();
        Stack<StackInfo> currentStack = threadStack.get(id);
        currentStack.clear();
        threadStack.remove(id);
        AgentConfig.error("force clear stack : " + id);
    }

    public static void push(Class targetClass, String targetMethod, final Object... arguments) {
        try {
            Stack<StackInfo> currentStack = getCurrentStack();
            if (currentStack != null) {
                StackInfoProcessor.processPushCallStack(currentStack, targetClass, targetMethod, arguments);
            }
        } catch (Exception e) {
            forceClearCurrentStack();
            AgentConfig.error(e);
        }
    }

    public static void pop(Class targetClass, String targetMethod, final Object returnValue) {
        try {
            Stack<StackInfo> currentStack = getCurrentStack();
            if (currentStack != null) {
                StackInfoProcessor.processPopCallStack(currentStack, targetClass, targetMethod, returnValue);
            }
        } catch (Exception e) {
            forceClearCurrentStack();
            AgentConfig.error(e);
        }
    }

    public static void pop(Class targetClass, String targetMethod) {
        try {
            Stack<StackInfo> currentStack = getCurrentStack();
            if (currentStack != null) {
                StackInfoProcessor.processPopCallStack(currentStack, targetClass, targetMethod);
            }
        } catch (Exception e) {
            forceClearCurrentStack();
            AgentConfig.error(e);
        }
    }

    public static void exceptionCatch(Class targetClass, String targetMethod, final Object error) {
        try {
            Stack<StackInfo> currentStack = getCurrentStack();
            if (currentStack != null) {
                StackInfoProcessor.processExceptionCatch(currentStack, targetClass, targetMethod, new Object[]{error});
            }
        } catch (Exception e) {
            forceClearCurrentStack();
            AgentConfig.error(e);
        }
    }
}
