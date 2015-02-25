package fury.marvel.trinity.stack.info.impl;

import fury.marvel.trinity.agent.AgentConfig;
import fury.marvel.trinity.stack.info.StackInfo;
import fury.marvel.trinity.stack.info.TraceLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by poets11 on 15. 2. 4..
 */
public abstract class AbstractStackInfo implements StackInfo {
    protected int depth;
    protected long startTime;
    protected long endTime;
    protected long elapsedTime;
    protected List<StackInfo> childStack;
    protected Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    @Override
    public long getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public void appendChild(StackInfo stackInfo) {
        if (childStack == null) childStack = new ArrayList<StackInfo>();

        childStack.add(stackInfo);
    }

    @Override
    public List<StackInfo> getChildStack() {
        return childStack;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
        setElapsedTime(endTime - startTime);
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    
    @Override
    public boolean isWriteable(TraceLevel configuredLevel) {
        TraceLevel currLevel = getTraceLevel();
        return currLevel.compareTo(configuredLevel) > -1;
    }

    private TraceLevel getTraceLevel() {
        Exception currentException = getExceptionInStack(this);
        
        if(currentException != null) {
            Class conditionException = AgentConfig.getConditionException();
            if(conditionException.isInstance(currentException)) return TraceLevel.EXCEPTION;
        }
        else if (getElapsedTime() >= AgentConfig.getConditionTimeout()) {
            return TraceLevel.TIMEOUT;
        }
        
        return TraceLevel.ALL;
    }
    
    public static Exception getExceptionInStack(StackInfo stackInfo) {
        AbstractStackInfo abstractStackInfo = (AbstractStackInfo) stackInfo;

        if (abstractStackInfo.getException() != null) return abstractStackInfo.getException();

        List<StackInfo> childStacks = abstractStackInfo.getChildStack();
        if(childStacks != null) {
            for (int i = 0; i < childStacks.size(); i++) {
                AbstractStackInfo childAbstractStackInfo = (AbstractStackInfo) childStacks.get(i);
                
                Exception exceptionInStack = getExceptionInStack(childAbstractStackInfo);
                if(exceptionInStack != null) return exceptionInStack;             
            }
        }
        
        return null;
    }

    @Override
    public String toString() {
        return "AbstractStackInfo{" +
                "depth=" + depth +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", elapsedTime=" + elapsedTime +
                ", childStack=" + childStack +
                ", exception=" + exception +
                '}';
    }
}
