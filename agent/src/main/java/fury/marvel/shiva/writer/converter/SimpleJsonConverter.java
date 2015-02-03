package fury.marvel.shiva.writer.converter;

import fury.marvel.shiva.trace.ObjectConverter;
import fury.marvel.shiva.trace.stack.StackInfo;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by poets11 on 15. 2. 3..
 */
public class SimpleJsonConverter implements StackInfoConverter {
    @Override
    public void convert(StackInfo stackInfo, Writer writer) {
        try {
            writer.write(ObjectConverter.convert(stackInfo).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
