package fury.marvel.trinity.transformer.modifier;

import fury.marvel.trinity.stack.StackManager;
import fury.marvel.trinity.transformer.CtClassUtil;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;

/**
 * Created by poets11 on 15. 2. 5..
 */
public abstract class AbstractClassModifier implements ClassModifier {
    public static final String STACK_MANAGER = StackManager.class.getName();
    public static final String VAR_NAME = "_stackInfo_";
    public static final String INIT_VAR_STATEMENT = VAR_NAME + " = new %s();";
    public static final String PEEK_VAR_STATEMENT = VAR_NAME + " = (%s)" + STACK_MANAGER + ".peek();";

    public static final String PUSH_MESSAGE = STACK_MANAGER + ".push(" + VAR_NAME + ");";
    public static final String POP_MESSAGE = STACK_MANAGER + ".pop(" + VAR_NAME + ");";
    public static final String EXCEPTION_CATCH_MESSAGE = STACK_MANAGER + ".catchException($e);";
//    public static final String PEEK_MESSAGE = STACK_MANAGER + ".peek();";

    protected CtClass ctException;
    protected CtClassUtil ctClassUtil = CtClassUtil.getInstance();
    
    protected AbstractClassModifier() throws IOException {
        ctException = ctClassUtil.createCtClass(java.lang.Exception.class);
    }

    protected boolean isInstanceOfInterface(CtClass target, String interfaceName) throws NotFoundException {
        if (target == null) return false;

        CtClass[] interfaces = target.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            CtClass anInterface = interfaces[i];

            if (interfaceName.equals(anInterface.getName())) return true;
            else if (isInstanceOfInterface(anInterface, interfaceName)) return true;
        }

        return isInstanceOfInterface(target.getSuperclass(), interfaceName);
    }

    protected String createPeekVarStatement(String expectClassName) {
        return String.format(PEEK_VAR_STATEMENT, expectClassName);
    }

    protected String createSysoutCode(String message) {
        return String.format("System.out.println(\"" + message + "\");");
    }

    protected String createInitVarStatement(String stackInfoClassName) {
        return String.format(INIT_VAR_STATEMENT, stackInfoClassName);
    }

    protected String createStatementBlock(String... statements) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        for (int i = 0; i < statements.length; i++) {
            builder.append(" ");
            builder.append(statements[i]);
            builder.append(" ");
        }

        builder.append("}");

        return builder.toString();
    }

    protected abstract boolean canModify(String className, CtClass target) throws Exception;

    protected abstract void doModify(String className, CtClass target) throws Exception;

    @Override
    public void modify(String className, CtClass target) throws Exception {
        if (canModify(className, target)) doModify(className, target);
    }
}
