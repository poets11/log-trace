package fury.marvel.trinity.agent;

import fury.marvel.trinity.transformer.Transformer;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.util.PropertyResourceBundle;

/**
 * Created by poets11 on 15. 1. 28..
 */
public class AgentMain {
    public static void premain(String agentArgs, Instrumentation inst) {
        initConfig(agentArgs);
        inst.addTransformer(new Transformer(), true);
    }

    private static void initConfig(String agentArgs) {
        if(agentArgs == null || agentArgs.trim().length() == 0) return;

        String[] split = agentArgs.split(AgentConfig.PARAM_SEPARATOR);
        for (int i = 0; i < split.length; i++) {
            String params = split[i];
            initConfigParam(params);
        }
    }

    private static void initConfigParam(String params) {
        String[] split = params.split(AgentConfig.KEY_VALUE_SEPARATOR);
        if (split.length != 2) return;
        
        if(AgentConfig.BASE_PACKAGE.equals(split[0])) {
            AgentConfig.getInstance().setBasePackage(split[1]);
        }
    }
}
