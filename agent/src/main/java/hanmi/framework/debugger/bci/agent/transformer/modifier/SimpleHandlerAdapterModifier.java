package hanmi.framework.debugger.bci.agent.transformer.modifier;

import hanmi.framework.debugger.bci.agent.assist.Method;
import hanmi.framework.debugger.bci.agent.config.AgentConfig;
import hanmi.framework.debugger.bci.agent.stack.CallStack;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class SimpleHandlerAdapterModifier extends AbstractClassModifier {
    protected static final String HANDLE_METHOD = "handle";

    protected Method handleMethod;
    protected List<String> handlerAdapterClassNames;

    public SimpleHandlerAdapterModifier() throws IOException {
        init();
    }

    protected void init() {
        handleMethod = new Method(HANDLE_METHOD, new String[]{
                "javax.servlet.http.HttpServletRequest",
                "javax.servlet.http.HttpServletResponse",
                "java.lang.Object"
        });

        handlerAdapterClassNames = Arrays.asList(new String[]{
//                "org/springframework/web/servlet/mvc/HttpRequestHandlerAdapter",      // ignore static resource request
                "org/springframework/web/servlet/mvc/SimpleControllerHandlerAdapter",
                "org/springframework/web/servlet/mvc/method/AbstractHandlerMethodAdapter"
        });
    }


    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        return handlerAdapterClassNames.contains(className);
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        CtMethod ctMethod = getHandleMethod(target);
        if (ctMethod == null) return;

        AgentConfig.log("MOD : " + className);

        String callStack = CallStack.class.getName();

        String beforeStatement = String.format("{ %s.init(); %s.push(this.getClass(), \"%s\", $args); }", callStack, callStack, ctMethod.getName());
        ctMethod.insertBefore(beforeStatement);

        String afterStatement = String.format("{ %s.pop(this.getClass(), \"%s\", ($w)$_); %s.clear(false); }", callStack, ctMethod.getName(), callStack);
        ctMethod.insertAfter(afterStatement);

        String catchStatement = String.format("{ %s.exceptionCatch(this.getClass(), \"%s\", $e); %s.clear(true); throw $e;}", callStack, ctMethod.getName(), callStack);
        CtClass ctClass = ctClassUtil.createCtClass(Exception.class);

        ctMethod.addCatch(catchStatement, ctClass);
    }

    protected CtMethod getHandleMethod(CtClass target) throws NotFoundException {
        CtMethod[] methods = target.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            CtMethod ctMethod = methods[i];

            if (isAbstract(ctMethod)) continue;

            if (handleMethod.isEqualCtMethod(ctMethod)) return ctMethod;
        }

        return null;
    }
}
