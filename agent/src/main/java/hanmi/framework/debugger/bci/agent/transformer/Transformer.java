package hanmi.framework.debugger.bci.agent.transformer;

import hanmi.framework.debugger.bci.agent.assist.CtClassUtil;
import hanmi.framework.debugger.bci.agent.config.AgentConfig;
import hanmi.framework.debugger.bci.agent.transformer.modifier.ClassModifier;
import hanmi.framework.debugger.bci.agent.transformer.modifier.SimpleClassModifier;
import hanmi.framework.debugger.bci.agent.transformer.modifier.SimpleHandlerAdapterModifier;
import hanmi.framework.debugger.bci.agent.transformer.modifier.SimpleConnectionModifier;
import hanmi.framework.debugger.bci.agent.transformer.modifier.SimplePreparedStatementModifier;
import hanmi.framework.debugger.bci.agent.transformer.modifier.SimpleResultSetModifier;
import javassist.CtClass;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
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
        try {
            modifiers = new ArrayList<ClassModifier>();
            modifiers.add(new SimpleConnectionModifier());
            modifiers.add(new SimplePreparedStatementModifier());
            modifiers.add(new SimpleResultSetModifier());
            modifiers.add(new SimpleClassModifier());
            modifiers.add(new SimpleHandlerAdapterModifier());
        } catch (IOException e) {
            AgentConfig.error(e);
        }
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (loader == null) {
            return classfileBuffer;
        } else {
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
            AgentConfig.error(e);
        } finally {
            if (target != null) {
                target.detach();
            }
        }

        return transformed;
    }

    private boolean isTransformable(String className, CtClass ctClass) {
        boolean notNull = ctClass != null;
        boolean notInterface = ctClass.isInterface() == false;
        boolean notProxy = className.indexOf("$") < 0 && className.indexOf("Proxy") < 0;

        return notNull && notInterface && notProxy;
    }
}
