package fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.writer.converter.simple;

import fury.marvel.trinity.agent.AgentConfig;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.StackInfo;
import fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.writer.converter.Converter;

/**
 * Created by poets11 on 15. 2. 6..
 */
public class SimpleConverter implements Converter {
    protected final String lineNew = "%s\n";
    protected final String lineTitle = "%s %s\n";
    protected final String lineKeyValue = "%s %s : %s\n";

    protected String indentPrefix;
    protected String indentSuffix;
    
    public SimpleConverter() {
        indentPrefix = AgentConfig.get(AgentConfig.PROP_INDENT_PREFIX);
        indentSuffix = AgentConfig.get(AgentConfig.PROP_INDENT_SUFFIX);
    }
    
    public String getIndentString(int depth) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < depth; i++) sb.append(indentPrefix);
        sb.append(indentSuffix);

        return sb.toString();
    }
    
    @Override
    public String convert(StackInfo stackInfo) {
        if (stackInfo == null) return "";
        
        return getIndentString(stackInfo.getDepth()) + stackInfo.toString();
    }
}
