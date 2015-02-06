package fury.marvel.shiva.stack.info.impl;

import fury.marvel.shiva.stack.info.PackageStackInfo;
import fury.marvel.shiva.stack.info.marshall.DefaultMarshaller;
import fury.marvel.shiva.stack.info.marshall.StringObject;

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
            params.add(DefaultMarshaller.marshall(args[i]));
        }
    }

    public void setResult(Object result) {
        this.result = DefaultMarshaller.marshall(result);
    }

    @Override
    public String toString() {
        return "PackageStackInfoImpl{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}