package fury.marvel.shiva.transformer.modifier.sql;

import fury.marvel.shiva.trace.stack.sql.SqlStackInfoImpl;
import fury.marvel.shiva.transformer.modifier.ClassModifier;
import javassist.CtClass;
import javassist.CtMethod;

import java.sql.Connection;

/**
 * Created by poets11 on 15. 1. 29..
 */
public class ConnectionModifier extends ClassModifier {
    public static final String SQL_STACK_INFO_CLASS_NAME = SqlStackInfoImpl.class.getName();
    public static final String OPEN_MESSAGE = VAR_NAME + " = new " + SQL_STACK_INFO_CLASS_NAME + "(); "
            + VAR_NAME + ".setSql($1); " + VAR_NAME + ".setStart(System.currentTimeMillis()); ";

    public static final String PREPARE_STATEMENT = "prepareStatement";

    private CtClass connection;
    private CtClass sqlStackInfo;

    public ConnectionModifier() {
        init();
    }

    private void init() {
        connection = createCtClass(Connection.class);
        sqlStackInfo = createCtClass(SqlStackInfoImpl.class);
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        boolean notProxy = className.indexOf("$") < 0;
        boolean subclassOf = target.subclassOf(connection);
        boolean subtypeOf = target.subtypeOf(connection);

        return (subclassOf || subtypeOf) && notProxy;
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        CtMethod[] methods = target.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            String methodName = method.getName();

            if (methodName.equals(PREPARE_STATEMENT)) {
                method.addLocalVariable(VAR_NAME, sqlStackInfo);
                method.insertBefore("{ " + OPEN_MESSAGE + getPushMessage(VAR_NAME) + " }");
            }
        }
    }
}
