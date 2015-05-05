package fury.marvel.trinity.agent.bci.transformer.modifier.reflect;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by poets11 on 15. 2. 13..
 */
public class Method<T> {
    private String name;
    private String[] paramTypeNames;

    public Method() {

    }

    public Method(String name, String... paramTypeNames) {
        this.name = name;
        this.paramTypeNames = paramTypeNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getParamTypeNames() {
        return paramTypeNames;
    }

    public void setParamTypeNames(String ... paramTypeNames) {
        this.paramTypeNames = paramTypeNames;
    }

    public boolean isEqualCtMethod(CtMethod method) throws NotFoundException {
        if (method == null) return false;
        if (method.getName().equals(name) == false) return false;

        CtClass[] parameterTypes = method.getParameterTypes();
        if (paramTypeNames != null && parameterTypes != null) {
            if (paramTypeNames.length != parameterTypes.length) return false;

            for (int i = 0; i < paramTypeNames.length; i++) {
                String expectedTypeName = paramTypeNames[i];
                String realTypeName = parameterTypes[i].getName();

                if (!expectedTypeName.equals(realTypeName)) return false;
            }
        }

        if (paramTypeNames == null) {
            if (parameterTypes != null && parameterTypes.length != 0) return false;
        }

        return true;
    }

    public T invoke(Object target, Object... arguments) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (target == null) return null;

        Class[] paramsTypeClasses = createParamTypeClasses();
        java.lang.reflect.Method method = target.getClass().getDeclaredMethod(name, paramsTypeClasses);
        Object invoke = method.invoke(target, arguments);

        if (invoke == null) return null;
        else return (T) invoke;
    }

    private Class[] createParamTypeClasses() throws ClassNotFoundException {
        if (paramTypeNames == null || paramTypeNames.length == 0) return null;

        Class[] classes = new Class[paramTypeNames.length];
        for (int i = 0; i < paramTypeNames.length; i++) {
            String paramTypeName = paramTypeNames[i];
            classes[i] = Class.forName(paramTypeName);
        }

        return classes;
    }

    @Override
    public String toString() {
        return "TargetMethod{" +
                "name='" + name + '\'' +
                ", paramTypeNames=" + Arrays.toString(paramTypeNames) +
                '}';
    }
}
