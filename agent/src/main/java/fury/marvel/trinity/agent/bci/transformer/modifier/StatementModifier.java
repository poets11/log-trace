package fury.marvel.trinity.agent.bci.transformer.modifier;

import fury.marvel.trinity.agent.bci.transformer.modifier.reflect.Method;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.IOException;

/**
 * Created by poets11 on 15. 1. 29..
 */
public class StatementModifier extends AbstractSqlModifier {
    public static final String JAVA_SQL_STATEMENT = "java.sql.Statement";

    protected Method executeMethodInStatement;
    protected Method executeQueryMethodInStatement;
    protected Method executeUpdateMethodInStatement;

    public StatementModifier() throws IOException {
        init();
    }

    private void init() throws IOException {
        executeMethodInStatement = new Method("execute", new String[]{"java.lang.String"});
        executeQueryMethodInStatement = new Method("executeQuery", new String[]{"java.lang.String"});
        executeUpdateMethodInStatement = new Method("executeUpdate", new String[]{"java.lang.String"});
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        return isInstanceOfInterface(target, JAVA_SQL_STATEMENT);
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        CtMethod[] methods = target.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            if (isAbstract(method)) continue;

            if (executeMethodInStatement.isEqualCtMethod(method)
                    || executeQueryMethodInStatement.isEqualCtMethod(method)
                    || executeUpdateMethodInStatement.isEqualCtMethod(method)) {
                setTraceExecuteStatement(method);
            }
        }
    }

    protected void setTraceExecuteStatement(CtMethod target) throws CannotCompileException {
        target.addLocalVariable(VAR_NAME, ctSqlStackInfo);

        String beforeStatement = createStatementBlock(INIT_VAR_NULL,
                createStatementBlockWithStackManagerInit(createInitVarStatement(SQL_STACK_INFO), SET_SQL, PUSH_MESSAGE));
        target.insertBefore(beforeStatement);

        String afterStatement = createStatementBlockWithStackManagerInit(SET_RESULT, POP_MESSAGE);
        target.insertAfter(afterStatement);
    }
}
