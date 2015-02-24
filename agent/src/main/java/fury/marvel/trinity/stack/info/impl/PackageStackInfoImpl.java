package fury.marvel.trinity.stack.info.impl;

import fury.marvel.trinity.stack.info.PackageStackInfo;
import fury.marvel.trinity.stack.info.marshall.JacksonMarshaller;
import fury.marvel.trinity.stack.info.marshall.MarshallerFactory;
import fury.marvel.trinity.stack.info.marshall.StringObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by poets11 on 15. 2. 4..
 */
public class PackageStackInfoImpl extends AbstractStackInfo implements PackageStackInfo {
    private String className;
    private String methodName;
    private List<StringObject> params;
    private StringObject result;

    public PackageStackInfoImpl() {
        StackTraceElement currentStack = new Throwable().getStackTrace()[1];
        setClassName(currentStack.getClassName());
        setMethodName(currentStack.getMethodName());
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public List<StringObject> getParams() {
        return params;
    }

    @Override
    public StringObject getResult() {
        return result;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParams(Object[] args) {
        if(args == null) return;

        params = new ArrayList<StringObject>();
        for (int i = 0; i < args.length; i++) {
            params.add(MarshallerFactory.getMarshaller().marshall(args[i]));
        }
    }

    public void setResult(Object result) {
        this.result = MarshallerFactory.getMarshaller().marshall(result);
    }

    @Override
    public String toString() {
        return className.substring(className.lastIndexOf(".") + 1) + "." + methodName + "()";
    }
}
