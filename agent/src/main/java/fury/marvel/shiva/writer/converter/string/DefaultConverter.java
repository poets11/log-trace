//package fury.marvel.shiva.writer.converter.string;
//
//import fury.marvel.shiva.trace.stack.StackInfo;
//
//import java.io.IOException;
//import java.io.Writer;
//
///**
// * Created by poets11 on 15. 2. 2..
// */
//public class DefaultConverter implements IndentConverter {
//    @Override
//    public void convert(StackInfo stackInfo, Writer writer, int depth) throws IOException {
//        writer.write(IndentConverterFactory.getIndentString(depth));
//        writer.write(stackInfo.toString());
//        writer.write("\n");
//    }
//}
