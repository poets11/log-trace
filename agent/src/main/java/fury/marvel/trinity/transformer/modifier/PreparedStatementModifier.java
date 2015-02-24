package fury.marvel.trinity.transformer.modifier;

import fury.marvel.trinity.reflect.Method;
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
public class PreparedStatementModifier extends AbstractSqlModifier {
    protected static final String JAVA_SQL_PREPARED_STATEMENT = "java.sql.PreparedStatement";
    protected static final String ADD_PARAM = VAR_NAME + ".addParam(($w)$2);";

    protected Method execute;
    protected Method executeQuery;
    protected Method executeUpdate;

    protected List<Method> setters;

    public PreparedStatementModifier() throws IOException {
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
        setters.add(new Method("setNull", new String[]{"int", "java.math.BigDecimal"}));
        setters.add(new Method("setString", new String[]{"int", "java.math.BigDecimal"}));
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

    protected void setTraceSetMethod(CtMethod target) throws CannotCompileException {
        target.addLocalVariable(VAR_NAME, ctSqlStackInfo);

        String afterStatement = createStatementBlock(INIT_VAR_NULL,
                createStatementBlockWithStackManagerInit(createPeekVarStatement(SQL_STACK_INFO), ADD_PARAM));
        target.insertAfter(afterStatement);
    }

    protected void setTraceExecutePreparedStatement(CtMethod target) throws CannotCompileException, NotFoundException {
        target.addLocalVariable(VAR_NAME, ctSqlStackInfo);

        String afterStatement = createStatementBlock(INIT_VAR_NULL,
                createStatementBlockWithStackManagerInit(createPeekVarStatement(SQL_STACK_INFO), SET_RESULT, POP_MESSAGE));
        target.insertAfter(afterStatement);
    }
}
