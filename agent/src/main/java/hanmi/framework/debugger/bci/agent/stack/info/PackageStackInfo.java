package hanmi.framework.debugger.bci.agent.stack.info;


import hanmi.framework.debugger.bci.agent.stack.info.marshall.MapperObject;
import hanmi.framework.debugger.bci.agent.stack.info.marshall.MarshallerFactory;
import hanmi.framework.debugger.bci.agent.stack.info.marshall.StringObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by poets11 on 15. 5. 20..
 */
public class PackageStackInfo extends StackInfo {
    private List<StringObject> argumentStringObjects;
    private StringObject resultValue;

    @Override
    public void setArguments(Object[] arguments) {
        if (arguments != null && arguments.length > 0) {
            argumentStringObjects = new ArrayList<StringObject>();

            for (int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];
                StringObject marshall = MarshallerFactory.getMarshaller().marshall(argument);
                argumentStringObjects.add(marshall);
            }
        }
    }

    public List<StringObject> getArgumentStringObjects() {
        return argumentStringObjects;
    }

    @Override
    public void setResultValue(Object resultValue) {
        if (resultValue != null) {
            StringObject marshall = MarshallerFactory.getMarshaller().marshall(resultValue);
            this.resultValue = marshall;
        } else {
            this.resultValue = MapperObject.EmptyObject.getInstance();
        }
    }

    public StringObject getResultValue() {
        return resultValue;
    }
}
