package fury.marvel.shiva.transformer.modifier.proxy;

import fury.marvel.shiva.transformer.modifier.ClassModifier;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * Created by poets11 on 15. 1. 30..
 */
public class ClassLoaderModifier extends ClassModifier {
    private CtClass classLoader;

    public ClassLoaderModifier() {
        init();
    }

    private void init() {
        classLoader = createCtClass(ClassLoader.class);
    }

    @Override
    protected boolean canModify(String className, CtClass target) throws Exception {
        boolean subclassOf = target.subclassOf(classLoader);
        boolean subtypeOf = target.subtypeOf(classLoader);

        return subclassOf || subtypeOf;
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        CtMethod[] methods = target.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            String methodName = method.getName();

            if(methodName.equals("findClass") || methodName.equals("loadClass")) {
                method.insertBefore("{ System.out.println(\"" + target.getName() + "." + methodName + "()\" + fury.marvel.shiva.trace.ObjectWriter.getObjectInfo($args)); }");
            } else {
//                method.insertBefore("{ System.out.println(\"" + target.getName() + "." + methodName + "() called\"); }");
            }
        }
    }
}
