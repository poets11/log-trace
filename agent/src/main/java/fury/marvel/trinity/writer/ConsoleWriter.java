package fury.marvel.trinity.writer;

import fury.marvel.trinity.agent.AgentConfig;
import fury.marvel.trinity.stack.info.StackInfo;
import fury.marvel.trinity.stack.info.TraceLevel;
import fury.marvel.trinity.stack.info.impl.AbstractStackInfo;
import fury.marvel.trinity.writer.converter.Converter;
import fury.marvel.trinity.writer.converter.ConverterFactory;

import java.util.List;

/**
 * Created by poets11 on 15. 2. 2..
 */
public class ConsoleWriter implements Writer {
    @Override
    public void write(StackInfo stackInfo) {
        TraceLevel traceLevel = AgentConfig.getTraceLevel();
        boolean writeable = stackInfo.isWriteable(traceLevel);
        
        if(writeable) doWrite(stackInfo);
    }

    public void doWrite(StackInfo stackInfo) {
        Converter converter = ConverterFactory.getConverter(stackInfo);
        System.out.print(converter.convert(stackInfo));

        List<StackInfo> childStack = stackInfo.getChildStack();
        if (childStack != null) {
            for (int i = 0; i < childStack.size(); i++) {
                doWrite(childStack.get(i));
            }
        }
    }
}
