package fury.marvel.shiva.transformer.modifier;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

/**
 * Created by poets11 on 15. 1. 29..
 */
public class StatementModifier extends AbstractSqlModifier {
    private static final String ADD_PARAM = VAR_NAME + ".addParam(($w)$2);";
    protected CtClass ctStatement;
    protected List<String> executeMethods;
    protected List<String> setMethods;

    public StatementModifier() throws IOException {
        init();
    }

    private void init() throws IOException {
        ctStatement     = ctClassUtil.createCtClass(Statement.class);
        executeMethods  = Arrays.asList(new String[]{"execute", "executeQuery", "executeUpdate"});
        setMethods      = Arrays.asList(new String[]{"setBigDecimal", "setDate", "setDouble",
                "setFloat", "setInt", "setLong", "setNull", "setString", "setTime", "setTimestamp"});
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        return isSub(target, ctStatement);
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        CtMethod[] methods = target.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            String methodName = method.getName();

            if (executeMethods.contains(methodName)) {
                CtClass[] parameterTypes = method.getParameterTypes();

                if (notEmpty(parameterTypes)) setTraceExecuteStatement(method);
                else setTraceExecutePreparedStatement(method);

                continue;
            }

            if (setMethods.contains(methodName)) {
                setTraceSetMethod(method);
            }
        }
    }

    protected void setTraceSetMethod(CtMethod target) throws CannotCompileException {
        target.addLocalVariable(VAR_NAME, ctSqlStackInfo);

        String afterStatement = createStatementBlock(createPeekVarStatement(SQL_STACK_INFO), ADD_PARAM);
        target.insertAfter(afterStatement);
    }

    protected boolean notEmpty(CtClass[] parameterTypes) {
        return parameterTypes != null && parameterTypes.length > 0;
    }

    protected void setTraceExecuteStatement(CtMethod target) throws CannotCompileException {
        target.addLocalVariable(VAR_NAME, ctSqlStackInfo);

        String beforeStatement = createStatementBlock(createInitVarStatement(SQL_STACK_INFO), SET_SQL, PUSH_MESSAGE);
        target.insertBefore(beforeStatement);

        String afterStatement = createStatementBlock(SET_RESULT, POP_MESSAGE);
        target.insertAfter(afterStatement);
    }

    protected void setTraceExecutePreparedStatement(CtMethod target) throws CannotCompileException, NotFoundException {
        target.addLocalVariable(VAR_NAME, ctSqlStackInfo);

        String afterStatement = createStatementBlock(createPeekVarStatement(SQL_STACK_INFO), SET_RESULT, POP_MESSAGE);
        target.insertAfter(afterStatement);
    }
}
