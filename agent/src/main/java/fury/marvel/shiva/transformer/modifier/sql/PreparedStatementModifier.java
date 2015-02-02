package fury.marvel.shiva.transformer.modifier.sql;

import fury.marvel.shiva.trace.stack.sql.SqlStackInfoImpl;
import fury.marvel.shiva.transformer.modifier.ClassModifier;
import javassist.CtClass;
import javassist.CtMethod;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

/**
 * Created by poets11 on 15. 1. 29..
 */
public class PreparedStatementModifier extends ClassModifier {

    public static final String SQL_STACK_INFO_CLASS_NAME = SqlStackInfoImpl.class.getName();

    public List<String> TARGET_SETTER;
    public List<String> TARGET_EXECUTER;
    public String getResultSet = "getResultSet";

    private CtClass sqlStackInfo;
    private CtClass preparedStatement;

    public PreparedStatementModifier() {
        init();
    }

    private void init() {
        preparedStatement = createCtClass(PreparedStatement.class);
        sqlStackInfo = createCtClass(SqlStackInfoImpl.class);

        TARGET_SETTER = Arrays.asList(new String[]{"setBigDecimal", "setDate", "setDouble", "setFloat", "setInt", "setLong",
                "setNull", "setString", "setTime", "setTimestamp"});
        TARGET_EXECUTER = Arrays.asList(new String[]{"execute", "executeQuery", "executeUpdate", "executeBatch"});
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        boolean subclassOf = target.subclassOf(preparedStatement);
        boolean subtypeOf = target.subtypeOf(preparedStatement);

        return subclassOf || subtypeOf;
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        CtMethod[] methods = target.getDeclaredMethods();

        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            String methodName = method.getName();

            if (TARGET_SETTER.contains(methodName)) {
                method.addLocalVariable(VAR_NAME, sqlStackInfo);
                method.insertBefore("{ " + VAR_NAME + " = (" + SQL_STACK_INFO_CLASS_NAME + ")" + STACK_MANAGER_CLASS_NAME + ".getSql(); "
                        + VAR_NAME + ".addParam(($w)$1, ($w)$2); }");
            }
            else if (TARGET_EXECUTER.contains(methodName)) {
                method.addLocalVariable(VAR_NAME, sqlStackInfo);
                method.insertBefore("{ " + VAR_NAME + " = (" + SQL_STACK_INFO_CLASS_NAME + ")" + STACK_MANAGER_CLASS_NAME + ".getSql(); "
                        + getPopMessage(VAR_NAME) + " }");
            }
            else if(methodName.equals(getResultSet)) {
                method.addLocalVariable(VAR_NAME, sqlStackInfo);
                method.insertAfter("{ " + VAR_NAME + " = (" + SQL_STACK_INFO_CLASS_NAME + ")" + STACK_MANAGER_CLASS_NAME + ".getSql(); "
                        + VAR_NAME + ".initResultSet(($w)$_); }");
            }
        }
    }
}
