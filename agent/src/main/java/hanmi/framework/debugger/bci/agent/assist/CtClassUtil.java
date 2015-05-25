package hanmi.framework.debugger.bci.agent.assist;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by poets11 on 15. 2. 5..
 */
public class CtClassUtil {
    private static CtClassUtil classUtil = new CtClassUtil();
    private ClassPool classPool;
    private ClassLoader urlClassLoder = null;
    private List<Integer> addedLoaderHashCodes = new ArrayList<Integer>();

    public static CtClassUtil getInstance() {
        return classUtil;
    }

    public CtClassUtil() {
        classPool = ClassPool.getDefault();
    }
    
    public ClassPool getClassPool() {
        return this.classPool;
    }

    public ClassLoader getUrlClassLoader() {
        return urlClassLoder;
    }

    public CtClass createCtClass(ClassLoader loader, String className, byte[] classfileBuff) throws ClassNotFoundException, IOException, NotFoundException {
        appendLoader(loader);

//        String loaderName = loader.getClass().getName();
//
//        if (loaderName.equals("java.net.URLClassLoader")) {
//            urlClassLoder = loader;
//        }
//
//        classPool.insertClassPath(new LoaderClassPath(loader));
        return createCtClass(classfileBuff);
    }

    private void appendLoader(ClassLoader loader) {
        String loaderName = loader.getClass().getName();
        int hashCode = loader.hashCode();
        if(addedLoaderHashCodes.contains(hashCode) == false) {
            ClassPool pool = new ClassPool(this.classPool);
            pool.appendClassPath(new LoaderClassPath(loader));
//            pool.appendSystemPath();         // the same class path as the default one.
            pool.childFirstLookup = true;
            this.classPool = pool;
            addedLoaderHashCodes.add(hashCode);
        }
    }

    public CtClass createCtClass(byte[] classfileBuff) throws IOException {
        return classPool.makeClass(new ByteArrayInputStream(classfileBuff));
    }

    public CtClass createCtClass(Class clazz) throws IOException {
//        classPool.insertClassPath(new ClassClassPath(clazz));
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
