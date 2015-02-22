package fury.marvel.trinity.transformer.modifier;

import fury.marvel.trinity.transformer.TargetMethod;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by poets11 on 15. 1. 29..
 */
public class StatementModifier extends AbstractSqlModifier {
    public static final String JAVA_SQL_STATEMENT = "java.sql.Statement";
    
    protected TargetMethod executeMethodInStatement;
    protected TargetMethod executeQueryMethodInStatement;
    protected TargetMethod executeUpdateMethodInStatement;

    public StatementModifier() throws IOException {
        init();
    }

    private void init() throws IOException {
        executeMethodInStatement        = new TargetMethod("execute", new String[]{"java.lang.String"});
        executeQueryMethodInStatement   = new TargetMethod("executeQuery", new String[]{"java.lang.String"});
        executeUpdateMethodInStatement  = new TargetMethod("executeUpdate", new String[]{"java.lang.String"});
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
            
            if(executeMethodInStatement.isEqualCtMethod(method)) setTraceExecuteStatement(method);
            else if(executeQueryMethodInStatement.isEqualCtMethod(method)) setTraceExecuteStatement(method);
            else if(executeUpdateMethodInStatement.isEqualCtMethod(method)) setTraceExecuteStatement(method);
        }
    }

    protected void setTraceExecuteStatement(CtMethod target) throws CannotCompileException {
        target.addLocalVariable(VAR_NAME, ctSqlStackInfo);

        String beforeStatement = createStatementBlock(createInitVarStatement(SQL_STACK_INFO), SET_SQL, PUSH_MESSAGE);
        target.insertBefore(beforeStatement);

        String afterStatement = createStatementBlock(SET_RESULT, POP_MESSAGE);
        target.insertAfter(afterStatement);
    }
}
