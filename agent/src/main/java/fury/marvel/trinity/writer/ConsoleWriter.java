package fury.marvel.trinity.writer;

import fury.marvel.trinity.stack.info.RequestStackInfo;
import fury.marvel.trinity.stack.info.StackInfo;
import fury.marvel.trinity.writer.converter.Converter;
import fury.marvel.trinity.writer.converter.ConverterFactory;

import java.util.List;

/**
 * Created by poets11 on 15. 2. 2..
 */
public class ConsoleWriter implements Writer {
    @Override
    public void write(StackInfo stackInfo) {
        if(stackInfo != null && stackInfo instanceof RequestStackInfo) doWrite(stackInfo);
    }
    
    public void doWrite(StackInfo stackInfo) {
        Converter converter = ConverterFactory.getConverter(stackInfo);
        System.out.println(converter.convert(stackInfo));

        List<StackInfo> childStack = stackInfo.getChildStack();
        if (childStack != null) {
            for (int i = 0; i < childStack.size(); i++) {
                doWrite(childStack.get(i));
            }
        }
    }
}
