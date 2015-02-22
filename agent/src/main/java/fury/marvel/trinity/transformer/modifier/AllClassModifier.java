package fury.marvel.trinity.transformer.modifier;

import fury.marvel.trinity.stack.info.impl.PackageStackInfoImpl;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.IOException;

/**
 * Created by poets11 on 15. 2. 16..
 */
public class AllClassModifier extends AbstractClassModifier {
    protected static final String PACKAGE_STACK_INFO = PackageStackInfoImpl.class.getName();
    private CtClass ctPackageStackInfo;

    public AllClassModifier() throws IOException {
        ctPackageStackInfo = ctClassUtil.createCtClass(PackageStackInfoImpl.class);
    }

    @Override
    protected boolean canModify(String className, CtClass target) {
        return className.startsWith("org/spring");
    }

    @Override
    protected void doModify(String className, CtClass target) {
        try {
            CtMethod[] methods = target.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                CtMethod ctMethod = methods[i];

//                ctMethod.addLocalVariable(VAR_NAME, ctPackageStackInfo);

                String beforeStatement = createStatementBlock(createSysoutCode(className + "." + ctMethod.getName() + "()"));
//                String beforeStatement = createStatementBlock(createInitVarStatement(PACKAGE_STACK_INFO), PUSH_MESSAGE);
                ctMethod.insertBefore(beforeStatement);

//                String afterStatement = createStatementBlock(POP_MESSAGE);
//                ctMethod.insertAfter(afterStatement);
            }
        } catch (CannotCompileException e) {
//            System.out.println("__ " + className);
//            e.printStackTrace();
        }
    }
}
