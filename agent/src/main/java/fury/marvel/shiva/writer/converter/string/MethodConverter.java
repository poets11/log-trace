package fury.marvel.shiva.writer.converter.string;

import fury.marvel.shiva.trace.stack.StackInfo;
import fury.marvel.shiva.trace.stack.call.MethodStackInfo;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by poets11 on 15. 2. 2..
 */
public class MethodConverter implements IndentConverter {
    @Override
    public void convert(StackInfo stackInfo, Writer writer, int depth) throws IOException {
        MethodStackInfo info = (MethodStackInfo) stackInfo;

        writer.write(IndentConverterFactory.getIndentString(depth));
        writer.write(info.getClassName() + "." + info.getMethodName() + "()");
        writer.write("\n");

        writer.write(IndentConverterFactory.getIndentString(depth));
        writer.write("Elapsed Time : " + info.getElapsedTime() + "ms");
        writer.write("\n");

        String[] paramValues = info.getParamValues();
        if (paramValues != null && paramValues.length > 0) {
            for (int i = 0; i < paramValues.length; i++) {
                writer.write(IndentConverterFactory.getIndentString(depth));
                writer.write("Param " + (i + 1) + " : " + paramValues[i]);
                writer.write("\n");
            }
        }

        writer.write(IndentConverterFactory.getIndentString(depth));
        writer.write("Return : " + info.getReturnValue());
        writer.write("\n");
    }
}
