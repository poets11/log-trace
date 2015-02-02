package fury.marvel.shiva.trace.stack;

import java.util.List;

/**
 * Created by poets11 on 15. 1. 28..
 */
public interface StackInfo {
    void setParent(StackInfo stackInfo);
    StackInfo getParent();
    void appendChild(StackInfo childStackInfo);
    List<StackInfo> getChild();
}
