package fury.marvel.trinity.writer.converter.simple;

import fury.marvel.trinity.agent.AgentConfig;
import fury.marvel.trinity.stack.info.StackInfo;
import fury.marvel.trinity.writer.converter.Converter;

/**
 * Created by poets11 on 15. 2. 6..
 */
public class SimpleConverter implements Converter {
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
