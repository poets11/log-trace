package hanmi.framework.debugger.bci.agent.stack.accepter;

import hanmi.framework.debugger.bci.agent.stack.info.StackInfo;
import hanmi.framework.debugger.bci.agent.stack.info.HandlerAdapterStackInfo;

import java.util.Stack;

/**
 * Created by poets11 on 15. 5. 19..
 */
public class HandlerAdapterAccepter extends AbstractStackInfoAccepter {
    public static String[] handlerAdapters = {
//            "org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter",
//            "org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter",
            "org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"
    };

    @Override
    public boolean acceptPush(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object[] arguments) {
        if (isAcceptable(targetClass)) {
            HandlerAdapterStackInfo stackInfo = (HandlerAdapterStackInfo) initStackInfo(new HandlerAdapterStackInfo(), targetClass, targetMethod, arguments);
            appendAndPush(currentStack, stackInfo);
            return true;
        }

        return false;
    }

    @Override
    public boolean acceptPop(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object returnValue) {
        if (isAcceptable(targetClass)) {
            StackInfo currentStackInfo = findCurrentStackInfo(currentStack, targetClass, targetMethod);
            if (currentStack == null) {
                return false;
            }
            
            currentStackInfo.setResultValue(returnValue);
            popUntilCurrentStackInfo(currentStack, currentStackInfo);
            return true;
        }

        return false;
    }

    @Override
    public boolean acceptPop(Stack<StackInfo> currentStack, Class targetClass, String targetMethod) {
        return false;
    }

    private boolean isAcceptable(Class targetClass) {
        for (int i = 0; i < handlerAdapters.length; i++) {
            String targetClassName = targetClass.getCanonicalName();
            if (handlerAdapters[i].equals(targetClassName)) {
                return true;
            }
        }

        return false;
    }
}
