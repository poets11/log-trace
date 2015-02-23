package fury.marvel.trinity.agent;

import fury.marvel.trinity.stack.info.TraceLevel;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class AgentConfigTest {

    @Test
    public void testDefaultInit() throws IOException {
        AgentConfig.init(null);

        assertEquals("|--", AgentConfig.get(AgentConfig.PROP_INDENT_PREFIX));
        assertEquals("|--> ", AgentConfig.get(AgentConfig.PROP_INDENT_SUFFIX));
        assertEquals("ALL", AgentConfig.get(AgentConfig.PROP_TRACE_LEVEL));
        assertEquals("10000", AgentConfig.get(AgentConfig.PROP_CONDITION_TIMEOUT));
        assertEquals("java.lang.Exception", AgentConfig.get(AgentConfig.PROP_CONDITION_EXCEPTION));

        assertEquals(TraceLevel.ALL, AgentConfig.getTraceLevel());
        assertEquals(java.lang.Exception.class, AgentConfig.getConditionException());
        assertEquals(10000, AgentConfig.getConditionTimeout());
    }
    
    @Test
    public void testArgumentInit() throws IOException {
        String args = "BASE_PACKAGE=ces,INDENT_PREFIX=++,INDENT_SUFFIX=> ,TRACE_LEVEL=NONE,CONDITION_TIMEOUT=10,CONDITION_EXCEPTION=java.lang.NullPointerException";
        
        AgentConfig.init(args);

        assertEquals("ces", AgentConfig.get(AgentConfig.PROP_BASE_PACKAGE));
        assertEquals("++", AgentConfig.get(AgentConfig.PROP_INDENT_PREFIX));
        assertEquals("> ", AgentConfig.get(AgentConfig.PROP_INDENT_SUFFIX));
        assertEquals("NONE", AgentConfig.get(AgentConfig.PROP_TRACE_LEVEL));
        assertEquals("10", AgentConfig.get(AgentConfig.PROP_CONDITION_TIMEOUT));
        assertEquals("java.lang.NullPointerException", AgentConfig.get(AgentConfig.PROP_CONDITION_EXCEPTION));
        
        assertEquals(TraceLevel.NONE, AgentConfig.getTraceLevel());
        assertEquals(java.lang.NullPointerException.class, AgentConfig.getConditionException());
        assertEquals(10, AgentConfig.getConditionTimeout());
    }
    
    @Test
    public void testCustomPropertiesFileInit() throws IOException {
        String args = "BASE_PACKAGE=ces,CONFIG_FILE_PATH=/Users/poets11/git/log-trace/agent/src/main/resources/fury/marvel/trinity/agent/config.properties";

        AgentConfig.init(args);

        assertEquals("ces", AgentConfig.get(AgentConfig.PROP_BASE_PACKAGE));
        assertEquals("|--", AgentConfig.get(AgentConfig.PROP_INDENT_PREFIX));
        assertEquals("|--> ", AgentConfig.get(AgentConfig.PROP_INDENT_SUFFIX));
        assertEquals("ALL", AgentConfig.get(AgentConfig.PROP_TRACE_LEVEL));
        assertEquals("10000", AgentConfig.get(AgentConfig.PROP_CONDITION_TIMEOUT));
        assertEquals("java.lang.Exception", AgentConfig.get(AgentConfig.PROP_CONDITION_EXCEPTION));

        assertEquals(TraceLevel.ALL, AgentConfig.getTraceLevel());
        assertEquals(java.lang.Exception.class, AgentConfig.getConditionException());
        assertEquals(10000, AgentConfig.getConditionTimeout());
    }
}