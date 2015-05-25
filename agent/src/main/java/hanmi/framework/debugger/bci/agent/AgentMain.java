package hanmi.framework.debugger.bci.agent;

import hanmi.framework.debugger.bci.agent.config.AgentConfig;
import hanmi.framework.debugger.bci.agent.transformer.Transformer;
import hanmi.framework.debugger.bci.agent.config.TraceLevel;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class AgentMain {
    private AgentMain() {
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        try {
            AgentConfig.init(agentArgs);
            AgentConfig.log(new AgentConfig().toString());

            String javassistPath = AgentConfig.get(AgentConfig.PROP_JAVASSIST_PATH);

            File file = new File(javassistPath);
            JarFile jarFile = new JarFile(file);
            inst.appendToBootstrapClassLoaderSearch(jarFile);

            TraceLevel traceLevel = AgentConfig.getTraceLevel();
            if (traceLevel.equals(TraceLevel.NONE) == false) {
                Transformer transformer = new Transformer();
                inst.addTransformer(transformer, true);
            } else {
                AgentConfig.log("Trace Level이 None으로 설정되어 Trace를 추적하지 않습니다.");
            }
        } catch (IOException e) {
            AgentConfig.error(e);
        }
    }
}
