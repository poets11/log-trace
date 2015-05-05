package fury.marvel.trinity.agent.bci.transformer.modifier;

import fury.marvel.trinity.agent.bci.transformer.modifier.reflect.Method;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.impl.RequestStackInfoImpl;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class HandlerAdapterModifier extends AbstractClassModifier {
    protected static final String REQUEST_STACK_INFO = RequestStackInfoImpl.class.getName();
    protected static final String HANDLE_METHOD = "handle";
    protected static final String SET_REQUEST = VAR_NAME + ".setRequest($1);";
    protected static final String SET_MAV = VAR_NAME + ".setModelAndView(($w)$_);";

    protected Method handleMethod;
    protected CtClass ctRequestStackInfo;
    protected List<String> handlerAdapterClassNames;

    public HandlerAdapterModifier() throws IOException {
        init();
    }

    protected void init() throws IOException {
        ctRequestStackInfo = ctClassUtil.createCtClass(RequestStackInfoImpl.class);

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

        ctMethod.addLocalVariable(VAR_NAME, ctRequestStackInfo);

        String beforeStatement = createStatementBlock(
                STACK_MANAGER_INIT,
                createInitVarStatement(REQUEST_STACK_INFO), SET_REQUEST, PUSH_MESSAGE);
        ctMethod.insertBefore(beforeStatement);

        String afterStatement = createStatementBlock(
                createStatementBlock(SET_MAV, POP_MESSAGE),
                STACK_MANAGER_CLEAR);
        ctMethod.insertAfter(afterStatement);

        String catchStatement = createStatementBlock(EXCEPTION_CATCH_MESSAGE, STACK_MANAGER_CLEAR, "throw $e;");
        ctMethod.addCatch(catchStatement, ctException);
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
