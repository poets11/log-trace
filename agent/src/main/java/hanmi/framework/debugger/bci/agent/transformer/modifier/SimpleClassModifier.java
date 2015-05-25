package hanmi.framework.debugger.bci.agent.transformer.modifier;

import hanmi.framework.debugger.bci.agent.config.AgentConfig;
import hanmi.framework.debugger.bci.agent.stack.CallStack;
import javassist.*;

import java.io.IOException;

/**
 * Created by poets11 on 15. 5. 18..
 */
public class SimpleClassModifier implements ClassModifier {
    protected String basePackage;

    public SimpleClassModifier() {
        this.basePackage = AgentConfig.get(AgentConfig.PROP_BASE_PACKAGE).replaceAll("[.]", "/");
    }

    protected boolean isAbstract(CtMethod ctMethod) {
        if (ctMethod == null) return true;
        else return Modifier.isAbstract(ctMethod.getModifiers());
    }

    @Override
    public void modify(String className, CtClass target) throws Exception {
        if (className.startsWith(basePackage) == false) return;

        AgentConfig.log("MOD : " + className);
        for (CtMethod ctMethod : target.getDeclaredMethods()) {
            if (isAbstract(ctMethod) == false) {
                setTraceMethodStack(className, ctMethod);
            }
        }

        for (CtConstructor ctConstructor : target.getDeclaredConstructors()) {
            setTraceConstructorStack(className, ctConstructor);
        }
    }

    private void setTraceConstructorStack(String className, CtConstructor ctMethod) throws CannotCompileException {
        if (className.contains("model")) {
            String callStack = CallStack.class.getName();

            String afterStatement = String.format("{ %s.push(this.getClass(), \"%s\", $args); %s.pop(this.getClass(), \"%s\"); }",
                    callStack, ctMethod.getName(), callStack, ctMethod.getName());
            ctMethod.insertAfter(afterStatement);
        }

    }

    private void setTraceMethodStack(String className, CtMethod ctMethod) throws IOException, CannotCompileException, NotFoundException {
        if (ctMethod.getName().equals("toString")) {
            return;
        }

        String callStack = CallStack.class.getName();

        String beforeStatement = String.format("{ %s.push(this.getClass(), \"%s\", $args); }", callStack, ctMethod.getName());
        ctMethod.insertBefore(beforeStatement);

        CtClass returnType = ctMethod.getReturnType();
        String afterStatement = null;
        if (returnType.getName().equals("void")) {
            afterStatement = String.format("{ %s.pop(this.getClass(), \"%s\"); }", callStack, ctMethod.getName());
        } else {
            afterStatement = String.format("{ %s.pop(this.getClass(), \"%s\", ($w)$_); }", callStack, ctMethod.getName());
        }

        ctMethod.insertAfter(afterStatement);

    }
}
