package hanmi.framework.debugger.bci.agent.stack.info;

import hanmi.framework.debugger.bci.agent.stack.info.marshall.MarshallerFactory;
import hanmi.framework.debugger.bci.agent.stack.info.marshall.StringObject;

/**
 * Created by poets11 on 15. 5. 20..
 */
public class ResultSetStackInfo extends StackInfo {
    private String columnName;
    private StringObject value;
    private boolean next;
    
    @Override
    public void setArguments(Object[] arguments) {
        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];
                columnName = (String)argument;
            }
        }
    }

    public String getColumnName() {
        return columnName;
    }

    public StringObject getValue() {
        return value;
    }

    @Override
    public void setResultValue(Object resultValue) {
        if (resultValue != null) {
            if (getMethodName().equals("next")) {
                next = (Boolean)resultValue;
            } else {
                value = MarshallerFactory.getMarshaller().marshall(resultValue);
            }
        }
    }

    public boolean hasNext() {
        return next;
    }
}
