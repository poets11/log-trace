package fury.marvel.shiva.transformer;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

/**
 * Created by poets11 on 15. 1. 30..
 */
public class DynamicProxyTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            if (className.startsWith("ces") && className.indexOf("$") < 0 && className.indexOf("WebApplication") < 0) {
                ClassPool aDefault = ClassPool.getDefault();
                CtClass ctClass = aDefault.makeClass(new ByteArrayInputStream((classfileBuffer)));

                Class aClass = ctClass.toClass();

                ProxyFactory proxyFactory = new ProxyFactory();
                proxyFactory.setSuperclass(aClass);
                Class proxyFactoryClass = proxyFactory.createClass();

                MethodHandler handler = new MethodHandler() {
                    @Override
                    public Object invoke(Object self, Method overridden, Method forwarder, Object[] args) throws Throwable {
                        System.out.println("do something " + overridden.getName());
                        return forwarder.invoke(self, args);
                    }
                };

                Object instance = proxyFactoryClass.newInstance();
                ProxyObject proxy = (ProxyObject) instance;
                proxy.setHandler(handler);

                ctClass.detach();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return classfileBuffer;
    }
}
