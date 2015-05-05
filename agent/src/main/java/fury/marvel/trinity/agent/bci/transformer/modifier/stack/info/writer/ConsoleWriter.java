package fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.writer;

import fury.marvel.trinity.agent.AgentConfig;
import fury.marvel.trinity.agent.bci.transformer.CtClassUtil;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.StackInfo;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.TraceLevel;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.writer.converter.Converter;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.writer.converter.ConverterFactory;
import javassist.CtClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by poets11 on 15. 2. 2..
 */
public class ConsoleWriter implements Writer {
    private Object logger;
    private Method info;
    private StringWriter writer = new StringWriter();
    
    public ConsoleWriter(Object logger, Method info) {
        this.logger = logger;
        this.info = info;
    }
    
    @Override
    public void write(StackInfo stackInfo) {
        TraceLevel traceLevel = AgentConfig.getTraceLevel();
        boolean writeable = stackInfo.isWriteable(traceLevel);

        if (writeable) {
            doWrite(stackInfo);

            try {
                if(this.info != null) {
                    info.invoke(logger, writer.getBuffer().toString());
                } else {
                    System.out.println(writer.getBuffer().toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void doWrite(StackInfo stackInfo) {
        Converter converter = ConverterFactory.getConverter(stackInfo);
        
        writer.write(converter.convert(stackInfo));

        List<StackInfo> childStack = stackInfo.getChildStack();
        if (childStack != null) {
            for (int i = 0; i < childStack.size(); i++) {
                doWrite(childStack.get(i));
            }
        }
    }
}
