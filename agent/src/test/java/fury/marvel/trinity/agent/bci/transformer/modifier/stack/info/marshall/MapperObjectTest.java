package fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.marshall;

import fury.marvel.trinity.agent.AgentConfig;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class MapperObjectTest {
    @Test
    public void testMarshall() throws IOException {
        AgentConfig.init(null);

        Marshaller marshaller = MarshallerFactory.getMarshaller();

        StringObject marshall = marshaller.marshall("hello");
        assertNotNull(marshall);
        assertEquals(String.class, marshall.getType());
        assertEquals("hello", marshall.getValue());

        marshall = marshaller.marshall(null);
        assertNotNull(marshall);
        assertNull(marshall.getType());
        assertNull(marshall.getValue());
    }
}