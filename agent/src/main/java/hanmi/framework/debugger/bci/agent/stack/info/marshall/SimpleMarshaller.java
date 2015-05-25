package hanmi.framework.debugger.bci.agent.stack.info.marshall;

/**
 * Created by poets11 on 15. 5. 21..
 */
public class SimpleMarshaller implements Marshaller {
    @Override
    public StringObject marshall(Object object) {
        return marshall(object, true);
    }

    @Override
    public StringObject marshall(Object object, boolean useToString) {
        MapperObject mapperObject = new MapperObject();
        
        mapperObject.setType(object.getClass());
        mapperObject.setValue(object.toString());
        
        return mapperObject;
    }
}
