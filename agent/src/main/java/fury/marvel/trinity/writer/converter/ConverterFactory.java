package fury.marvel.trinity.writer.converter;

import fury.marvel.trinity.stack.info.PackageStackInfo;
import fury.marvel.trinity.stack.info.RequestStackInfo;
import fury.marvel.trinity.stack.info.SqlStackInfo;
import fury.marvel.trinity.stack.info.StackInfo;
import fury.marvel.trinity.writer.converter.simple.SimpleConverter;
import fury.marvel.trinity.writer.converter.simple.SimplePackageInfoConverter;
import fury.marvel.trinity.writer.converter.simple.SimpleRequestInfoConverter;
import fury.marvel.trinity.writer.converter.simple.SimpleSqlInfoConverter;

/**
* Created by poets11 on 15. 1. 28..
*/
public class ConverterFactory {
    public static Converter getConverter(StackInfo source) {
        if(source == null) return new SimpleConverter();
        else if(source instanceof RequestStackInfo) return new SimpleRequestInfoConverter();
        else if(source instanceof PackageStackInfo) return new SimplePackageInfoConverter();
        else if(source instanceof SqlStackInfo) return new SimpleSqlInfoConverter();
        else return new SimpleConverter();
    }
}
