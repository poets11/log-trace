package hanmi.framework.debugger.bci.agent.transformer.modifier;

import hanmi.framework.debugger.bci.agent.assist.Method;
import hanmi.framework.debugger.bci.agent.stack.CallStack;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by poets11 on 15. 2. 2..
 */
public class SimpleResultSetModifier extends AbstractClassModifier {
    protected static final String JAVA_SQL_RESULT_SET = "java.sql.ResultSet";

    protected CtClass ctResultSetStackInfo;
    protected List<Method> getters;

    public SimpleResultSetModifier() throws IOException {
        init();
    }

    protected void init() throws IOException {
        getters = new ArrayList<Method>();
        getters.add(new Method("getInt", new String[]{"java.lang.String"}));
        getters.add(new Method("getLong", new String[]{"java.lang.String"}));
        getters.add(new Method("getFloat", new String[]{"java.lang.String"}));
        getters.add(new Method("getDouble", new String[]{"java.lang.String"}));
        getters.add(new Method("getString", new String[]{"java.lang.String"}));
        getters.add(new Method("getDate", new String[]{"java.lang.String"}));
        getters.add(new Method("getTime", new String[]{"java.lang.String"}));
        getters.add(new Method("getTimestamp", new String[]{"java.lang.String"}));
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        return isInstanceOfInterface(target, JAVA_SQL_RESULT_SET);
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        CtMethod[] methods = target.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            if (isAbstract(method)) continue;

            for (int j = 0; j < getters.size(); j++) {
                Method targetMethod = getters.get(j);
                if (targetMethod.isEqualCtMethod(method)) {
                    setTraceGetMethod(method);
                    break;
                }
            }

            if (method.getName().equals("next")) {
                setTraceNextMethod(method);
            }
        }
    }

    private void setTraceNextMethod(CtMethod target) throws CannotCompileException {
        String callStack = CallStack.class.getName();

        String beforeStatement = String.format("{ %s.push(this.getClass(), \"%s\", null); }", callStack, target.getName());
        target.insertBefore(beforeStatement);

        String afterStatement = String.format("{ %s.pop(this.getClass(), \"%s\", ($w)$_); }", callStack, target.getName());
        target.insertAfter(afterStatement);
    }

    protected void setTraceGetMethod(CtMethod target) throws CannotCompileException {
        String callStack = CallStack.class.getName();

        String beforeStatement = String.format("{ %s.push(this.getClass(), \"%s\", $args); }", callStack, target.getName());
        target.insertBefore(beforeStatement);

        String afterStatement = String.format("{ %s.pop(this.getClass(), \"%s\", ($w)$_); }", callStack, target.getName());
        target.insertAfter(afterStatement);
    }
}
