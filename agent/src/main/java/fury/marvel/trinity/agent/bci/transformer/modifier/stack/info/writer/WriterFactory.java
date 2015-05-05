package fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.writer;


import fury.marvel.trinity.agent.bci.transformer.CtClassUtil;

import java.lang.reflect.Method;

/**
 * Created by poets11 on 15. 2. 6..
 */
public class WriterFactory {
    public static Writer getWriter() {
        ClassLoader urlClassLoader = CtClassUtil.getInstance().getUrlClassLoader();
        try {
            if (urlClassLoader != null) {
                Class<?> aClass = urlClassLoader.loadClass("org.slf4j.LoggerFactory");
                Method method = aClass.getMethod("getLogger", new Class[]{String.class});
                
                Object invoke = method.invoke(aClass, "[Trinity]");
                Method info = invoke.getClass().getMethod("info", new Class[]{String.class});
                
                return new ConsoleWriter(invoke, info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return new ConsoleWriter(null, null);
    }
}
