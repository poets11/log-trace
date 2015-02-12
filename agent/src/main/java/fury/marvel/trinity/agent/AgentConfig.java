package fury.marvel.trinity.agent;

/**
 * Created by poets11 on 15. 2. 4..
 */
public class AgentConfig {
    public static final String BASE_PACKAGE = "BASE_PACKAGE";
    public static final String PARAM_SEPARATOR = ",";
    public static final String KEY_VALUE_SEPARATOR = "=";
    
    private static AgentConfig agentConfig = new AgentConfig();
    
    private String basePackage;
    
    public static AgentConfig getInstance() {
        return agentConfig;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
