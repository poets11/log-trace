package hanmi.framework.debugger.bci.agent.stack.info.writer;

import hanmi.framework.debugger.bci.agent.stack.info.StackInfo;

import java.util.Stack;

/**
 * Created by poets11 on 15. 5. 20..
 */
public interface StackInfoWriter {
    void write(Stack<StackInfo> currentStack);

    void write(Stack<StackInfo> currentStack, boolean hasException);
}
