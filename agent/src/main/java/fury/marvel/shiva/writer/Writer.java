package fury.marvel.shiva.writer;

import fury.marvel.shiva.stack.info.StackInfo;
import fury.marvel.shiva.writer.converter.Converter;

/**
 * Created by poets11 on 15. 2. 6..
 */
public interface Writer {
    void setConverter(Converter converter);
    void write(StackInfo stackInfo);
}
