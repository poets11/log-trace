package fury.marvel.trinity.agent.bci.transformer.modifier.stack.info.marshall;

import org.json.simple.JSONValue;

/**
 * Created by poets11 on 15. 2. 25..
 */
public class JsonSimpleMarshaller implements Marshaller {
    @Override
    public StringObject marshall(Object object) {
        return marshall(object, false);
    }

    @Override
    public StringObject marshall(Object object, boolean useToString) {
        MapperObject mapperObject = new MapperObject();
        if(object == null) return mapperObject;

        mapperObject.setType(object.getClass());
        
        if(useToString) mapperObject.setValue(JSONValue.toJSONString(object));
        else mapperObject.setValue(object.toString());

        return mapperObject;
    }
}
