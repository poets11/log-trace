package fury.marvel.shiva.writer.converter.string;


import fury.marvel.shiva.trace.stack.StackInfo;
import fury.marvel.shiva.trace.stack.call.ConstructorStackInfo;
import fury.marvel.shiva.trace.stack.call.MethodStackInfo;
import fury.marvel.shiva.trace.stack.init.RequestStackInfo;
import fury.marvel.shiva.trace.stack.sql.SqlStackInfo;

/**
 * Created by poets11 on 15. 1. 29..
 */
public class IndentConverterFactory {
    private static IndentConverter requestConverter = new RequestConverter();
    private static IndentConverter methodConverter = new MethodConverter();
    private static IndentConverter constructorConverter = new ConstructorConverter();
    private static IndentConverter sqlConverter = new SqlConverter();
    private static IndentConverter defaultConverter = new DefaultConverter();

    public static IndentConverter getConverter(StackInfo stackInfo) {
        if (stackInfo instanceof RequestStackInfo) return requestConverter;
        else if (stackInfo instanceof MethodStackInfo) return methodConverter;
        else if (stackInfo instanceof ConstructorStackInfo) return constructorConverter;
        else if (stackInfo instanceof SqlStackInfo) return sqlConverter;
        else return defaultConverter;
    }

    public static String getIndentString(int depth) {
        return getIndentString(IndentConverter.DEFAULT_INDENT_CHAR, depth);
    }

    public static String getIndentString(String indent, int depth) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            builder.append(indent);
        }

        builder.append("|--> ");

        return builder.toString();
    }
}
