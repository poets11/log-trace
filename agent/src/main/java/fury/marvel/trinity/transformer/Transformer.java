package fury.marvel.trinity.transformer;

import fury.marvel.trinity.transformer.modifier.*;
import javassist.CtClass;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class Transformer implements ClassFileTransformer {
    private CtClassUtil ctClassUtil = CtClassUtil.getInstance();
    private List<ClassModifier> modifiers;

    public Transformer() {
        initModifiers();
    }

    private void initModifiers() {
        modifiers = new ArrayList<ClassModifier>();

        try {
            modifiers.add(new HandlerAdapterModifier());
            modifiers.add(new PackageModifier());
            modifiers.add(new ConnectionModifier());
            modifiers.add(new StatementModifier());
            modifiers.add(new ResultSetModifier());
//            modifiers.add(new DispatchServletModifier());
        } catch (IOException e) {
            // TODO if modifier throws exception int init then skip init modifier
            e.printStackTrace();
        }
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        // the classloader is null that means the class is out of bci
        if (loader == null) return classfileBuffer;
        else return doTransform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
    }

    private byte[] doTransform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] transformed = classfileBuffer;
        CtClass target = null;

        try {
            target = ctClassUtil.createCtClass(loader, className, classfileBuffer);

            if (isTransformable(className, target)) {
                for (int i = 0; i < modifiers.size(); i++) {
                    ClassModifier classModifier = modifiers.get(i);
                    classModifier.modify(className, target);
                }
//                target.toClass(loader, protectionDomain);
                transformed = target.toBytecode();
                return transformed;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(target != null) target.detach();
        }

        return transformed;
    }

    private boolean isTransformable(String className, CtClass ctClass) {
        boolean notNull = ctClass != null;
        boolean notInterface = ctClass.isInterface() == false;
        boolean notProxy = className.indexOf("$") < 0;

        return notNull && notInterface && notProxy;
    }
}
