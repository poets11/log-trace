package fury.marvel.trinity.transformer.modifier;

import fury.marvel.trinity.transformer.TargetMethod;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;

/**
 * Created by poets11 on 15. 1. 29..
 */
public class ConnectionModifier extends AbstractSqlModifier {
    protected static final String JAVA_SQL_CONNECTION = "java.sql.Connection";
    protected TargetMethod preparStatementMethod;

    public ConnectionModifier() throws IOException {
        init();
    }

    private void init() throws IOException {
        preparStatementMethod = new TargetMethod("prepareStatement", new String[]{"java.lang.String"});
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        return isInstanceOfInterface(target, JAVA_SQL_CONNECTION);
    }

    @Override
    protected void doModify(String className, CtClass target) throws NotFoundException, CannotCompileException {
        CtMethod[] methods = target.getDeclaredMethods();

        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];

            if (preparStatementMethod.isEqualCtMethod(method)) {
                method.addLocalVariable(VAR_NAME, ctSqlStackInfo);

                String beforeStatement = createStatementBlock(createInitVarStatement(SQL_STACK_INFO), SET_SQL, PUSH_MESSAGE);
                method.insertBefore(beforeStatement);
            }
        }
    }
}
