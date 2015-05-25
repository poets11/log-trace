package hanmi.framework.debugger.bci.agent.stack.info;

import hanmi.framework.debugger.bci.agent.stack.info.StackInfo;

import java.util.StringTokenizer;

/**
 * Created by poets11 on 15. 5. 20..
 */
public class ConnectionStackInfo extends StackInfo {
    private String sql;
    
    @Override
    public void setArguments(Object[] arguments) {
        if (arguments != null) {
            sql = removeBreakingWhitespace((String) arguments[0]);
        }
    }

    public String getSql() {
        return sql;
    }

    @Override
    public void setResultValue(Object resultValue) {

    }

    protected String removeBreakingWhitespace(String original) {
        StringTokenizer whitespaceStripper = new StringTokenizer(original);
        StringBuilder builder = new StringBuilder();
        while (whitespaceStripper.hasMoreTokens()) {
            builder.append(whitespaceStripper.nextToken());
            builder.append(" ");
        }
        return builder.toString();
    }
}
