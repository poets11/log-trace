package fury.marvel.trinity.agent;


import fury.marvel.trinity.stack.info.TraceLevel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by poets11 on 15. 2. 4..
 */
public class AgentConfig {
    public static final String PROP_BASE_PACKAGE = "BASE_PACKAGE";
    public static final String PROP_INDENT_PREFIX = "INDENT_PREFIX";
    public static final String PROP_INDENT_SUFFIX = "INDENT_SUFFIX";
    public static final String PROP_TRACE_LEVEL = "TRACE_LEVEL";
    public static final String PROP_CONDITION_TIMEOUT = "CONDITION_TIMEOUT";
    public static final String PROP_CONDITION_EXCEPTION = "CONDITION_EXCEPTION";

    public static final String CONFIG_FILE_PATH = "CONFIG_FILE_PATH";
    private static final String ARGS_BLOCK = ",";
    private static final String ARGS_PAIR = "=";

    private static final String DEFAULT_CONFIG_FILE_PATH = "fury/marvel/trinity/agent/config.properties";

    private static Map<String, String> CONFIG = new HashMap<String, String>();

    public static void init(String agentArgs) throws IOException {
        Map<String, String> argsMap = createArgsMap(agentArgs);

        // load default properteis
        loadDefaultProperties();

        // load custom properties
        if (argsMap.containsKey(CONFIG_FILE_PATH)) loadCustomProperties(argsMap.get(CONFIG_FILE_PATH));

        // load command line properties
        loadArgumentProperties(argsMap);
    }

    private static void loadDefaultProperties() throws IOException {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE_PATH);

        Properties properties = new Properties();
        properties.load(stream);

        loadConfig(properties);

        stream.close();
    }

    private static void loadCustomProperties(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists() == false) return;

        FileInputStream stream = new FileInputStream(file);

        Properties properties = new Properties();
        properties.load(stream);

        loadConfig(properties);

        stream.close();
    }

    private static void loadArgumentProperties(Map<String, String> argsMap) {
        loadConfig(argsMap);
    }

    private static void loadConfig(Map argsMap) {
        putConfig(PROP_BASE_PACKAGE, argsMap.get(PROP_BASE_PACKAGE));
        putConfig(PROP_INDENT_PREFIX, argsMap.get(PROP_INDENT_PREFIX));
        putConfig(PROP_INDENT_SUFFIX, argsMap.get(PROP_INDENT_SUFFIX));
        putConfig(PROP_TRACE_LEVEL, argsMap.get(PROP_TRACE_LEVEL));
        putConfig(PROP_CONDITION_TIMEOUT, argsMap.get(PROP_CONDITION_TIMEOUT));
        putConfig(PROP_CONDITION_EXCEPTION, argsMap.get(PROP_CONDITION_EXCEPTION));
    }

    private static void putConfig(String key, Object value) {
        if (value != null) CONFIG.put(key, (String) value);
    }

    private static Map<String, String> createArgsMap(String agentArgs) {
        Map<String, String> map = new HashMap<String, String>();

        if (agentArgs == null) return map;

        String[] blocks = agentArgs.split(AgentConfig.ARGS_BLOCK);
        for (int i = 0; i < blocks.length; i++) {
            String[] pair = blocks[i].split(AgentConfig.ARGS_PAIR);

            if (pair.length == 2) {
                map.put(pair[0], pair[1]);
            }
        }

        return map;
    }

    public static String get(String key) {
        return CONFIG.get(key);
    }

    public static long getConditionTimeout() {
        return Long.valueOf(get(PROP_CONDITION_TIMEOUT));
    }

    public static Class getConditionException() {
        try {
            return Class.forName(get(PROP_CONDITION_EXCEPTION));
        } catch (ClassNotFoundException e) {
            return Exception.class;
        }
    }

    public static TraceLevel getTraceLevel() {
        return TraceLevel.valueOf(get(PROP_TRACE_LEVEL));
    }
}
