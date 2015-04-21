package fury.marvel.trinity.transformer;

import fury.marvel.trinity.transformer.modifier.*;
import javassist.CtClass;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class Transformer implements ClassFileTransformer {
    private Logger logger = Logger.getLogger(getClass().getName());
    
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
            modifiers.add(new PreparedStatementModifier());
            modifiers.add(new ResultSetModifier());
        } 
        catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (loader == null) {
            return classfileBuffer;
        }
        else {
            byte[] transform = doTransform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
            return transform;
        }
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
                transformed = target.toBytecode();
                return transformed;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            if(target != null) {
                target.detach();
            }
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
