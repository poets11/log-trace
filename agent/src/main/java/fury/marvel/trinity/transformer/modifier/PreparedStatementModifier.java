package fury.marvel.trinity.transformer.modifier;

import fury.marvel.trinity.transformer.TargetMethod;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by poets11 on 15. 1. 29..
 */
public class PreparedStatementModifier extends AbstractSqlModifier {
    protected static final String JAVA_SQL_PREPARED_STATEMENT = "java.sql.PreparedStatement";
    protected static final String ADD_PARAM = VAR_NAME + ".addParam(($w)$2);";

    protected TargetMethod execute;
    protected TargetMethod executeQuery;
    protected TargetMethod executeUpdate;

    protected List<TargetMethod> setters;

    public PreparedStatementModifier() throws IOException {
        init();
    }

    private void init() throws IOException {
        execute         = new TargetMethod("execute", null);
        executeQuery    = new TargetMethod("executeQuery", null);
        executeUpdate   = new TargetMethod("executeUpdate", null);

        setters = new ArrayList<TargetMethod>();
        setters.add(new TargetMethod("setInt",      new String[]{"int", "int"}));
        setters.add(new TargetMethod("setLong",     new String[]{"int", "log"}));
        setters.add(new TargetMethod("setFloat",    new String[]{"int", "float"}));
        setters.add(new TargetMethod("setDouble",   new String[]{"int", "double"}));
        setters.add(new TargetMethod("setDate",     new String[]{"int", "java.util.Date"}));
        setters.add(new TargetMethod("setNull",     new String[]{"int", "java.math.BigDecimal"}));
        setters.add(new TargetMethod("setString",   new String[]{"int", "java.math.BigDecimal"}));
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
            
            if(isExecuteMethod(method)) setTraceExecutePreparedStatement(method);
            else if(isSetterMethod(method)) setTraceSetMethod(method);
        }
    }

    private boolean isSetterMethod(CtMethod method) throws NotFoundException {
        for (int i = 0; i < setters.size(); i++) {
            TargetMethod targetMethod = setters.get(i);
            if(targetMethod.isEqualCtMethod(method)) return true;
        }
        
        return false;
    }

    private boolean isExecuteMethod(CtMethod method) throws NotFoundException {
        return execute.isEqualCtMethod(method) || executeQuery.isEqualCtMethod(method) || executeUpdate.isEqualCtMethod(method);
        
    }

    protected void setTraceSetMethod(CtMethod target) throws CannotCompileException {
        target.addLocalVariable(VAR_NAME, ctSqlStackInfo);

        String afterStatement = createStatementBlock(createPeekVarStatement(SQL_STACK_INFO), ADD_PARAM);
        target.insertAfter(afterStatement);
    }

    protected void setTraceExecutePreparedStatement(CtMethod target) throws CannotCompileException, NotFoundException {
        target.addLocalVariable(VAR_NAME, ctSqlStackInfo);

        String afterStatement = createStatementBlock(createPeekVarStatement(SQL_STACK_INFO), SET_RESULT, POP_MESSAGE);
        target.insertAfter(afterStatement);
    }
}
