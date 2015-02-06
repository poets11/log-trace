package fury.marvel.shiva.transformer;

import javassist.ClassPool;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by poets11 on 15. 2. 5..
 */
public class CtClassUtil {
    private ClassPool classPool;

    public CtClassUtil() {
        classPool = ClassPool.getDefault();
    }

    public CtClass createCtClass(byte[] classfileBuff) throws IOException {
        return classPool.makeClass(new ByteArrayInputStream(classfileBuff));
    }

    public CtClass createCtClass(Class clazz) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        String resourcePath = clazz.getName().replaceAll("[.]", "/") + ".class";

        InputStream resourceAsStream = null;
        try {
            resourceAsStream = classLoader.getResourceAsStream(resourcePath);
            return classPool.makeClass(resourceAsStream);
        } finally {
            if(resourceAsStream != null) resourceAsStream.close();
        }
    }
}
