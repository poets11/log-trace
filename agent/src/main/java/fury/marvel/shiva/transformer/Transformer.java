package fury.marvel.shiva.transformer;

import fury.marvel.shiva.transformer.modifier.ClassModifier;
import fury.marvel.shiva.transformer.modifier.call.PackageBaseModifier;
import fury.marvel.shiva.transformer.modifier.init.HandlerAdaptorModifier;
import fury.marvel.shiva.transformer.modifier.sql.ConnectionModifier;
import fury.marvel.shiva.transformer.modifier.sql.PreparedStatementModifier;
import fury.marvel.shiva.transformer.modifier.sql.ResultSetModifier;
import javassist.ClassPool;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
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
    private List<ClassModifier> modifiers;
    private ClassPool classPool;

    public Transformer() {
        initModifiers();
        initClassPool();
    }

    private void initClassPool() {
        classPool = ClassPool.getDefault();
    }

    private void initModifiers() {
        modifiers = new ArrayList<ClassModifier>();
        modifiers.add(new PackageBaseModifier());
        modifiers.add(new HandlerAdaptorModifier());
        modifiers.add(new ConnectionModifier());
        modifiers.add(new PreparedStatementModifier());
        modifiers.add(new ResultSetModifier());
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        // skip classes that is loaded by system classloader
        if (loader == null) return classfileBuffer;
        else return doTransform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
    }

    private byte[] doTransform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] transformed = classfileBuffer;

        try {
            CtClass target = createCtClass(classfileBuffer);

            if (isTransformable(target)) {
                for (int i = 0; i < modifiers.size(); i++) {
                    ClassModifier classModifier = modifiers.get(i);
                    classModifier.modify(className, target);
                }

                transformed = target.toBytecode();
                target.detach();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transformed;
    }

    private boolean isTransformable(CtClass ctClass) {
        return ctClass != null && ctClass.isInterface() == false;
    }

    private CtClass createCtClass(byte[] classfileBuffer) throws IOException {
        ByteArrayInputStream classStream = new ByteArrayInputStream(classfileBuffer);
        return classPool.makeClass(classStream);
    }
}
