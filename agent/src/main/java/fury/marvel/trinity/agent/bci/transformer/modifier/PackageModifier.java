package fury.marvel.trinity.agent.bci.transformer.modifier;

import fury.marvel.trinity.agent.AgentConfig;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.impl.PackageStackInfoImpl;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class PackageModifier extends AbstractClassModifier {
    protected static final String PACKAGE_STACK_INFO = PackageStackInfoImpl.class.getName();
    protected static final String SET_PARAMS = VAR_NAME + ".setParams($args);";
    protected static final String SET_RESULT = VAR_NAME + ".setResult(($w)$_);";
    
    protected String basePackage;    
    protected List<String> ignoreMethods; // TODO set this from global config
    protected CtClass ctPackageStackInfo;

    public PackageModifier() throws IOException {
        init();
    }

    protected void init() throws IOException {
        ignoreMethods = new ArrayList<String>();
        ignoreMethods.add("toString");

        basePackage = AgentConfig.get(AgentConfig.PROP_BASE_PACKAGE).replaceAll("[.]", "/");
        ctPackageStackInfo = ctClassUtil.createCtClass(PackageStackInfoImpl.class);
    }

    @Override
    protected boolean canModify(String className, CtClass target) {
        return className.startsWith(basePackage);   // TODO change ant pattern matcher
    }

    @Override
    protected void doModify(String className, CtClass target) throws Exception {
        for (CtMethod ctMethod : target.getDeclaredMethods()) {
            if (ignoreMethods.contains(ctMethod.getName())) continue;
            else if(isAbstract(ctMethod)) continue;
            else setTraceMethodStack(className, ctMethod);
        }

        for (CtConstructor ctConstructor : target.getDeclaredConstructors()) {
            setTraceConstructorStack(className, ctConstructor);
        }
    }

    protected void setTraceConstructorStack(String className, CtConstructor ctConstructor) throws Exception {
        ctConstructor.addLocalVariable(VAR_NAME, ctPackageStackInfo);

        String beforeStatement = createStatementBlock(INIT_VAR_NULL,
                createStatementBlockWithStackManagerInit(createInitVarStatement(PACKAGE_STACK_INFO), SET_PARAMS, PUSH_MESSAGE));
        ctConstructor.insertBefore(beforeStatement);

        String afterStatement = createStatementBlockWithStackManagerInit(POP_MESSAGE);
        ctConstructor.insertAfter(afterStatement);
    }

    protected void setTraceMethodStack(String className, CtMethod ctMethod) throws Exception {
        ctMethod.addLocalVariable(VAR_NAME, ctPackageStackInfo);

        String beforeStatement = createStatementBlock(INIT_VAR_NULL,
                createStatementBlockWithStackManagerInit(createInitVarStatement(PACKAGE_STACK_INFO), SET_PARAMS, PUSH_MESSAGE));
        ctMethod.insertBefore(beforeStatement);

        String afterStatement = createStatementBlockWithStackManagerInit(SET_RESULT, POP_MESSAGE);
        ctMethod.insertAfter(afterStatement);
    }
}
