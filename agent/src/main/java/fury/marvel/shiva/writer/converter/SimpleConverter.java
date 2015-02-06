package fury.marvel.shiva.writer.converter;

import fury.marvel.shiva.stack.info.StackInfo;

/**
 * Created by poets11 on 15. 2. 6..
 */
public class SimpleConverter implements Converter {
    @Override
    public String convert(StackInfo stackInfo) {
        return stackInfo != null ? stackInfo.toString() : "";
    }
}
