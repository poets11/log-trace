package fury.marvel.trinity.writer;

import fury.marvel.trinity.stack.info.RequestStackInfo;
import fury.marvel.trinity.stack.info.StackInfo;
import fury.marvel.trinity.writer.converter.Converter;
import fury.marvel.trinity.writer.converter.ConverterFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by poets11 on 15. 2. 12..
 */
public class FileWriter implements Writer {
    private java.io.FileWriter writer;

    public FileWriter() {
        try {
            this.writer = new java.io.FileWriter("/Users/poets11/temp/trace.log", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(StackInfo stackInfo) {
        if (stackInfo != null && stackInfo instanceof RequestStackInfo) try {
            doWrite(stackInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doWrite(StackInfo stackInfo) throws IOException {
        Converter converter = ConverterFactory.getConverter(stackInfo);
        writer.write(converter.convert(stackInfo));
        writer.write("\n");
        writer.flush();

        List<StackInfo> childStack = stackInfo.getChildStack();
        if (childStack != null) {
            for (int i = 0; i < childStack.size(); i++) {
                doWrite(childStack.get(i));
            }
        }
    }
}
