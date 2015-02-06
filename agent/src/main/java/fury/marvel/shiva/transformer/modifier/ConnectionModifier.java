package fury.marvel.shiva.transformer.modifier;

import javassist.CtClass;
import javassist.CtMethod;

import java.io.IOException;
import java.sql.Connection;

/**
 * Created by poets11 on 15. 1. 29..
 */
public class ConnectionModifier extends AbstractSqlModifier {
    protected static final String PREPARE_STATEMENT_METHOD = "prepareStatement";

    protected CtClass ctConnection;

    public ConnectionModifier() throws IOException {
        init();
    }

    private void init() throws IOException {
        ctConnection = ctClassUtil.createCtClass(Connection.class);
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        return isSub(target, ctConnection);
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        CtMethod[] methods = target.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            String methodName = method.getName();

            if (methodName.equals(PREPARE_STATEMENT_METHOD)) {
                method.addLocalVariable(VAR_NAME, ctSqlStackInfo);

                String beforeStatement = createStatementBlock(createInitVarStatement(SQL_STACK_INFO), SET_SQL, PUSH_MESSAGE);
                method.insertBefore(beforeStatement);
            }
        }
    }
}
