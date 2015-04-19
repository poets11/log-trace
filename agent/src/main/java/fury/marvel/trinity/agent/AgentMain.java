package fury.marvel.trinity.agent;

import fury.marvel.trinity.stack.info.TraceLevel;
import fury.marvel.trinity.transformer.Transformer;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.util.PropertyResourceBundle;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class AgentMain {
    public static void premain(String agentArgs, Instrumentation inst) {
        try {
            AgentConfig.init(agentArgs);

            TraceLevel traceLevel = AgentConfig.getTraceLevel();
            if(traceLevel.equals(TraceLevel.NONE) == false) {
                inst.addTransformer(new Transformer(), true);
                System.out.println("[Trinity] 설정 완료. 설정값 : " + new AgentConfig().toString());
            } else {
                System.out.println("[Trinity] 설정 완료. Trace Level이 None으로 설정되어 Trace를 추적하지 않습니다.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
