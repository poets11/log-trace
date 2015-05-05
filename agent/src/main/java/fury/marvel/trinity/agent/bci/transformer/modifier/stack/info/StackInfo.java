package fury.marvel.trinity.agent.bci.transformer.modifier.stack.info;

import java.util.List;

/**
 * Created by poets11 on 15. 1. 28..
 */
public interface StackInfo {
    long getStartTime();

    long getEndTime();

    long getElapsedTime();
    
    int getDepth();

    void appendChild(StackInfo stackInfo);
    
    List<StackInfo> getChildStack();

    Exception getException();

    boolean isWriteable(TraceLevel configuredLevel);
}
