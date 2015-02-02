package fury.marvel.shiva.classloader;

import javassist.*;

import java.io.IOException;

/**
 * Created by poets11 on 15. 1. 30..
 */
public class ProxyClassLoader extends ClassLoader {
//    private CtClass member;

    public ProxyClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> aClass = super.loadClass(name);
//        return makeProxy(aClass);
        return aClass;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> aClass = super.loadClass(name, resolve);
        return makeProxy(aClass);
//        return aClass;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> aClass = super.findClass(name);
        return makeProxy(aClass);
//        return aClass;
    }

    private Class<?> makeProxy(Class<?> targetClass) {
        String className = targetClass.getName();
        if (className.equals("ces.common.member.model.Member")) {
            ClassPool classPool = ClassPool.getDefault();

            try {
                CtClass aClass = classPool.makeClass(super.getResourceAsStream(className.replaceAll("[.]", "/") + ".class"));

                aClass.defrost();
                CtClass innerModel = classPool.makeClass("InnerModel");
                innerModel.defrost();
                innerModel.setSuperclass(aClass);

                CtField field = CtField.make(className + " model = new " + className + "();", innerModel);
                innerModel.addField(field);

                CtMethod method = CtMethod.make("public void setName(String name) { System.out.println(\"inner.setName()\"); this.model.setName(name); }", innerModel);
                innerModel.addMethod(method);

                Class toClass = innerModel.toClass();

                return toClass;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }

        return targetClass;
    }
}
