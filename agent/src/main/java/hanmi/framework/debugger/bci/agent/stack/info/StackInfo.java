package hanmi.framework.debugger.bci.agent.stack.info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by poets11 on 15. 5. 20..
 */
public abstract class StackInfo {
    private String className;
    private String methodName;
    private long startTime;
    private long endTime;
    
    private List<StackInfo> innerStacks;

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

    public List<StackInfo> getInnerStacks() {
        return innerStacks;
    }

    public void setInnerStacks(List<StackInfo> innerStacks) {
        this.innerStacks = innerStacks;
    }

    public void appendInnerStack(StackInfo stackInfo) {
        if (innerStacks == null) {
            innerStacks = new ArrayList<StackInfo>();
        }

        innerStacks.add(stackInfo);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        if (endTime == 0) {
            endTime = System.currentTimeMillis();
        }

        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getElapsedTime() {
        return getEndTime() - getStartTime();
    }

    public abstract void setArguments(Object[] arguments);

    public abstract void setResultValue(Object resultValue);

    @Override
    public String toString() {
        String base = className.substring(className.lastIndexOf(".") + 1) + "." + methodName;

        if (innerStacks != null) {
            base += " / " + innerStacks;
        }

        return base;
    }
}
