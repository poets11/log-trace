package fury.marvel.shiva.agent;

import fury.marvel.shiva.transformer.Transformer;

import java.lang.instrument.Instrumentation;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class AgentMain {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new Transformer(), true);

        /*inst.addTransformer(new DynamicProxyTransformer());

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ProxyClassLoader proxyClassLoader = new ProxyClassLoader(contextClassLoader);

        Thread.currentThread().setContextClassLoader(proxyClassLoader);*/
    }
}
