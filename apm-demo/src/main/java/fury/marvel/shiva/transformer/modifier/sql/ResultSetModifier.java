package fury.marvel.shiva.transformer.modifier.sql;

import fury.marvel.shiva.trace.stack.sql.SqlStackInfo;
import fury.marvel.shiva.trace.stack.sql.SqlStackInfoImpl;
import fury.marvel.shiva.transformer.modifier.ClassModifier;
import javassist.CtClass;
import javassist.CtMethod;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

/**
 * Created by poets11 on 15. 2. 2..
 */
public class ResultSetModifier extends ClassModifier {
    public static final String SQL_STACK_INFO_CLASS_NAME = SqlStackInfoImpl.class.getName();

    private CtClass sqlStackInfo;
    private List<String> targetGetter;
    private CtClass resultSet;

    public ResultSetModifier() {
        init();
    }

    private void init() {
        resultSet = createCtClass(ResultSet.class);
        sqlStackInfo = createCtClass(SqlStackInfoImpl.class);
        targetGetter = Arrays.asList(new String[]{"getBigDecimal", "getBoolean", "getDate", "getDouble",
                "getFloat", "getInt", "getLong", "getString", "getTime", "getTimestamp"});
    }


    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        boolean subclassOf = target.subclassOf(resultSet);
        boolean subtypeOf = target.subtypeOf(resultSet);
        boolean notProxy = className.indexOf("$") < 0;

        return (subclassOf || subtypeOf) && notProxy;
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        CtMethod[] methods = target.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            String methodName = method.getName();

            if(targetGetter.contains(methodName)) {
                CtClass[] parameterTypes = method.getParameterTypes();

                if("java.lang.String".equals(parameterTypes[0].getName())) {
                    method.addLocalVariable(VAR_NAME, sqlStackInfo);
                    method.insertAfter("{ " + VAR_NAME + " = (" + SQL_STACK_INFO_CLASS_NAME + ")" + STACK_MANAGER_CLASS_NAME + ".getSql(); "
                            + VAR_NAME + ".addResultValue(($w)$1, ($w)$_); }");
                }
            } else if(methodName.equals("next")) {
                method.addLocalVariable(VAR_NAME, sqlStackInfo);
                method.insertAfter("{ " + VAR_NAME + " = (" + SQL_STACK_INFO_CLASS_NAME + ")" + STACK_MANAGER_CLASS_NAME + ".getSql(); "
                        + VAR_NAME + ".next($_); }");
            }
        }
    }
}
