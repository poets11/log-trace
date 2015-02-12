package fury.marvel.trinity.writer.converter.simple;

import fury.marvel.trinity.stack.info.RequestStackInfo;
import fury.marvel.trinity.stack.info.StackInfo;

/**
 * Created by poets11 on 15. 2. 12..
 */
public class SimpleRequestInfoConverter extends SimpleConverter {
    @Override
    public String convert(StackInfo stackInfo) {
        if(stackInfo == null) return "";

        RequestStackInfo reqStackInfo = (RequestStackInfo) stackInfo;
        StringBuilder builder = new StringBuilder();
        String indentString = getIndentString(reqStackInfo.getDepth());

        builder.append(indentString);
        builder.append(reqStackInfo.getUrl());
        builder.append("(");
        builder.append(reqStackInfo.getMethod());
        builder.append(")");
        
        return builder.toString();
    }
}
