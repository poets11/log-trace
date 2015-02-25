package fury.marvel.trinity.writer.converter.simple;

import fury.marvel.trinity.stack.info.PackageStackInfo;
import fury.marvel.trinity.stack.info.StackInfo;
import fury.marvel.trinity.stack.info.marshall.StringObject;

import java.util.List;

/**
 * Created by poets11 on 15. 2. 12..
 */
public class SimplePackageInfoConverter extends SimpleConverter {
    private final String lineClassName = "%s %s.%s(); - %sms\n";

    @Override
    public String convert(StackInfo stackInfo) {
        if (stackInfo == null) return "";

        PackageStackInfo packageStackInfo = (PackageStackInfo) stackInfo;
        String indentString = getIndentString(packageStackInfo.getDepth());
        StringBuilder builder = new StringBuilder();

        String className = packageStackInfo.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        String methodName = packageStackInfo.getMethodName();

        builder.append(String.format(lineNew, indentString));
        builder.append(String.format(lineClassName, indentString, className, methodName, packageStackInfo.getElapsedTime()));

        List<StringObject> params = packageStackInfo.getParams();
        if(params != null && params.size() > 0) {
            builder.append(String.format(lineKeyValue, indentString, "Params", params));
        }

        StringObject result = packageStackInfo.getResult();
        if(result != null && result.getType() != null) {
            builder.append(String.format(lineKeyValue, indentString, "Return", result));
        }

        return builder.toString();
    }
}
