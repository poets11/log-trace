package fury.marvel.trinity.writer.converter.simple;

import fury.marvel.trinity.stack.info.StackInfo;
import fury.marvel.trinity.writer.converter.Converter;

/**
 * Created by poets11 on 15. 2. 6..
 */
public class SimpleConverter implements Converter {
    public String getIndentString(int depth) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i <= depth; i++) sb.append("|--");
        sb.append("> ");

        return sb.toString();
    }
    
    @Override
    public String convert(StackInfo stackInfo) {
        if (stackInfo == null) return "";
        
        return getIndentString(stackInfo.getDepth()) + stackInfo.toString();
    }
}
