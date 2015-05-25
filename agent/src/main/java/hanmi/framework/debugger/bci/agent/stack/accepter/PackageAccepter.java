package hanmi.framework.debugger.bci.agent.stack.accepter;

import hanmi.framework.debugger.bci.agent.config.AgentConfig;
import hanmi.framework.debugger.bci.agent.stack.info.StackInfo;
import hanmi.framework.debugger.bci.agent.stack.info.PackageStackInfo;

import java.util.Stack;

/**
 * Created by poets11 on 15. 5. 19..
 */
public class PackageAccepter extends AbstractStackInfoAccepter {
    @Override
    public boolean acceptPush(Stack<StackInfo> currentStack, Class targetClass, String targetMethod, Object[] arguments) {
        if (isAcceptable(targetClass)) {
            PackageStackInfo stackInfo = (PackageStackInfo) initStackInfo(new PackageStackInfo(), targetClass, targetMethod, arguments);
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
        String targetClassName = targetClass.getName();
        String basePackage = AgentConfig.get(AgentConfig.PROP_BASE_PACKAGE);

        return targetClassName.startsWith(basePackage);
    }
}
