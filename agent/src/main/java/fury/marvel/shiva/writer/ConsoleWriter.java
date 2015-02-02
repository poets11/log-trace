package fury.marvel.shiva.writer;

import fury.marvel.shiva.trace.stack.StackInfo;
import fury.marvel.shiva.writer.converter.ConverterFactory;
import fury.marvel.shiva.writer.converter.StackInfoConverter;

import java.io.StringWriter;

/**
 * Created by poets11 on 15. 2. 2..
 */
public class ConsoleWriter {
    public static void write(StackInfo stackInfo) {
        StringWriter writer = new StringWriter();

        StackInfoConverter converter = ConverterFactory.getStackInfoConverter();
        converter.convert(stackInfo, writer);

        StringBuffer buffer = writer.getBuffer();
        System.out.println(buffer);
    }
}
