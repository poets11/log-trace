package fury.marvel.trinity.stack.info;

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
}
