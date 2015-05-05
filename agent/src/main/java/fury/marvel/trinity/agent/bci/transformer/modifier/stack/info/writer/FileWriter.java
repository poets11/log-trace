package fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.writer;

import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.RequestStackInfo;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.StackInfo;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.writer.converter.Converter;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.writer.converter.ConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by poets11 on 15. 2. 12..
 */
public class FileWriter implements Writer {
    private Logger logger = Logger.getLogger(getClass().getName());
    private java.io.FileWriter writer;

    public FileWriter() {
        try {
            this.writer = new java.io.FileWriter("/Users/poets11/temp/trace.log", true);
        } 
        catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void write(StackInfo stackInfo) {
        if (stackInfo != null && stackInfo instanceof RequestStackInfo) {
            try {
                doWrite(stackInfo);
            } 
            catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
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
