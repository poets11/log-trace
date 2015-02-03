package fury.marvel.shiva.transformer.modifier.call;

import fury.marvel.shiva.trace.stack.call.ConstructorStackInfo;
import fury.marvel.shiva.trace.stack.call.MethodStackInfo;
import fury.marvel.shiva.transformer.modifier.ClassModifier;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class PackageBaseModifier extends ClassModifier {
    public static final String METHOD_STACK_INFO_CLASS_NAME = MethodStackInfo.class.getName();
    public static final String CONSTRUCTOR_STACK_INFO_CLASS_NAME = ConstructorStackInfo.class.getName();

    public static final String METHOD_TRACE_OPEN_MESSAGE = "{ " + VAR_NAME + " = new " + METHOD_STACK_INFO_CLASS_NAME + "(); "
            + VAR_NAME + ".setClassName(\"%s\"); " + VAR_NAME + ".setMethodName(\"%s\"); "
            + VAR_NAME + ".setParamValues($args); " + VAR_NAME + ".setStart(System.currentTimeMillis()); }";
    public static final String METHOD_TRACE_CLOSE_MESSAGE = "{ " + VAR_NAME + ".setEnd(System.currentTimeMillis()); "
            + VAR_NAME + ".setReturnValue(($w)$_); }";
    public static final String CONSTRUCTOR_TRACE_OPEN_MESSAGE = "{ " + VAR_NAME + "= new " + CONSTRUCTOR_STACK_INFO_CLASS_NAME + "(); "
            + VAR_NAME + ".setClassName(\"%s\"); " + VAR_NAME + ".setMethodName(\"%s\"); "
            + VAR_NAME + ".setParamValues($args); " + VAR_NAME + ".setStart(System.currentTimeMillis()); }";
    public static final String CONSTRUCTOR_TRACE_CLOSE_MESSAGE = "{ " + VAR_NAME + ".setEnd(System.currentTimeMillis()); }";

    private String basePackage = "ces";
    private List<String> ignoreMethods;
    private CtClass methodStackInfo;
    private CtClass constructorStackInfo;

    public PackageBaseModifier() {
        init();
    }



    private void init() {
        basePackage = "ces";

        ignoreMethods = new ArrayList<String>();
        ignoreMethods.add("toString");

        methodStackInfo = createCtClass(MethodStackInfo.class);
        constructorStackInfo = createCtClass(ConstructorStackInfo.class);
    }

    @Override
    protected boolean canModify(String className, CtClass target) {
        boolean correctPackage = className.startsWith(basePackage);
        boolean innerClass = className.indexOf("$") > -1;

        return correctPackage && innerClass == false;
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        for (CtMethod ctMethod : target.getDeclaredMethods()) {
            if (ignoreMethods.contains(ctMethod.getName())) continue;
            else setTraceMethodStack(className, ctMethod);
        }

        ;
        for (CtConstructor ctConstructor : target.getDeclaredConstructors()) {
            setTraceConstructorStack(className, ctConstructor);
        }
    }

    private void setTraceConstructorStack(String className, CtConstructor ctConstructor) throws Exception {
        ctConstructor.addLocalVariable(VAR_NAME, constructorStackInfo);
        ctConstructor.insertBefore(getPushMessage(VAR_NAME));
        ctConstructor.insertBefore(String.format(CONSTRUCTOR_TRACE_OPEN_MESSAGE, getClassName(className), ctConstructor.getName()));
        ctConstructor.insertAfter(CONSTRUCTOR_TRACE_CLOSE_MESSAGE);
        ctConstructor.insertAfter(getPopMessage(VAR_NAME));
    }

    private void setTraceMethodStack(String className, CtMethod ctMethod) throws Exception {
        ctMethod.addLocalVariable(VAR_NAME, methodStackInfo);
        ctMethod.insertBefore(getPushMessage(VAR_NAME));
        ctMethod.insertBefore(String.format(METHOD_TRACE_OPEN_MESSAGE, getClassName(className), ctMethod.getName()));
        ctMethod.insertAfter(METHOD_TRACE_CLOSE_MESSAGE);
        ctMethod.insertAfter(getPopMessage(VAR_NAME));
    }
}
