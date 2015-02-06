package fury.marvel.shiva.writer;

import fury.marvel.shiva.stack.info.StackInfo;
import fury.marvel.shiva.writer.converter.Converter;

import java.util.List;

/**
 * Created by poets11 on 15. 2. 2..
 */
public class ConsoleWriter implements Writer {
    private Converter converter;

    @Override
    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    @Override
    public void write(StackInfo stackInfo) {
        write(stackInfo, 1);
    }

    private void write(StackInfo stackInfo, int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("|--");
        }
        System.out.print(">");

        System.out.println(converter.convert(stackInfo));

        List<StackInfo> childStack = stackInfo.getChildStack();
        if (childStack != null) {
            for (int i = 0; i < childStack.size(); i++) {
                StackInfo info = childStack.get(i);
                write(info, depth + 1);
            }
        }
    }
}
