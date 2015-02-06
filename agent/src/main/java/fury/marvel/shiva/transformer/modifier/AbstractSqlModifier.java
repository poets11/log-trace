package fury.marvel.shiva.transformer.modifier;

import fury.marvel.shiva.stack.info.impl.SqlStackInfoImpl;
import javassist.CtClass;

import java.io.IOException;

/**
 * Created by poets11 on 15. 2. 6..
 */
public abstract class AbstractSqlModifier extends AbstractClassModifier {
    protected static final String SQL_STACK_INFO = SqlStackInfoImpl.class.getName();
    protected static final String SET_SQL = VAR_NAME + ".setSql($1);";
    protected static final String SET_RESULT = VAR_NAME + ".setResult(($w)$_);";

    protected CtClass ctSqlStackInfo;

    public AbstractSqlModifier() throws IOException {
        ctSqlStackInfo = ctClassUtil.createCtClass(SqlStackInfoImpl.class);
    }
}
