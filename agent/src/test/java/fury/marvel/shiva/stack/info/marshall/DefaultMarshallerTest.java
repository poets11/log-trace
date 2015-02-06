package fury.marvel.shiva.stack.info.marshall;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class DefaultMarshallerTest {
    @Test
    public void testMarshall() throws Exception {
        String str = "hello";
        StringObject stringObject = DefaultMarshaller.marshall(str);

        assertEquals(String.class, stringObject.getType());
        assertEquals("\"hello\"", stringObject.getValue());

        int i = 3;
        stringObject = DefaultMarshaller.marshall(i);

        assertEquals(Integer.class, stringObject.getType());
        assertEquals("3", stringObject.getValue());

        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");

        stringObject = DefaultMarshaller.marshall(list);
        assertEquals(ArrayList.class, stringObject.getType());
        assertEquals("[\"a\",\"b\"]", stringObject.getValue());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", "hello");
        map.put("b", 3);

        DefaultMarshaller.marshall(map);
    }
}