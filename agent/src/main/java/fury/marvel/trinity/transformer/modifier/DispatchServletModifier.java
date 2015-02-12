package fury.marvel.trinity.transformer.modifier;

import fury.marvel.trinity.stack.info.impl.RequestStackInfoImpl;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.IOException;

/**
 * Created by poets11 on 15. 2. 11..
 */
public class DispatchServletModifier extends AbstractClassModifier {
    protected static final String DISPATCHER_SERVLET_CLASS_NAME = "org/springframework/web/servlet/DispatcherServlet";
    protected static final String REQUEST_STACK_INFO = RequestStackInfoImpl.class.getName();
    protected static final String SET_REQUEST = VAR_NAME + ".setRequest($1);";
    protected static final String DO_SERVICE = "doService";

    protected CtClass ctRequestStackInfo;

//    contentType = ctx.getContentType();
//    contentType.startsWith("multipart/");
    
    public DispatchServletModifier() throws IOException {
        init();
    }

    private void init() throws IOException {
        ctRequestStackInfo = ctClassUtil.createCtClass(RequestStackInfoImpl.class);
        
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        return className.equals(DISPATCHER_SERVLET_CLASS_NAME);
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        CtMethod ctMethod = target.getDeclaredMethod(DO_SERVICE);
        ctMethod.addLocalVariable(VAR_NAME, ctRequestStackInfo);

        String beforeStatement = createStatementBlock(createInitVarStatement(REQUEST_STACK_INFO), SET_REQUEST, PUSH_MESSAGE);
        ctMethod.insertBefore(beforeStatement);

        String afterStatement = createStatementBlock(POP_MESSAGE);
        ctMethod.insertAfter(afterStatement);
    }
}
