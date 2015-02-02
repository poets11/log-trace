package fury.marvel.shiva.transformer.modifier.init;

import fury.marvel.shiva.trace.stack.init.RequestStackInfo;
import fury.marvel.shiva.transformer.modifier.ClassModifier;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.springframework.web.servlet.HandlerAdapter;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class HandlerAdaptorModifier extends ClassModifier {
    public static final String REQUEST_STACK_INFO_CLASS_NAME = RequestStackInfo.class.getName();
    public static final String VAR_NAME = "_stackInfo";

    public static final String TRACE_OPEN_MESSAGE = "{ " + VAR_NAME + " = new " + REQUEST_STACK_INFO_CLASS_NAME + "(); "
            + VAR_NAME + ".setRequest($1); " + VAR_NAME + ".setStart(System.currentTimeMillis()); }";
    public static final String TRACE_CLOSE_MESSAGE = "{ " + VAR_NAME + ".setEnd(System.currentTimeMillis()); "
            + VAR_NAME + ".setModelAndView(($w)$_); }";

    private CtClass requestStackInfo;
    private CtClass handlerAdaptor;
    private String handleMethod;

    public HandlerAdaptorModifier() {
        init();
    }

    private void init() {
        requestStackInfo = createCtClass(RequestStackInfo.class);
        handlerAdaptor = createCtClass(HandlerAdapter.class);
        handleMethod = "handle";
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        boolean instanceOfHandlerAdaptor = target.subtypeOf(handlerAdaptor);
        return instanceOfHandlerAdaptor;
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        try {
            CtMethod ctMethod = target.getDeclaredMethod(handleMethod);
            ctMethod.addLocalVariable(VAR_NAME, requestStackInfo);
            ctMethod.insertBefore(getPushMessage(VAR_NAME));
            ctMethod.insertBefore(TRACE_OPEN_MESSAGE);
            ctMethod.insertAfter(TRACE_CLOSE_MESSAGE);
            ctMethod.insertAfter(getPopMessage(VAR_NAME));
        } catch (NotFoundException nfe) {

        }
    }
}
