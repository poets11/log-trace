package fury.marvel.trinity.transformer;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by poets11 on 15. 2. 5..
 */
public class CtClassUtil {
    private static CtClassUtil classUtil = new CtClassUtil();
    
    private ClassPool classPool;
    private List<String> ignoreClassLoaders;

    public static CtClassUtil getInstance() { return classUtil; }
    
    public CtClassUtil() {
        classPool = ClassPool.getDefault();
        ignoreClassLoaders = Arrays.asList(new String[]{"sun.", "java.", "javax."});
    }

    // sun.misc.Launcher$AppClassLoader, 
    // javax.management.remote.rmi.NoCallStackClassLoader, 
    // sun.reflect.DelegatingClassLoader, 
    // sun.misc.Launcher$ExtClassLoader, 
    // org.apache.catalina.loader.StandardClassLoader, 
    // org.apache.catalina.loader.WebappClassLoader, 
    // org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl$TcclSafeAggregatedClassLoader]
   
    public CtClass createCtClass(ClassLoader loader, String className, byte[] classfileBuff) throws ClassNotFoundException, IOException, NotFoundException {
        classPool.insertClassPath(new LoaderClassPath(loader));
        return createCtClass(classfileBuff);
    }

    private boolean notIgnoreTarget(String loaderName) {
        for (int i = 0; i < ignoreClassLoaders.size(); i++) {
            String ignore = ignoreClassLoaders.get(i);
            if(loaderName.startsWith(ignore) == true) return true;
        }
        
        return false;
    }

    public CtClass createCtClass(byte[] classfileBuff) throws IOException {
        return classPool.makeClass(new ByteArrayInputStream(classfileBuff));
    }

    public CtClass createCtClass(Class clazz) throws IOException {
        classPool.insertClassPath(new ClassClassPath(clazz));
        ClassLoader classLoader = this.getClass().getClassLoader();
        String resourcePath = clazz.getName().replaceAll("[.]", "/") + ".class";

        InputStream resourceAsStream = null;
        try {
            resourceAsStream = classLoader.getResourceAsStream(resourcePath);
            return classPool.makeClass(resourceAsStream);
        } finally {
            if (resourceAsStream != null) resourceAsStream.close();
        }
    }
}
