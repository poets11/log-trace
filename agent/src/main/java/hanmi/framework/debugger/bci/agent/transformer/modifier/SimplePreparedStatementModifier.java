package hanmi.framework.debugger.bci.agent.transformer.modifier;

import hanmi.framework.debugger.bci.agent.assist.Method;
import hanmi.framework.debugger.bci.agent.stack.CallStack;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by poets11 on 15. 1. 29..
 */
public class SimplePreparedStatementModifier extends AbstractClassModifier {
    protected static final String JAVA_SQL_PREPARED_STATEMENT = "java.sql.PreparedStatement";

    protected Method execute;
    protected Method executeQuery;
    protected Method executeUpdate;

    protected List<Method> setters;

    public SimplePreparedStatementModifier() throws IOException {
        init();
    }

    private void init() throws IOException {
        execute = new Method("execute", null);
        executeQuery = new Method("executeQuery", null);
        executeUpdate = new Method("executeUpdate", null);

        setters = new ArrayList<Method>();
        setters.add(new Method("setInt", new String[]{"int", "int"}));
        setters.add(new Method("setLong", new String[]{"int", "log"}));
        setters.add(new Method("setFloat", new String[]{"int", "float"}));
        setters.add(new Method("setDouble", new String[]{"int", "double"}));
        setters.add(new Method("setDate", new String[]{"int", "java.util.Date"}));
        setters.add(new Method("setString", new String[]{"int", "java.lang.String"}));
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        return isInstanceOfInterface(target, JAVA_SQL_PREPARED_STATEMENT);
    }

    @Override
    protected void doModify(String className, CtClass target) throws CannotCompileException, NotFoundException {
        CtMethod[] methods = target.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            if (isAbstract(method)) continue;
            else if (isExecuteMethod(method)) setTraceExecutePreparedStatement(method);
            else if (isSetterMethod(method)) setTraceSetMethod(method);
        }
    }

    private boolean isSetterMethod(CtMethod method) throws NotFoundException {
        for (int i = 0; i < setters.size(); i++) {
            Method targetMethod = setters.get(i);
            if (targetMethod.isEqualCtMethod(method)) return true;
        }

        return false;
    }

    private boolean isExecuteMethod(CtMethod method) throws NotFoundException {
        return execute.isEqualCtMethod(method) || executeQuery.isEqualCtMethod(method) || executeUpdate.isEqualCtMethod(method);

    }

    protected void setTraceSetMethod(CtMethod method) throws CannotCompileException {
        String callStack = CallStack.class.getName();

        String beforeStatement = String.format("{ %s.push(this.getClass(), \"%s\", $args); }", callStack, method.getName());
        method.insertBefore(beforeStatement);

        String afterStatement = String.format("{ %s.pop(this.getClass(), \"%s\"); }", callStack, method.getName());
        method.insertAfter(afterStatement);
    }

    protected void setTraceExecutePreparedStatement(CtMethod target) throws CannotCompileException, NotFoundException {
        String callStack = CallStack.class.getName();

        String beforeStatement = String.format("{ %s.push(this.getClass(), \"%s\", null); }", callStack, target.getName());
        target.insertBefore(beforeStatement);

        String afterStatement = String.format("{ %s.pop(this.getClass(), \"%s\"); }", callStack, target.getName());
        target.insertAfter(afterStatement);
    }
}
