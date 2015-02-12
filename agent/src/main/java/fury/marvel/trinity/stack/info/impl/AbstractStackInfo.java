package fury.marvel.trinity.stack.info.impl;

import fury.marvel.trinity.stack.info.StackInfo;

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
}
