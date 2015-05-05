package fury.marvel.trinity.agent;

import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.TraceLevel;
import fury.marvel.trinity.agent.bci.transformer.Transformer;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class AgentMain {
    private AgentMain() {}
    
    public static void premain(String agentArgs, Instrumentation inst) {
        Logger logger = Logger.getLogger(AgentMain.class.getName());
        
        try {
            AgentConfig.init(agentArgs);

            TraceLevel traceLevel = AgentConfig.getTraceLevel();
            if (traceLevel.equals(TraceLevel.NONE) == false) {
                inst.addTransformer(new Transformer(), true);
                logger.log(Level.INFO, "[Trinity] 설정 완료. 설정값 : " + new AgentConfig().toString());
            } else {
                logger.log(Level.INFO, "[Trinity] 설정 완료. Trace Level이 None으로 설정되어 Trace를 추적하지 않습니다.");
            }
        } 
        catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
