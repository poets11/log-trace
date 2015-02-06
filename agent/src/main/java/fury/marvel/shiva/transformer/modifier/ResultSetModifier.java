package fury.marvel.shiva.transformer.modifier;

import fury.marvel.shiva.stack.info.impl.ResultSetStackInfoImpl;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Arrays;
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

    protected CtClass ctResultSetStackInfo;
    protected CtClass ctResultSet;
    protected List<String> getMethods;

    public ResultSetModifier() throws IOException {
        init();
    }

    protected void init() throws IOException {
        ctResultSetStackInfo    = ctClassUtil.createCtClass(ResultSetStackInfoImpl.class);
        ctResultSet             = ctClassUtil.createCtClass(ResultSet.class);
        getMethods              = Arrays.asList(new String[]{"getBigDecimal", "getBoolean", "getDate",
                "getDouble", "getFloat", "getInt", "getLong", "getString", "getTime", "getTimestamp"});
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        return isSub(target, ctResultSet);
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        CtMethod[] methods = target.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            String methodName = method.getName();

            if (getMethods.contains(methodName)) {
                CtClass[] parameterTypes = method.getParameterTypes();

                if ("java.lang.String".equals(parameterTypes[0].getName())) {
                    method.addLocalVariable(VAR_NAME, ctSqlStackInfo);
                    method.addLocalVariable(SQL_VAR_NAME, ctResultSetStackInfo);

                    String afterStatement = createStatementBlock(PEEK_SQL_MESSAGE,
                            INIT_SQL_VAR_STATEMENT, ADD_RESULT_SET_VALUE);
                    method.insertAfter(afterStatement);
                }
            } else if (methodName.equals("next")) {
                method.addLocalVariable(VAR_NAME, ctSqlStackInfo);
                method.addLocalVariable(SQL_VAR_NAME, ctResultSetStackInfo);

                String afterStatement = createStatementBlock(PEEK_SQL_MESSAGE,
                        INIT_SQL_VAR_STATEMENT, NEXT, POP_SQL_MESSAGE);
                method.insertAfter(afterStatement);
            }
        }
    }
}
