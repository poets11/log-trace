package fury.marvel.shiva.writer.converter;

import fury.marvel.shiva.trace.stack.StackInfo;

import java.io.Writer;

/**
 * Created by poets11 on 15. 2. 2..
 */
public interface StackInfoConverter {
    void convert(StackInfo stackInfo, Writer writer);
}
