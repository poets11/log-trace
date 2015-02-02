package fury.marvel.shiva.transformer.modifier;

import fury.marvel.shiva.trace.stack.ThreadStackManager;
import javassist.ClassPool;
import javassist.CtClass;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by poets11 on 15. 1. 28..
 */
public abstract class ClassModifier {
    public static final String STACK_MANAGER_CLASS_NAME = ThreadStackManager.class.getName();
    public static final String PUSH_MESSAGE = "{ " + STACK_MANAGER_CLASS_NAME + ".push(%s); }";
    public static final String POP_MESSAGE = "{ " + STACK_MANAGER_CLASS_NAME + ".pop(%s); }";
    public static final String VAR_NAME = "_stackInfo";

    protected ClassPool classPool;

    public ClassModifier() {
        classPool = ClassPool.getDefault();
    }

    protected abstract boolean canModify(String className, CtClass target) throws Exception;

    protected abstract void doModify(String className, CtClass target) throws Exception;

    protected CtClass createCtClass(Class clazz) {
        String resourcePath = clazz.getName().replaceAll("[.]", "/") + ".class";
        InputStream resourceAsStream = null;

        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            resourceAsStream = classLoader.getResourceAsStream(resourcePath);
            CtClass ctClass = classPool.makeClass(resourceAsStream);

            return ctClass;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (resourceAsStream != null) try {
                resourceAsStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected String getPushMessage(String varName) {
        return String.format(PUSH_MESSAGE, varName);
    }

    protected String getPopMessage(String varName) {
        return String.format(POP_MESSAGE, varName);
    }

    protected String getClassName(String className) {
        return className;
    }

    public void modify(String className, CtClass target) throws Exception {
        if (canModify(className, target)) doModify(className, target);
    }
}
