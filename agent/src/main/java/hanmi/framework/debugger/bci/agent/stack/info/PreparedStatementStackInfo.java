package hanmi.framework.debugger.bci.agent.stack.info;

import hanmi.framework.debugger.bci.agent.stack.info.marshall.MarshallerFactory;
import hanmi.framework.debugger.bci.agent.stack.info.marshall.StringObject;

/**
 * Created by poets11 on 15. 5. 20..
 */
public class PreparedStatementStackInfo extends StackInfo {
    private int index;
    private StringObject param;

    @Override
    public void setArguments(Object[] arguments) {
        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];

                if (i == 0) {
                    index = (Integer) argument;
                } else {
                    param = MarshallerFactory.getMarshaller().marshall(argument);
                }
            }
        }
    }

    public StringObject getParam() {
        return param;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void setResultValue(Object resultValue) {

    }
}
