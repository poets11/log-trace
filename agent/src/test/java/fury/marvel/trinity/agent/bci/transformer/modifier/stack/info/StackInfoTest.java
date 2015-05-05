package fury.marvel.trinity.agent.bci.transformer.modifier.stack.info;

import fury.marvel.trinity.agent.AgentConfig;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.impl.PackageStackInfoImpl;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class StackInfoTest {
    @Test
    public void testWriteable() throws IOException {
        AgentConfig.init(null);
        
        PackageStackInfoImpl stackInfo = new PackageStackInfoImpl();
        stackInfo.setException(new NullPointerException("NullPointerException"));

        assertTrue(stackInfo.isWriteable(TraceLevel.ALL));
        assertTrue(stackInfo.isWriteable(TraceLevel.TIMEOUT));
        assertTrue(stackInfo.isWriteable(TraceLevel.EXCEPTION));
        assertFalse(stackInfo.isWriteable(TraceLevel.NONE));
        
        
        stackInfo = new PackageStackInfoImpl();
        stackInfo.setElapsedTime(1000000);

        assertTrue(stackInfo.isWriteable(TraceLevel.ALL));
        assertTrue(stackInfo.isWriteable(TraceLevel.TIMEOUT));
        assertFalse(stackInfo.isWriteable(TraceLevel.EXCEPTION));
        assertFalse(stackInfo.isWriteable(TraceLevel.NONE));
        
        
        stackInfo = new PackageStackInfoImpl();

        assertTrue(stackInfo.isWriteable(TraceLevel.ALL));
        assertFalse(stackInfo.isWriteable(TraceLevel.TIMEOUT));
        assertFalse(stackInfo.isWriteable(TraceLevel.EXCEPTION));
        assertFalse(stackInfo.isWriteable(TraceLevel.NONE));
    }

}