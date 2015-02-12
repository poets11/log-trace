package fury.marvel.trinity.transformer.modifier;

import fury.marvel.trinity.stack.info.impl.RequestStackInfoImpl;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.springframework.web.servlet.HandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class HandlerAdapterModifier extends AbstractClassModifier {
    protected static final String REQUEST_STACK_INFO = RequestStackInfoImpl.class.getName();
    protected static final String HANDLE_METHOD = "handle";
    protected static final String SET_REQUEST = VAR_NAME + ".setRequest($1);";
    protected static final String SET_MAV = VAR_NAME + ".setModelAndView(($w)$_);";

    protected CtClass ctRequestStackInfo;
    protected CtClass ctHandlerAdapter;
    protected CtClass[] ctHandleParamTypes;

    public HandlerAdapterModifier() throws IOException {
        init();
    }

    protected void init() throws IOException {
        ctRequestStackInfo = ctClassUtil.createCtClass(RequestStackInfoImpl.class);
        ctHandlerAdapter = ctClassUtil.createCtClass(HandlerAdapter.class);

        ctHandleParamTypes = new CtClass[3];
        ctHandleParamTypes[0] = ctClassUtil.createCtClass(HttpServletRequest.class);
        ctHandleParamTypes[1] = ctClassUtil.createCtClass(HttpServletResponse.class);
        ctHandleParamTypes[2] = ctClassUtil.createCtClass(Object.class);
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        return isSub(target, ctHandlerAdapter);
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        try {
            CtMethod ctMethod = getHandleMethod(target);
            ctMethod.addLocalVariable(VAR_NAME, ctRequestStackInfo);

            String beforeStatement = createStatementBlock(createInitVarStatement(REQUEST_STACK_INFO), SET_REQUEST, PUSH_MESSAGE);
            ctMethod.insertBefore(beforeStatement);

            String afterStatement = createStatementBlock(SET_MAV, POP_MESSAGE);
            ctMethod.insertAfter(afterStatement);
        } catch (NotFoundException nfe) {
            // if not found method from target then do nothing
        }
    }

    protected CtMethod getHandleMethod(CtClass target) throws NotFoundException {
        return target.getDeclaredMethod(HANDLE_METHOD, ctHandleParamTypes);
    }
}
