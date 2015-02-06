package fury.marvel.shiva.stack;

import fury.marvel.shiva.stack.info.StackInfo;
import fury.marvel.shiva.stack.info.impl.PackageStackInfoImpl;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StackManagerTest {

    @org.junit.Test
    public void testStackManager() throws Exception {
        StackInfo stackInfo = StackManager.peek();
        assertNull(stackInfo);

        stackInfo = new PackageStackInfoImpl();
        StackManager.push(stackInfo);

        StackInfo peekStackInfo = StackManager.peek();
        assertEquals(stackInfo.getStartTime(), peekStackInfo.getStartTime());

        PackageStackInfoImpl childStackInfo = new PackageStackInfoImpl();
        StackManager.push(childStackInfo);

        StackManager.pop(childStackInfo);
        StackManager.pop(peekStackInfo);

        List<StackInfo> childStacks = peekStackInfo.getChildStack();
        assertEquals(childStacks.size(), 1);

        assertEquals(childStacks.get(0), childStackInfo);

        assertNull(StackManager.peek());
    }
}