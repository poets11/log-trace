package fury.marvel.trinity.writer.converter.simple;

import fury.marvel.trinity.stack.info.PackageStackInfo;
import fury.marvel.trinity.stack.info.StackInfo;

/**
 * Created by poets11 on 15. 2. 12..
 */
public class SimplePackageInfoConverter extends SimpleConverter {
    @Override
    public String convert(StackInfo stackInfo) {
        if (stackInfo == null) return "";

        PackageStackInfo packageStackInfo = (PackageStackInfo) stackInfo;
        String indentString = getIndentString(packageStackInfo.getDepth());
        StringBuilder builder = new StringBuilder();

        String className = packageStackInfo.getClassName();
        String methodName = packageStackInfo.getMethodName();
        
        builder.append(indentString);
        builder.append(className.substring(className.lastIndexOf(".") + 1));
        builder.append(".");
        builder.append(methodName);
        builder.append("();");
        
        return builder.toString();
    }
}
