package fury.marvel.shiva.trace.stack.call;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fury.marvel.shiva.trace.ObjectConverter;
import fury.marvel.shiva.trace.stack.StackInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class MethodStackInfo implements StackInfo {
    private long start;
    private long end;
    private long elapsedTime;
    private String className;
    private String methodName;
    private Object[] paramValues;
    private Object returnValue;
    @JsonIgnore
    private StackInfo parent;
    private List<StackInfo> childStack;

    public MethodStackInfo() {
        init();
    }

    private void init() {

    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
        setElapsedTime(end - start);
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParamValues() {
        return paramValues;
    }

    public void setParamValues(Object[] paramValues) {
        if (paramValues != null && paramValues.length > 0) {
            this.paramValues = new String[paramValues.length];

            for (int i = 0; i < paramValues.length; i++) {
                Object paramValue = paramValues[i];
                this.paramValues[i] = ObjectConverter.convert(paramValue);
            }
        }
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = ObjectConverter.convert(returnValue);
    }

    public List<StackInfo> getChildStack() {
        return childStack;
    }

    @Override
    public StackInfo getParent() {
        return parent;
    }

    @Override
    public void setParent(StackInfo stackInfo) {
        this.parent = stackInfo;
    }

    @Override
    public void appendChild(StackInfo childStackInfo) {
        if (childStack == null) childStack = new ArrayList<StackInfo>();
        childStack.add(childStackInfo);
    }

    @Override
    public List<StackInfo> getChild() {
        return childStack;
    }
}
