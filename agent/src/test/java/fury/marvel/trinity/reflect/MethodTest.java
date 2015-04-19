package fury.marvel.trinity.reflect;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class MethodTest {
    @Test
    public void testMethod() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method<String> method = new Method<String>();
        method.setName("toString");
        
        String str = "hello";

        String invoke = method.invoke(str, null);
        assertEquals(str, invoke);

        method = new Method<String>();
        method.setName("valueOf");
        method.setParamTypeNames("java.lang.Object");

        invoke = method.invoke(str, 3l);
        assertEquals("3", invoke);
    }
}