package fury.marvel.trinity.writer.converter.simple;

import fury.marvel.trinity.stack.info.SqlStackInfo;
import fury.marvel.trinity.stack.info.StackInfo;

/**
 * Created by poets11 on 15. 2. 12..
 */
public class SimpleSqlInfoConverter extends SimpleConverter {
    @Override
    public String convert(StackInfo stackInfo) {
        if (stackInfo == null) return "";

        SqlStackInfo sqlStackInfo = (SqlStackInfo) stackInfo;
        StringBuilder builder = new StringBuilder();
        String indentString = getIndentString(sqlStackInfo.getDepth());

        builder.append(indentString);
        builder.append(sqlStackInfo.getSql());

        return builder.toString();
    }
}
