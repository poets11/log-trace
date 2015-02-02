package fury.marvel.shiva.writer.converter;

import fury.marvel.shiva.trace.stack.StackInfo;
import fury.marvel.shiva.writer.converter.string.IndentConverter;
import fury.marvel.shiva.writer.converter.string.IndentConverterFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class SimpleIndentConverter implements StackInfoConverter {
    @Override
    public void convert(StackInfo stackInfo, Writer writer) {
        try {
            doConvert(stackInfo, writer, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doConvert(StackInfo stackInfo, Writer writer, int depth) throws IOException {
        IndentConverter indentConverter = IndentConverterFactory.getConverter(stackInfo);
        indentConverter.convert(stackInfo, writer, depth);

        List<StackInfo> child = stackInfo.getChild();
        if (child != null && child.size() > 0) {
            for (int i = 0; i < child.size(); i++) {
                StackInfo info = child.get(i);
                doConvert(info, writer, depth + 1);
            }
        }
    }
}
