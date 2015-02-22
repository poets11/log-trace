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
            } else {
                System.out.println("__ trace level none");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
