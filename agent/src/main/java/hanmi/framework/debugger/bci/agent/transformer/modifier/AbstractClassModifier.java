package hanmi.framework.debugger.bci.agent.transformer.modifier;

import hanmi.framework.debugger.bci.agent.assist.CtClassUtil;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import java.io.IOException;

/**
 * Created by poets11 on 15. 2. 5..
 */
public abstract class AbstractClassModifier implements ClassModifier {
    protected CtClass ctException;
    protected CtClassUtil ctClassUtil = CtClassUtil.getInstance();

    protected AbstractClassModifier() throws IOException {
        ctException = ctClassUtil.createCtClass(Exception.class);
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

    protected boolean isAbstract(CtMethod ctMethod) {
        if (ctMethod == null) return true;
        else return Modifier.isAbstract(ctMethod.getModifiers());
    }

    protected abstract boolean canModify(String className, CtClass target) throws Exception;

    protected abstract void doModify(String className, CtClass target) throws Exception;

    @Override
    public void modify(String className, CtClass target) throws Exception {
        if (canModify(className, target)) doModify(className, target);
    }
}
