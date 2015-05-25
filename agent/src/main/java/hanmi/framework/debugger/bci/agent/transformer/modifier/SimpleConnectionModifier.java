package hanmi.framework.debugger.bci.agent.transformer.modifier;

import hanmi.framework.debugger.bci.agent.assist.Method;
import hanmi.framework.debugger.bci.agent.stack.CallStack;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;

/**
 * Created by poets11 on 15. 1. 29..
 */
public class SimpleConnectionModifier extends AbstractClassModifier {
    protected static final String JAVA_SQL_CONNECTION = "java.sql.Connection";
    protected Method preparStatementMethod;

    public SimpleConnectionModifier() throws IOException {
        init();
    }

    private void init() throws IOException {
        preparStatementMethod = new Method("prepareStatement", new String[]{"java.lang.String"});
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        return isInstanceOfInterface(target, JAVA_SQL_CONNECTION);
    }

    @Override
    protected void doModify(String className, CtClass target) throws NotFoundException, CannotCompileException {
        CtMethod[] methods = target.getDeclaredMethods();

        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            if (isAbstract(method)) continue;

            if (preparStatementMethod.isEqualCtMethod(method)) {
                String callStack = CallStack.class.getName();

                String beforeStatement = String.format("{ %s.push(this.getClass(), \"%s\", $args); }", callStack, method.getName());
                method.insertBefore(beforeStatement);

                String afterStatement = String.format("{ %s.pop(this.getClass(), \"%s\"); }", callStack, method.getName());
                method.insertAfter(afterStatement);
            }
        }
    }
}
