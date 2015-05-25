package hanmi.framework.debugger.bci.agent.stack.info.marshall;

/**
 * Created by poets11 on 15. 2. 24..
 */
public class MarshallerFactory {
    public static Marshaller getMarshaller() {
        return new SimpleMarshaller();
    }
}
