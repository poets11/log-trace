package fury.marvel.trinity.stack;

import fury.marvel.trinity.agent.AgentConfig;
import fury.marvel.trinity.stack.info.SqlStackInfo;
import fury.marvel.trinity.stack.info.impl.RequestStackInfoImpl;
import fury.marvel.trinity.stack.info.impl.SqlStackInfoImpl;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StackManagerTest {
    @Test
    public void testInit() throws Exception {
        AgentConfig.init(null);

        assertFalse(StackManager.isInitialized());

        StackManager.init();
        assertTrue(StackManager.isInitialized());

        StackManager.clear();
        assertFalse(StackManager.isInitialized());
    }
    
    @Test
    public void testPushPop() throws IOException {
        AgentConfig.init(null);
        
        StackManager.init();
        assertTrue(StackManager.isInitialized());

        assertEquals(0, StackManager.size());

        RequestStackInfoImpl stackInfo = new RequestStackInfoImpl();
        
        StackManager.push(stackInfo);
        assertEquals(1, StackManager.size());
        
        StackManager.pop(stackInfo);
        assertEquals(0, StackManager.size());

        SqlStackInfoImpl sqlStackInfo = new SqlStackInfoImpl();
        StackManager.push(sqlStackInfo);
        
        assertEquals(sqlStackInfo, StackManager.peekSql());

        StackManager.pop(sqlStackInfo);
        assertEquals(0, StackManager.size());
    }
}