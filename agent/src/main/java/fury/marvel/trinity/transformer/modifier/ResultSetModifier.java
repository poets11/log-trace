package fury.marvel.trinity.transformer.modifier;

import fury.marvel.trinity.reflect.Method;
import fury.marvel.trinity.stack.info.impl.ResultSetStackInfoImpl;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by poets11 on 15. 2. 2..
 */
public class ResultSetModifier extends AbstractSqlModifier {
    protected static final String RESULT_SET_STACK_INFO = ResultSetStackInfoImpl.class.getName();
    protected static final String PEEK_SQL_MESSAGE = VAR_NAME + " = (" + SQL_STACK_INFO + ")"
            + STACK_MANAGER + ".peekSql();";
    protected static final String SQL_VAR_NAME = "_resultSetStackInfo_";
    protected static final String INIT_SQL_VAR_STATEMENT = SQL_VAR_NAME + " = (" + RESULT_SET_STACK_INFO + ")"
            + VAR_NAME + ".getResultSetStackInfo(); if(" + SQL_VAR_NAME + " == null) { " + SQL_VAR_NAME + " = new "
            + RESULT_SET_STACK_INFO + "(); " + VAR_NAME + ".setResultSet(" + SQL_VAR_NAME + "); "
            + SQL_VAR_NAME + ".initColumnNames(this); }";
    protected static final String ADD_RESULT_SET_VALUE = SQL_VAR_NAME + ".addResultSetValue(($w)$1, ($w)$_);";
    protected static final String NEXT = SQL_VAR_NAME + ".next($_);";
    protected static final String POP_SQL_MESSAGE = "if($_ == false) { " + STACK_MANAGER + ".popSql(" + VAR_NAME + "); }";
    protected static final String JAVA_SQL_RESULT_SET = "java.sql.ResultSet";

    protected CtClass ctResultSetStackInfo;
    protected List<Method> getters;

    public ResultSetModifier() throws IOException {
        init();
    }

    protected void init() throws IOException {
        ctResultSetStackInfo = ctClassUtil.createCtClass(ResultSetStackInfoImpl.class);

        getters = new ArrayList<Method>();
        getters.add(new Method("getInt", new String[]{"java.lang.String"}));
        getters.add(new Method("getLong", new String[]{"java.lang.String"}));
        getters.add(new Method("getFloat", new String[]{"java.lang.String"}));
        getters.add(new Method("getDouble", new String[]{"java.lang.String"}));
        getters.add(new Method("getDate", new String[]{"java.lang.String"}));
        getters.add(new Method("getString", new String[]{"java.lang.String"}));
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

            if (method.getName().equals("next")) setTraceNextMethod(method);
        }
    }

    private void setTraceNextMethod(CtMethod method) throws CannotCompileException {
        method.addLocalVariable(VAR_NAME, ctSqlStackInfo);
        method.addLocalVariable(SQL_VAR_NAME, ctResultSetStackInfo);

        String afterStatement = createStatementBlockWithStackManagerInit(PEEK_SQL_MESSAGE,
                INIT_SQL_VAR_STATEMENT, NEXT, POP_SQL_MESSAGE);
        method.insertAfter(afterStatement);
    }

    protected void setTraceGetMethod(CtMethod method) throws CannotCompileException {
        method.addLocalVariable(VAR_NAME, ctSqlStackInfo);
        method.addLocalVariable(SQL_VAR_NAME, ctResultSetStackInfo);

        String afterStatement = createStatementBlockWithStackManagerInit(PEEK_SQL_MESSAGE,
                INIT_SQL_VAR_STATEMENT, ADD_RESULT_SET_VALUE);
        method.insertAfter(afterStatement);
    }
}
